package me.th.system.log.service;

import me.th.share.common.PageData;
import me.th.system.log.entity.Log;
import me.th.system.log.service.dto.LogDto;
import me.th.system.log.service.query.LogQueryCriteria;
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

    /**
     * 查询
     *
     * @param criteria -
     * @return PageData<LogDto>
     */
    PageData<LogDto> queryAll(LogQueryCriteria criteria);

    /**
     * 删除
     *
     * @param id -
     */
    void delete(Long id);

    /**
     * 删除所有INFO
     */
    void deleteAllInfo();

    /**
     * 删除所有ERROR
     */
    void deleteAllError();
}
