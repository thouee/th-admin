package me.th.share.exception;

import lombok.Getter;

@Getter
public enum Checker implements BizExceptionCheck {
    SERVER_ERROR(2001, "服务器内部异常"),
    AUTH_EXPIRE(3001, "当前登录状态过期"),
    AUTH_NOT_FOUND(3002, "找不到当前登录的信息")
    ;

    private final Integer code;
    private final String message;

    Checker(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
