package me.th.system.auth.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import me.th.share.rest.AnonymousAccess;
import me.th.system.auth.enums.RequestMethodMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static me.th.system.auth.enums.RequestMethodMode.ALL;
import static me.th.system.auth.enums.RequestMethodMode.DELETE;
import static me.th.system.auth.enums.RequestMethodMode.GET;
import static me.th.system.auth.enums.RequestMethodMode.POST;
import static me.th.system.auth.enums.RequestMethodMode.PUT;
import static me.th.system.auth.enums.RequestMethodMode.find;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AccessDeniedHandler jwtAccessDeniedHandler;
    private final ApplicationContext applicationContext;
    private final TokenConfigurer tokenConfigurer;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 搜索匿名访问接口的访问地址
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Map<RequestMethodMode, Set<String>> anonymous = fetchAnonymousUrl(handlerMethods);
        httpSecurity
                // 禁用 CSRF
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 授权异常
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // 防止 iframe 造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()
                // 不创建会话
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 放行静态资源
                .antMatchers(HttpMethod.GET,
                        "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/webSocket/**")
                .permitAll()
                // 放行 Swagger 文档
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/doc.html", "/favicon.ico")
                .permitAll()
                // 放行文件
                .antMatchers("/avatar/**", "/file/**").permitAll()
                // 放行 druid 相关
                .antMatchers("/druid/**").permitAll()
                // 放行 options 类型请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 放行自定义匿名访问接口
                .antMatchers(HttpMethod.GET, anonymous.get(GET).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST, anonymous.get(POST).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.PUT, anonymous.get(PUT).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.DELETE, anonymous.get(DELETE).toArray(new String[0])).permitAll()
                .antMatchers(anonymous.get(ALL).toArray(new String[0])).permitAll()
                // 余下所有请求都需要认证
                .anyRequest().authenticated()
                .and().apply(tokenConfigurer);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    /**
     * 搜索所有的自定义的匿名访问接口
     *
     * @param handlerMethodMap -
     * @return Map<RequestMethodMode, Set < String>>
     */
    private Map<RequestMethodMode, Set<String>> fetchAnonymousUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod method = entry.getValue();
            AnonymousAccess anonymousAccess = method.getMethodAnnotation(AnonymousAccess.class);
            if (anonymousAccess != null) {
                List<RequestMethod> methods = CollectionUtil.newArrayList(entry.getKey().getMethodsCondition().getMethods());
                RequestMethodMode requestMethodMode = find(CollectionUtil.isEmpty(methods) ?
                        ALL.getType() : methods.get(0).name());
                Set<String> patterns = Objects.requireNonNull(entry.getKey().getPatternsCondition()).getPatterns();
                switch (Objects.requireNonNull(requestMethodMode)) {
                    case GET:
                        get.addAll(patterns);
                        break;
                    case POST:
                        post.addAll(patterns);
                        break;
                    case PUT:
                        put.addAll(patterns);
                        break;
                    case DELETE:
                        delete.addAll(patterns);
                        break;
                    default:
                        all.addAll(patterns);
                        break;
                }
            }
        }
        Map<RequestMethodMode, Set<String>> anonymous = MapUtil.newHashMap(8);
        anonymous.put(GET, get);
        anonymous.put(POST, post);
        anonymous.put(PUT, put);
        anonymous.put(DELETE, delete);
        anonymous.put(ALL, all);
        return anonymous;
    }
}
