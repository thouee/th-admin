package me.th.system.auth.component;

import me.th.share.common.ER;
import me.th.share.util.JacksonUtils;
import me.th.share.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String AUTH_EXCEPTION_MESSAGE = "UNAUTHORIZED";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 当用户尝试访问安全的 REST 资源而不提供任何凭据时，将调用此方法抛出 401 响应
        ER er = new ER(HttpServletResponse.SC_UNAUTHORIZED,
                authException == null ? AUTH_EXCEPTION_MESSAGE : authException.getLocalizedMessage());
        String json = JacksonUtils.toString(er);
        WebUtils.renderString(response, json);
    }
}
