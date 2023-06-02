package me.th.share.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import me.th.share.exception.Checker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

@Slf4j
public class SecurityUtils {

    /**
     * 获得登录用户信息
     *
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        UserDetailsService userDetailsService = SpringContextHolder.getBean(UserDetailsService.class);
        return userDetailsService.loadUserByUsername(getCurrentUsername());
    }

    /**
     * 获得登录用户用户名
     *
     * @return String
     */
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Checker.AUTH_EXPIRE_ERROR.isNull(authentication);

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }

        throw Checker.AUTH_NOT_FOUND.newException();
    }

    /**
     * 获得登录用户ID
     *
     * @return Long
     */
    public static Long getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        return JsonPath.read(userDetails, "$.data.user.id");
    }

    /**
     * 获取登录用户数据权限
     *
     * @return List<Long>
     */
    public static List<Long> getCurrentUserDataScope() {
        UserDetails userDetails = getCurrentUser();
        return JsonPath.read(userDetails, "$.data.dataScopes");
    }
}
