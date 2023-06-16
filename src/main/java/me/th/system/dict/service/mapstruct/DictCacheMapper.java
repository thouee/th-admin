package me.th.system.dict.service.mapstruct;

import me.th.share.base.BaseMapper;
import me.th.system.dict.entity.Dict;
import me.th.system.dict.service.dto.DictCacheDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictCacheMapper extends BaseMapper<Dict, DictCacheDto> {
}
