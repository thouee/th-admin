package me.th.system.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.th.share.exception.Checker;
import me.th.system.security.service.dto.JwtUserDto;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserLoginDto loginData = userService.getLoginData(username);
            // if 用户被禁用
            Checker.USER_NOT_ENABLED.isTrue(!loginData.getEnabled());

            return new JwtUserDto(loginData, Set.of());
        } catch (Exception e) {
            throw new UsernameNotFoundException("");
        }
    }
}
