package me.th.system.user.service;

import me.th.system.user.entity.User;
import me.th.system.user.service.dto.UserDto;
import me.th.system.user.service.dto.UserLoginDto;

import java.util.Set;

public interface UserService {

    UserDto findById(long id);

    void create(User entity);

    void update(User entity);

    void delete(Set<Long> ids);

    void delete(long id);

    UserLoginDto getLoginData(String username);
}
