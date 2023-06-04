package me.th.share.serializer.datamask;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DataMaskingSerializer.class)
public @interface DataMasking {

    /**
     * 加密方式
     */
    MaskingFunMode maskFun();

    /**
     * 是否展示明文
     */
    boolean showOrigin() default false;

    // ==================== 以下属性只在 maskFun = MaskFunMode.CUSTOM 时才起作用 ====================

    /**
     * 左侧需保留明文位数
     */
    int prefixNoMaskLen() default 0;

    /**
     * 右侧需保留明文位数
     */
    int suffixNoMaskLen() default 0;

    /**
     * 使用什么字符进行加密
     */
    String symbol() default "*";
}
