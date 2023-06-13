package me.th.system.user.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ApiModel("用户添加传输类")
public class UserAddDto implements Serializable {

    private static final long serialVersionUID = 8385225374470000097L;

    @ApiModelProperty(value = "用户名，不可为空", required = true)
    @NotBlank(message = "用户名不可为空")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "密码，不可为空", required = true)
    @NotBlank(message = "密码不可为空")
    private String password;

    @ApiModelProperty(value = "手机号，不可为空", required = true)
    @NotBlank(message = "手机号不可为空")
    private String phone;

    @ApiModelProperty(value = "邮箱，不可为空", required = true)
    @NotBlank(message = "邮箱不可为空")
    private String email;
}
