package me.th.system.log.service;

import lombok.RequiredArgsConstructor;
import me.th.share.common.PageData;
import me.th.share.constant.Constant;
import me.th.share.query.QueryHelper;
import me.th.share.util.PageUtils;
import me.th.system.log.entity.Log;
import me.th.system.log.repository.LogRepository;
import me.th.system.log.service.dto.LogDto;
import me.th.system.log.service.mapstruct.LogMapper;
import me.th.system.log.service.query.LogQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    private final LogMapper logMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Log log) {
        logRepository.save(log);
    }

    @Override
    public PageData<LogDto> queryAll(LogQueryCriteria criteria) {
        Pageable pageable = QueryHelper.getPageable(criteria);
        Page<Log> page = logRepository.findAll((root, query, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder), pageable);
        Page<LogDto> map = page.map(logMapper::toDto);
        return PageUtils.toPageData(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        logRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllInfo() {
        logRepository.deleteByLogType(Constant.LOG_TYPE_INFO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllError() {
        logRepository.deleteByLogType(Constant.LOG_TYPE_ERROR);
    }
}
