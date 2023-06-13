package me.th.system.user.rest;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import me.th.share.common.PageData;
import me.th.share.common.R;
import me.th.share.rest.AnonymousPostMapping;
import me.th.system.user.service.UserServiceImpl;
import me.th.system.user.service.dto.UserAddDto;
import me.th.system.user.service.dto.UserDto;
import me.th.system.user.service.dto.UserSignUpDto;
import me.th.system.user.service.dto.UserUpdatePasswordDto;
import me.th.system.user.service.query.UserQueryCriteria;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // 用户注册，由用户自己注册
    @AnonymousPostMapping("/signUp")
    public R<Void> signUp(@Validated @RequestBody UserSignUpDto userSignUp) {
        userService.create(userSignUp);
        return R.ok();
    }

    @GetMapping("/page")
    public R<PageData<UserDto>> queryUser(@RequestBody UserQueryCriteria criteria) {
        PageData<UserDto> PageData = userService.queryAll(criteria);
        return R.ok(PageData);
    }

    // 用户添加，由登录用户主动添加用户
    @PostMapping("/add")
    public R<Void> addUser(@Validated @RequestBody UserAddDto userAdd) {
        userService.create(userAdd);
        return R.ok();
    }

    @PostMapping("/update/password")
    public R<Void> updatePassword(@Validated @RequestBody UserUpdatePasswordDto userUpdatePassword) {
        userService.updatePassword(userUpdatePassword);
        return R.ok();
    }

    @DeleteMapping("/multiDelete")
    public R<Void> multiDeleteUser(@RequestBody List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            userService.delete(new HashSet<>(ids));
        }
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> deleteUser(@PathVariable @NotNull(message = "不可删除 id 为空的用户") Long id) {
        userService.delete(id);
        return R.ok();
    }
}
