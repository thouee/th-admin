package me.th.system.user.service.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseQueryCriteria;
import me.th.share.query.Query;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "用户分页查询类")
public class UserQueryCriteria extends BaseQueryCriteria {

    private static final long serialVersionUID = 4122607678390106151L;

    @ApiModelProperty(value = "用户名")
    @Query(blurry = {"username"})
    private String username;

    @ApiModelProperty(value = "昵称")
    @Query(blurry = {"nickname"})
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    @Query(blurry = {"email"})
    private String email;

    @ApiModelProperty(value = "手机号")
    @Query(blurry = {"phone"})
    private String phone;

    @ApiModelProperty(value = "是否启用")
    @Query
    private Boolean enabled;

    @ApiModelProperty(value = "注册时间")
    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> createdTime;
}
