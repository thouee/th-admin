package me.th.system.user.rest;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.th.share.common.PageData;
import me.th.share.common.R;
import me.th.share.rest.AnonymousPostMapping;
import me.th.system.log.annotation.Logging;
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

@Api(tags = "系统：用户模块")
@ApiSupport(author = "thou")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @Logging("用户注册")
    @ApiOperation(value = "用户注册")
    @ApiOperationSupport(author = "thou")
    @AnonymousPostMapping("/signUp")
    public R<Void> signUp(@Validated @RequestBody UserSignUpDto userSignUp) {
        userService.create(userSignUp);
        return R.ok();
    }

    @ApiOperation(value = "用户数据分页")
    @GetMapping("/page")
    public R<PageData<UserDto>> queryUser(@RequestBody UserQueryCriteria criteria) {
        PageData<UserDto> PageData = userService.queryAll(criteria);
        return R.ok(PageData);
    }

    @Logging("用户添加")
    @ApiOperation(value = "用户添加")
    @PostMapping("/add")
    public R<Void> addUser(@Validated @RequestBody UserAddDto userAdd) {
        userService.create(userAdd);
        return R.ok();
    }

    @Logging("修改密码")
    @ApiOperation(value = "修改密码")
    @PostMapping("/update/password")
    public R<Void> updatePassword(@Validated @RequestBody UserUpdatePasswordDto userUpdatePassword) {
        userService.updatePassword(userUpdatePassword);
        return R.ok();
    }

    @Logging("用户删除")
    @ApiOperation(value = "批量删除")
    @DeleteMapping("/multiDelete")
    public R<Void> multiDeleteUser(@RequestBody List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            userService.delete(new HashSet<>(ids));
        }
        return R.ok();
    }

    @Logging("用户删除")
    @ApiOperation(value = "用户删除")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteUser(@PathVariable @NotNull(message = "不可删除 id 为空的用户") Long id) {
        userService.delete(id);
        return R.ok();
    }
}
