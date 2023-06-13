package me.th.system.user.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseDTO;

import java.util.Date;

@Getter
@Setter
@ApiModel(value = "用户传输类")
public class UserDto extends BaseDTO {

    private static final long serialVersionUID = 3092969636900532392L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "头像地址")
    private String avatarName;

    @ApiModelProperty(value = "头像真实路径")
    private String avatarPath;

    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin;

    @ApiModelProperty(value = "密码修改时间")
    private Date pwdResetTime;
}
