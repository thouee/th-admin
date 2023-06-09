package me.th.system.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.th.share.util.RedisUtils;
import me.th.share.util.WebUtils;
import me.th.system.auth.domain.SecurityProperties;
import me.th.system.auth.service.dto.JwtUserDto;
import me.th.system.auth.service.dto.OnlineUserDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineUserService {

    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;

    /**
     * 缓存在线用户信息
     *
     * @param jwtUser -
     * @param token   -
     * @param request -
     */
    public void save(JwtUserDto jwtUser, String token, HttpServletRequest request) {
        String ip = WebUtils.getIP(request);
        String browser = WebUtils.getBrowser(request);
        String cityInfo = WebUtils.getCityInfo(ip);

        OnlineUserDto onlineUserDto = new OnlineUserDto();
        onlineUserDto.setUsername(jwtUser.getUsername());
        onlineUserDto.setNickname(jwtUser.getUser().getNickName());
        onlineUserDto.setIp(ip);
        onlineUserDto.setBrowser(browser);
        onlineUserDto.setAddress(cityInfo);
        onlineUserDto.setKey(token);
        onlineUserDto.setLoginTime(LocalDateTime.now());

        redisUtils.set(securityProperties.getOnlineKey() + jwtUser.getUsername(), onlineUserDto,
                securityProperties.getExpire(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取在线用户信息
     *
     * @param key -
     * @return OnlineUserDto
     */
    public OnlineUserDto getOne(String key) {
        return ((OnlineUserDto) redisUtils.get(key));
    }

    /**
     * 批量获取在线用户信息
     *
     * @param predicate 条件过滤
     * @return List<OnlineUserDto>
     */
    public List<OnlineUserDto> getAll(Predicate<OnlineUserDto> predicate) {
        List<String> keys = redisUtils.scan(securityProperties.getOnlineKey() + "*");
        List<OnlineUserDto> users = new ArrayList<>();
        List<Object> objs = redisUtils.get(keys);
        for (Object o : objs) {
            OnlineUserDto ol = (OnlineUserDto) o;
            if (predicate == null) {
                users.add(ol);
            } else {
                boolean isTrue = predicate.test(ol);
                if (isTrue) {
                    users.add(ol);
                }
            }
        }
        users.sort(Comparator.comparing(OnlineUserDto::getLoginTime));
        return users;
    }

    /**
     * 退出登录
     *
     * @param key -
     */
    public void logout(String key) {
        redisUtils.del(securityProperties.getOnlineKey() + key);
    }
}
