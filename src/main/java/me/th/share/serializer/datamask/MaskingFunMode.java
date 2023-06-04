package me.th.share.serializer.datamask;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public enum MaskingFunMode {
    /**
     * 身份证
     */
    ID_CARD(s -> {
        if (StringUtils.isEmpty(s)) return s;
        return s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
    }),
    /**
     * 中文名
     */
    CHINESE_NAME(s -> {
        if (StringUtils.isEmpty(s)) return s;
        return s.replaceAll("(\\S)\\S*", "$1**");
    }),
    /**
     * 手机号
     */
    PHONE(s -> {
        if (StringUtils.isEmpty(s)) return s;
        return s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }),
    /**
     * 自定义
     */
    CUSTOM(null), // 自定义
    ;

    private final Function<String, String> function;

    MaskingFunMode(Function<String, String> function) {
        this.function = function;
    }

    public Function<String, String> function() {
        return this.function;
    }
}
