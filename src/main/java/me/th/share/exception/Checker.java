package me.th.share.exception;

import lombok.Getter;

@Getter
public enum Checker implements BizExceptionCheck {
    SERVER_ERROR(6001, "服务器内部异常"),
    ;

    private final Integer code;
    private final String message;

    Checker(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
