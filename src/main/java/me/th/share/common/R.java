package me.th.share.common;

import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseResponse;

/**
 * CommonResponse
 */
@Getter
@Setter
public class R<T> extends BaseResponse {

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
