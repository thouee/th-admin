package me.th;

import lombok.extern.slf4j.Slf4j;
import me.th.share.util.RedisUtils;
import me.th.share.util.SpringContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AdminApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        log.info("启动成功...");
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public RedisUtils redisUtils(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisUtils(redisTemplate);
    }

    @Resource
    private ConfigurableEnvironment environment;

    @Override
    public void run(String... args) throws Exception {
        log.info("====================开始输出项目配置====================");
        environment.getPropertySources()
                .stream()
                .filter(ps -> ps instanceof OriginTrackedMapPropertySource)
                .map(ps -> ((OriginTrackedMapPropertySource) ps))
                .forEach(ms -> Arrays.stream(ms.getPropertyNames()).forEach(s -> {
                    if (!s.equalsIgnoreCase("spring.profiles.active")) {
                        log.info(s + "\t\t" + environment.getProperty(s));
                    }
                }));
        log.info("====================输出项目配置完成====================");
    }
}
