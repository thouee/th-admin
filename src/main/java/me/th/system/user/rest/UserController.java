package me.th.system.user.rest;

import lombok.RequiredArgsConstructor;
import me.th.share.common.R;
import me.th.system.user.service.UserServiceImpl;
import me.th.system.user.service.dto.UserSignUpDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/signUp")
    public R<Void> signUp(@Validated @RequestBody UserSignUpDto userSignUpDto) {
        userService.create(userSignUpDto);
        return R.ok();
    }
}
