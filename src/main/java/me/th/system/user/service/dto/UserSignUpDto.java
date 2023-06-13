package me.th.system.user.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ApiModel("用户登记传输类")
public class UserSignUpDto implements Serializable {

    private static final long serialVersionUID = -7684559668171713433L;

    @ApiModelProperty(value = "用户名，不可为空", required = true)
    @NotBlank(message = "用户名不可为空")
    private String username;

    @ApiModelProperty(value = "密码，不可为空", required = true)
    @NotBlank(message = "密码不可为空")
    private String password;

    @ApiModelProperty(value = "确认密码，不可为空", required = true)
    @NotBlank(message = "确认密码不可为空")
    private String checkPassword;
}
