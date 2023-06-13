package me.th.system.user.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "用户修改密码传输类")
public class UserUpdatePasswordDto implements Serializable {

    private static final long serialVersionUID = -8733274277279262844L;

    @ApiModelProperty(value = "原密码，不可为空", required = true)
    @NotBlank(message = "原密码不可为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码，不可为空", required = true)
    @NotBlank(message = "新密码不可为空")
    private String newPassword;

    @ApiModelProperty(value = "确认密码，不可为空", required = true)
    @NotBlank(message = "确认密码不可为空")
    private String checkPassword;
}
