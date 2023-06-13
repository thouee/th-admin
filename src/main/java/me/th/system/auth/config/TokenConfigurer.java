package me.th.system.auth.config;

import lombok.RequiredArgsConstructor;
import me.th.system.auth.component.TokenFilter;
import me.th.system.auth.component.TokenProvider;
import me.th.system.auth.domain.SecurityProperties;
import me.th.system.auth.service.OnlineUserService;
import me.th.system.auth.service.UserCacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final UserCacheManager userCacheManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider, properties, onlineUserService, userCacheManager);
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
