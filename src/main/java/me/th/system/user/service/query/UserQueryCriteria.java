package me.th.system.user.service.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseQueryCriteria;
import me.th.share.query.Query;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserQueryCriteria extends BaseQueryCriteria {

    private static final long serialVersionUID = 4122607678390106151L;

    @Query(blurry = {"username"})
    private String username;

    @Query(blurry = {"nickname"})
    private String nickname;

    @Query(blurry = {"email"})
    private String email;

    @Query(blurry = {"phone"})
    private String phone;

    @Query
    private Boolean enabled;

    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> createdTime;
}
