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

    /**
     * 查询
     *
     * @param criteria -
     * @return PageData<UserDto>
     */
    PageData<UserDto> queryAll(UserQueryCriteria criteria);

    /**
     * 查询
     *
     * @param id -
     * @return UserDto
     */
    UserDto findById(long id);

    /**
     * 创建
     *
     * @param userSignUp -
     */
    void create(UserSignUpDto userSignUp);

    /**
     * 创建
     *
     * @param userAdd -
     */
    void create(UserAddDto userAdd);

    /**
     * 更新
     *
     * @param entity -
     */
    void update(User entity);

    /**
     * 更新密码
     *
     * @param userUpdatePassword -
     */
    void updatePassword(UserUpdatePasswordDto userUpdatePassword);

    /**
     * 批量删除
     *
     * @param ids -
     */
    void delete(Set<Long> ids);

    /**
     * 删除
     *
     * @param id -
     */
    void delete(long id);

    /**
     * 查询登录用户信息
     *
     * @param username -
     * @return UserLoginDto
     */
    UserLoginDto getLoginData(String username);
}
