package me.th.share.common;

import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseResponse;

/**
 * ErrorResponse
 */
@Getter
@Setter
public class ER extends BaseResponse {

    public ER(Integer code, String message) {
        super(code, message);
    }
}
