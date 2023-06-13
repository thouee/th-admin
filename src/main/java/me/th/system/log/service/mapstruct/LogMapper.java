package me.th.system.log.service.mapstruct;

import me.th.share.base.BaseMapper;
import me.th.system.log.entity.Log;
import me.th.system.log.service.dto.LogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogMapper extends BaseMapper<Log, LogDto> {
}
