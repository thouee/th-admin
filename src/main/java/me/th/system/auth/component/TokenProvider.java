package me.th.system.auth.component;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import me.th.share.exception.Checker;
import me.th.share.util.RedisUtils;
import me.th.system.auth.domain.SecurityProperties;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    public static final String AUTHORITIES_KEY = "user";
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    public TokenProvider(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Checker.CONFIGURATION_ERROR.isNull(properties.getBase64Secret(), "JWT 配置出错，请设置属性 base64-secret");
        byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        jwtBuilder = Jwts.builder().signWith(key, SignatureAlgorithm.HS512);
    }

    /**
     * 创建 Token 设置永不过期，Token 的时间有效性交给 Redis 维护
     *
     * @param authentication -
     * @return String
     */
    public String createToken(Authentication authentication) {
        return jwtBuilder
                // 加入 ID 确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                .claim(AUTHORITIES_KEY, authentication.getName())
                .setSubject(authentication.getName())
                .compact();
    }

    /**
     * 根据 Token 获取鉴权信息
     *
     * @param token -
     * @return Authentication
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        User principal = new User(claims.getSubject(), "****", Lists.newArrayList());
        return new UsernamePasswordAuthenticationToken(principal, token, Lists.newArrayList());
    }

    public Claims getClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    /**
     * 续签检查
     *
     * @param token -
     */
    public void checkRenew(String token) {
        // 计算 token 过期时间
        long time = redisUtils.getExpire(properties.getOnlineKey() + token) * 1000;
        DateTime expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        Checker.AUTH_EXPIRE_ERROR.isTrue(differ <= 0);
        // 如果在续签检查时间范围内，则续签
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisUtils.expire(properties.getCaptchaKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(properties.getHeader());
        if (token != null && token.startsWith(properties.getPrefix())) {
            return token.substring(properties.getPrefix().length() + 1);
        }
        return null;
    }
}
