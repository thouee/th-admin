package me.th.system.auth.config;

import me.th.system.auth.domain.LoginProperties;
import me.th.system.auth.domain.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeanConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "login")
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwt")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
