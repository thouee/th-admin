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

    private String nickName;

    @NotBlank(message = "密码不可为空")
    private String password;

    @NotBlank(message = "手机号不可为空")
    private String phone;

    @NotBlank(message = "邮箱不可为空")
    private String email;
}
