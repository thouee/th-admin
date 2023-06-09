package me.th.system.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.th.share.exception.Checker;
import me.th.system.auth.service.dto.JwtUserDto;
import me.th.system.user.service.UserService;
import me.th.system.user.service.dto.UserLoginDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserCacheManager userCacheManager;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 先从缓存中获取
        JwtUserDto jwtUser = userCacheManager.getUserCache(username);
        if (jwtUser == null) {
            UserLoginDto loginData = userService.getLoginData(username);
            if (loginData == null) {
                // SpringSecurity 会自动转换 UsernameNotFoundException 为 BadCredentialsException
                throw new UsernameNotFoundException(username);
            }
            // if 用户被禁用
            Checker.USER_NOT_ENABLED.isTrue(!loginData.getEnabled());
            jwtUser = new JwtUserDto(loginData, Set.of());
            // 添加登录缓存
            userCacheManager.addUserCache(username, jwtUser);
        }
        return jwtUser;
    }
}
