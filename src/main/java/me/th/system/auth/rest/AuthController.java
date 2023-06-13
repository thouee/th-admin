package me.th.system.auth.rest;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import me.th.share.common.R;
import me.th.share.exception.Checker;
import me.th.share.rest.AnonymousGetMapping;
import me.th.share.rest.AnonymousPostMapping;
import me.th.share.util.JacksonUtils;
import me.th.share.util.RedisUtils;
import me.th.system.auth.component.TokenProvider;
import me.th.system.auth.domain.LoginCaptchaMode;
import me.th.system.auth.domain.LoginProperties;
import me.th.system.auth.domain.SecurityProperties;
import me.th.system.auth.service.OnlineUserService;
import me.th.system.auth.service.dto.AuthUserDto;
import me.th.system.auth.service.dto.JwtUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RedisUtils redisUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final LoginProperties loginProperties;
    private final SecurityProperties securityProperties;
    private final OnlineUserService onlineUserService;

    @AnonymousPostMapping(value = "/login")
    public R<ObjectNode> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) {
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        // if 验证码已过期
        Checker.CAPTCHA_NOT_FOUND.isTrue(StringUtils.isBlank(code));
        // if 验证码不正确
        boolean isTrue = StringUtils.isBlank(authUser.getCode()) || !code.equalsIgnoreCase(authUser.getCode());
        Checker.CAPTCHA_INCORRECT.isTrue(isTrue);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = tokenProvider.createToken(authenticationToken);
        JwtUserDto jwtUser = (JwtUserDto) authenticate.getPrincipal();

        // 保存在线用户信息
        onlineUserService.save(jwtUser, token, request);
        // 强退之前已经登陆的 token
        if (loginProperties.isSingleLogin()) {
            onlineUserService.kickOut(token);
        }

        ObjectNode node = JacksonUtils.newObjectNode();
        node.put("token", securityProperties.getPrefix() + token);
        node.putPOJO("user", jwtUser);
        return R.ok(node);
    }

    @AnonymousGetMapping("/captcha")
    public R<ObjectNode> getCaptcha() {
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = securityProperties.getCaptchaKey() + IdUtil.simpleUUID();
        // 当验证码类型为 ARITHMETIC 且长度 >= 2 时，captcha.text() 有可能为浮点数
        String value = captcha.text();
        if (captcha.getCharType() - 1 == LoginCaptchaMode.ARITHMETIC.ordinal() && value.contains(".")) {
            value = value.split("\\.")[0];
        }
        // 缓存
        redisUtils.set(uuid, value, loginProperties.getLoginCaptcha().getExpire(), TimeUnit.MINUTES);

        ObjectNode node = JacksonUtils.newObjectNode();
        node.put("captcha", captcha.toBase64());
        node.put("uuid", uuid);
        return R.ok(node);
    }

    @DeleteMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        onlineUserService.logout(token);
        return R.ok();
    }
}
