package me.th.share.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse {

    private Integer code;
    private String message;

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
