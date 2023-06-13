package me.th.share.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("通用响应")
public abstract class BaseResponse {

    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("信息")
    private String message;

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
