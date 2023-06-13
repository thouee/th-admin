package me.th.share.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseResponse;

/**
 * CommonResponse
 */
@Getter
@Setter
@ApiModel
public class R<T> extends BaseResponse {

    @ApiModelProperty("数据")
    private T data;

    protected R() {
        super(CommonResponseMode.SUCCESS.getCode(), CommonResponseMode.SUCCESS.getMessage());
    }

    protected R(T data) {
        super(CommonResponseMode.SUCCESS.getCode(), CommonResponseMode.SUCCESS.getMessage());
        this.data = data;
    }

    public static <T> R<T> ok(T data) {
        return new R<>(data);
    }

    public static R<Void> ok() {
        return new R<>();
    }
}
