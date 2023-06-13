package me.th.system.user.service;

import me.th.share.common.PageData;
import me.th.system.user.entity.User;
import me.th.system.user.service.dto.UserAddDto;
import me.th.system.user.service.dto.UserDto;
import me.th.system.user.service.dto.UserLoginDto;
import me.th.system.user.service.dto.UserSignUpDto;
import me.th.system.user.service.dto.UserUpdatePasswordDto;
import me.th.system.user.service.query.UserQueryCriteria;

import java.util.Set;

public interface UserService {

    PageData<UserDto> queryAll(UserQueryCriteria criteria);

    UserDto findById(long id);

    void create(UserSignUpDto userSignUp);

    void create(UserAddDto userAdd);

    void update(User entity);

    void updatePassword(UserUpdatePasswordDto userUpdatePassword);

    void delete(Set<Long> ids);

    void delete(long id);

    UserLoginDto getLoginData(String username);
}
