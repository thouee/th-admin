package me.th.share.jpa;

import me.th.share.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 审计
 */
@Component("auditorAware")
public class AuditorConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of(SecurityUtils.getCurrentUsername());
        } catch (Exception ignore) {
        }
        // 用户定时任务或无 Token 调用
        return Optional.of("System");
    }
}
