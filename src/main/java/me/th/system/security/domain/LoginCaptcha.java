package me.th.system.security.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCaptcha {

    /**
     * 验证码类型
     */
    private LoginCaptchaMode captchaType = LoginCaptchaMode.ARITHMETIC;
    /**
     * 验证码有效期，单位分钟
     */
    private Long expire = 2L;
    /**
     * 验证码内容长度
     */
    private int length = 2;
    /**
     * 验证码宽度
     */
    private int width = 100;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private int fontSize = 25;
}
