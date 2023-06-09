package me.th.system.user.service.mapstruct;

import me.th.share.base.BaseMapper;
import me.th.system.user.entity.User;
import me.th.system.user.service.dto.UserSignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSignUpMapper extends BaseMapper<User, UserSignUpDto> {
}
