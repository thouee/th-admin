package me.th.share.serializer.datamask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataMasking {

    /**
     * 加密方式
     */
    MaskingMode mode();

    /**
     * 加密用的字符，默认为 `*`，只在 mode 为 CUSTOM 时起作用，对其他 mode 无效
     */
    String mask() default "*";
}
