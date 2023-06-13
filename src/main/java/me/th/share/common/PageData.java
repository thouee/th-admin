package me.th.share.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@ApiModel(value = "分页数据返回结构")
public class PageData<T> {

    @ApiModelProperty(value = "记录总数")
    private Long total;
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;
    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
    @ApiModelProperty(value = "记录")
    private Collection<T> records;
}
