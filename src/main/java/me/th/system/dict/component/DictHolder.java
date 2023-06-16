package me.th.system.dict.component;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import me.th.share.util.RedisUtils;
import me.th.system.dict.service.DictService;
import me.th.system.dict.service.dto.DictCacheDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DictHolder {

    private final RedisUtils redisUtils;
    private final DictService dictService;

    /**
     * 过期时间，单位毫秒，折算2小时
     */
    private final long EXPIRE_TIME = 7_200_000L;
    /**
     * 过期检测阈值，单位毫秒，折算半小时
     */
    private final long DETECT_TIME = 1_800_000L;
    /**
     * 续期时间，单位毫秒，折算1小时
     */
    private final long RENEW_TIME = 3_600_000L;
    private final String DICT_CACHE_KEY = "DICT-KEY-{0}-VALUE-{1}";

    public String getDictName(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return null;
        }
        String cacheKey = getCacheKey(key, value);
        Object name = redisUtils.hGet(cacheKey);
        // 有缓存
        if (name != null) {
            // 续期检测
            checkRenew(cacheKey);
            return ((String) name);
        }
        DictCacheDto dict4Cache = dictService.getDict4Cache(key, value);
        redisUtils.set(cacheKey, "", EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return ((String) name);
    }

    private String getCacheKey(String key, String value) {
        return MessageFormat.format(DICT_CACHE_KEY, key, value);
    }

    /**
     * 续期检测
     *
     * @param key -
     */
    private void checkRenew(String key) {
        long time = redisUtils.getExpire(key, TimeUnit.MILLISECONDS);
        DateTime expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        long differ = expireDate.getTime() - System.currentTimeMillis();
        if (differ <= DETECT_TIME) {
            long renew = time + RENEW_TIME;
            redisUtils.expire(key, renew, TimeUnit.MILLISECONDS);
        }
    }

    // private void add
}
