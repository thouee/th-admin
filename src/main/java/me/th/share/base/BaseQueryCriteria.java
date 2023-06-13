package me.th.share.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.th.share.query.PageLink;
import me.th.share.query.QueryOrder;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "基础分页查询类")
public class BaseQueryCriteria implements Serializable {

    private static final long serialVersionUID = -2183492679004157722L;

    @ApiModelProperty(value = "排序条件")
    private QueryOrder queryOrder;

    @ApiModelProperty(value = "分页条件")
    private PageLink pageLink;
}
