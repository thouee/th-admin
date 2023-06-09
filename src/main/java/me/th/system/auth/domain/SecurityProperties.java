package me.th.system.auth.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class SecurityProperties {

    /**
     * Request Headers
     */
    private String header = "Authorization";
    /**
     * 令牌前缀，不需要加空格
     */
    private String prefix = "Bearer";
    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;
    /**
     * 令牌过期时间，单位毫秒
     */
    private Long expire = 1_800_000L;
    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    private String onlineKey = "ONLINE-TOKEN-";
    /**
     * 验证码
     */
    private String captchaKey = "CAPTCHA-";
    /**
     * token 续期检测
     */
    private Long detect = 300_000L;
    /**
     * 续期时间
     */
    private Long renew = 1_800_000L;

    public String getPrefix() {
        return StringUtils.trim(prefix) + " ";
    }
}
