package me.th.share.exception;

import lombok.Getter;

@Getter
public enum Checker implements BizExceptionCheck {
    SERVER_ERROR(2001, "服务器内部异常"),
    CONFIGURATION_ERROR(2002, "{0}"), // 配置错误，{0} 为自定义异常信息
    ENTITY_NOT_FOUND_ERROR(2003, "{0}不存在"), // 实体找不到错误，{0} 为传入实体名
    ENTITY_EXIST_ERROR(2004, "{0}"), // 实体已存在错误
    AUTH_EXPIRE_ERROR(3001, "当前登录状态过期"),
    AUTH_NOT_FOUND(3002, "找不到当前登录的信息"),
    CAPTCHA_NOT_FOUND(3003, "验证码已过期"),
    CAPTCHA_INCORRECT(3004, "验证码不正确"),
    USERNAME_OR_PASSWORD_INCORRECT(3005, "用户名或密码错误"),
    USER_NOT_ENABLED(3006, "用户被禁用"),
    TWICE_PASSWORD_NOT_EQUAL(3007, "两次密码不相同"),
    PASSWORD_NOT_INCORRECT(3008, "原密码错误"),
    ;

    private final Integer code;
    private final String message;

    Checker(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
