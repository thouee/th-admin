package me.th.system.user.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseDTO;

import java.util.Date;

@Getter
@Setter
public class UserDto extends BaseDTO {

    private static final long serialVersionUID = 3092969636900532392L;

    private Long id;

    private String username;

    private String nickName;

    private String email;

    private Integer gender;

    private String avatarName;

    private String avatarPath;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin;

    private Date pwdResetTime;
}
