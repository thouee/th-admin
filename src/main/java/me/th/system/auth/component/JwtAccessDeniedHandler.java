package me.th.system.auth.component;

import me.th.share.common.ER;
import me.th.share.util.JacksonUtils;
import me.th.share.util.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 当用户在没有授权的情况下访问受保护的 REST 资源时，将调用此方法抛出 403 响应
        ER er = new ER(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        String json = JacksonUtils.toString(er);
        WebUtils.renderString(response, json);
    }
}
