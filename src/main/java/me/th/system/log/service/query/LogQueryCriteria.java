package me.th.system.log.service.query;

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
@ApiModel(value = "日志分页查询类")
public class LogQueryCriteria extends BaseQueryCriteria {

    private static final long serialVersionUID = -8258919490698000952L;

    @ApiModelProperty(value = "模糊搜索参数")
    @Query(blurry = {"username", "description", "address", "requestIp", "method", "params"})
    private String searchText;

    @ApiModelProperty(value = "用户名")
    @Query
    private String username;

    @ApiModelProperty(value = "日志类型")
    @Query
    private String logType;

    @ApiModelProperty(value = "记录时间")
    @Query(type = Query.Type.BETWEEN)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> createdTime;
}
