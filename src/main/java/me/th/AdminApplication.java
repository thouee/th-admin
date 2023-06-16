package me.th;

import cn.hutool.core.collection.CollectionUtil;
import com.ulisesbocchio.jasyptspringboot.wrapper.EncryptableMapPropertySourceWrapper;
import lombok.extern.slf4j.Slf4j;
import me.th.share.util.RedisUtils;
import me.th.share.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AdminApplication extends SpringBootServletInitializer implements CommandLineRunner {

    private static final Map<String, String> PROGRAM_ARGUMENTS = new ConcurrentHashMap<>();

    @Resource
    private ConfigurableEnvironment environment;

    public static void main(String[] args) {
        initProgramArguments();
        SpringApplication.run(AdminApplication.class, updateArguments(args));
        log.info("启动成功...");
    }

    private static void initProgramArguments() {
        // 生产环境不要写在代码中，请以启动参数的形式添加在启动脚本里，这里只是为了开发和测试方便
        PROGRAM_ARGUMENTS.put("--jasypt.encryptor.password", "wL-2hQBzQqvGiruu");
    }

    private static String[] updateArguments(String[] args) {
        List<String> list = CollectionUtil.newArrayList(args);
        for (Map.Entry<String, String> entry : PROGRAM_ARGUMENTS.entrySet()) {
            if (list.stream().noneMatch(arg -> arg.startsWith(entry.getKey()))) {
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        return list.toArray(new String[0]);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("====================开始输出项目配置====================");
        environment.getPropertySources()
                .stream()
                // 从 OriginTrackedMapPropertySource 修改为 EncryptableMapPropertySourceWrapper，适应 jasypt
                .filter(ps -> ps instanceof EncryptableMapPropertySourceWrapper)
                .map(ps -> ((EncryptableMapPropertySourceWrapper) ps))
                .forEach(ms -> {
                    String name = ms.getName();
                    if (StringUtils.containsAny(name, "application.yml", "application-dev.yml",
                            "application-test.yml")) {
                        Arrays.stream(ms.getPropertyNames()).forEach(s -> {
                            if (!s.equalsIgnoreCase("spring.profiles.active")) {
                                log.info(s + "\t\t" + environment.getProperty(s));
                            }
                        });
                    }
                });
        log.info("====================输出项目配置完成====================");
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public RedisUtils redisUtils(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisUtils(redisTemplate);
    }
}
