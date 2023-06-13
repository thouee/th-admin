package me.th.system.user.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class UserUpdatePasswordDto implements Serializable {

    private static final long serialVersionUID = -8733274277279262844L;

    @NotBlank(message = "原密码不可为空")
    private String oldPassword;

    @NotBlank(message = "新密码不可为空")
    private String newPassword;

    @NotBlank(message = "确认密码不可为空")
    private String checkPassword;
}
