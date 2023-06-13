package me.th.system.auth.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel(value = "用户登录传输类")
public class AuthUserDto {

    @ApiModelProperty(value = "用户名，不能为空", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码，不能为空", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码，不能为空", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "验证码对应的 uuid")
    private String uuid = "";
}
