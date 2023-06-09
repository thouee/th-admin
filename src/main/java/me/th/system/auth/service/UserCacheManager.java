package me.th.system.auth.service;

import cn.hutool.core.util.RandomUtil;
import me.th.share.util.RedisUtils;
import me.th.system.auth.domain.LoginProperties;
import me.th.system.auth.service.dto.JwtUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserCacheManager {

    @Resource
    private RedisUtils redisUtils;

    @Value("${login.keep-alive-time}")
    private long keepAliveTime;

    /**
     * 从缓存中获取登录信息
     *
     * @param username -
     * @return JwtUserDto
     */
    public JwtUserDto getUserCache(String username) {
        if (StringUtils.isNotBlank(username)) {
            Object o = redisUtils.hGet(LoginProperties.loginCacheKey, username);
            if (o != null) {
                return ((JwtUserDto) o);
            }
        }
        return null;
    }

    /**
     * 添加登录信息到缓存中
     *
     * @param username -
     * @param jwtUser  -
     */
    @Async
    public void addUserCache(String username, JwtUserDto jwtUser) {
        if (StringUtils.isNotBlank(username)) {
            // 修改过期时间，避免同时过期
            long expire = keepAliveTime + RandomUtil.randomInt(900, 1800);
            redisUtils.hSet(LoginProperties.loginCacheKey, username, jwtUser, expire);
        }
    }

    /**
     * 清除缓存中的登录信息
     *
     * @param username -
     */
    public void cleanUserCache(String username) {
        if (StringUtils.isNotBlank(username)) {
            redisUtils.hDel(LoginProperties.loginCacheKey, username);
        }
    }
}
