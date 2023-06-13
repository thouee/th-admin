package me.th.share.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PageLink implements Serializable {

    private static final long serialVersionUID = 5261416618332109033L;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = 0;
}
