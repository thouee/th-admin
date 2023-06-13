package me.th.system.auth.component;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import me.th.system.auth.domain.SecurityProperties;
import me.th.system.auth.service.OnlineUserService;
import me.th.system.auth.service.UserCacheManager;
import me.th.system.auth.service.dto.OnlineUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final UserCacheManager userCacheManager;

    public TokenFilter(TokenProvider tokenProvider, SecurityProperties properties,
                       OnlineUserService onlineUserService, UserCacheManager userCacheManager) {
        this.tokenProvider = tokenProvider;
        this.properties = properties;
        this.onlineUserService = onlineUserService;
        this.userCacheManager = userCacheManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = tokenProvider.getToken(httpServletRequest);
        // if Token 存在
        if (StringUtils.isNotBlank(token)) {
            OnlineUserDto onlineUser = null;
            boolean cleanUserCache = false;
            try {
                onlineUser = onlineUserService.getOne(properties.getOnlineKey() + token);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || onlineUser == null) {
                    userCacheManager.cleanUserCache(String.valueOf(tokenProvider.getClaims(token)
                            .get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            if (onlineUser != null) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // token 续期
                tokenProvider.checkRenew(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
