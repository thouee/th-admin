package me.th.system.user.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class UserSignUpDto implements Serializable {

    private static final long serialVersionUID = -7684559668171713433L;

    @NotBlank(message = "用户名不可为空")
    private String username;

    @NotBlank(message = "密码不可为空")
    private String password;

    @NotBlank(message = "确认密码不可为空")
    private String checkPassword;
}
