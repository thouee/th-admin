package me.th.system.security.domain;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.Getter;
import lombok.Setter;
import me.th.share.exception.Checker;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.text.MessageFormat;
import java.util.Objects;

@Getter
@Setter
public class LoginProperties {

    /**
     * 账号单用户登录
     */
    private boolean singleLogin = false;

    private LoginCode loginCode;

    public static final String cacheKey = "USER-LOGIN-CODE";
    private static final String[] OPERATOR = {"+", "-", "×"};

    /**
     * 生成验证码
     *
     * @return Captcha
     */
    public Captcha getCaptcha() {
        return doGetCaptcha(Objects.isNull(loginCode) ? new LoginCode() : loginCode);
    }

    private Captcha doGetCaptcha(LoginCode loginCode) {
        Captcha captcha = null;
        switch (loginCode.getCodeType()) {
            case ARITHMETIC:
                captcha = new FixedArithmeticCaptcha();
                break;
            case CHINESE:
                captcha = new ChineseCaptcha();
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha();
                break;
            case GIF:
                captcha = new GifCaptcha();
                break;
            case SPEC:
                captcha = new SpecCaptcha();
                break;
            default:
                throw Checker.CONFIGURATION_ERROR.newException("验证码配置错误，暂不支持该类型：" + loginCode.getCodeType());
        }

        captcha.setWidth(loginCode.getWidth());
        captcha.setHeight(loginCode.getHeight());
        captcha.setLen(loginCode.getLength());
        if (StringUtils.isNotBlank(loginCode.getFontName())) {
            captcha.setFont(new Font(loginCode.getFontName(), Font.PLAIN, loginCode.getFontSize()));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {

        public FixedArithmeticCaptcha() {
        }

        @Override
        protected char[] alphas() {
            int var0 = num(1, 10), var1 = num(1, 10), var2 = num(3);
            int var3 = new int[]{var0 + var1, var1 - var2, var0 * var1}[var2];
            String var4 = OPERATOR[var2];
            this.setArithmeticString(MessageFormat.format("{0}{1}{2}", var0, var4, var1));
            this.chars = String.valueOf(var3);
            return chars.toCharArray();
        }
    }
}
