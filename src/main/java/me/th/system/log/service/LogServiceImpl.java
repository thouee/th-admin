package me.th.system.log.service;

import me.th.system.log.entity.Log;
import me.th.system.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Log log) {
        logRepository.save(log);
    }
}
