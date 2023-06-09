package me.th.share.common;

import lombok.Getter;
import me.th.share.common.IResponseMode;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Getter
public enum CommonResponseMode implements IResponseMode {
    SUCCESS(HttpStatus.OK.value(), "成功"), // 200
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "错误的请求"), // 400
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "未找到"), // 404
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "不允许的方法"), // 405
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE.value(), "不可接受"), // 406
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT.value(), "请求超时"), // 408
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "不支持的媒体类型"), // 415
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器错误"), // 500
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE.value(), "服务不可用"), // 503
    ;

    private final Integer code;
    private final String message;

    CommonResponseMode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
