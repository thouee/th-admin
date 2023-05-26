package me.th.share.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    @Override
    public void destroy() throws Exception {
        clearHolder();
    }

    private static void clearHolder() {
        log.debug("清除 SpringContextHolder 中的 applicationContext：{}", applicationContext);
        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextHolder.applicationContext != null) {
            log.warn("SpringContextHolder 中的 applicationContext 被覆盖，原有 ApplicationContext 为 [{}]， 被覆盖为 [{}]",
                    SpringContextHolder.applicationContext, applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 检测 applicationContext 是否已被注入
     */
    private static void assertContextInjected() {
        Objects.requireNonNull(applicationContext, "applicationContext 属性未被注入，" +
                "请在 Spring 容器中注册 SpringContextHolder");
    }

    /**
     * 从 applicationContext 中 根据名称获取 bean
     */
    public static Object getBean(String name) {
        assertContextInjected();
        return applicationContext.getBean(name);
    }

    /**
     * 从 applicationContext 中根据类型获取 bean
     */
    public static <T> T getBean(Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(clazz);
    }
}
