package me.th.system.log.service;

import me.th.system.log.entity.Log;
import org.springframework.scheduling.annotation.Async;

public interface LogService {

    /**
     * 保存日志数据
     *
     * @param joinPoint -
     * @param log       -
     */
    @Async
    void save(Log log);
}
