package me.th.share.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * 基本对象的属性名，默认使用属性名
     */
    String propName() default "";

    /**
     * 查询方式
     */
    Type type() default Type.EQUAL;

    /**
     * 连接查询的属性名
     */
    String joinName() default "";

    /**
     * 连接方式，默认左连接
     */
    Join join() default Join.LEFT;

    /**
     * 模糊查询，仅支持String类型
     */
    String[] blurry() default {};

    enum Type {
        /**
         * 等于
         */
        EQUAL,
        /**
         * 不等于
         */
        NOT_EQUAL,
        /**
         * 大于
         */
        GREATER_THAN,
        /**
         * 大于等于
         */
        GREATER_OR_EQUAL,
        /**
         * 小于
         */
        LESS_THAN,
        /**
         * 小于等于
         */
        LESS_OR_EQUAL,
        /**
         * 中模糊查询
         */
        INNER_LIKE,
        /**
         * 左模糊查询
         */
        LEFT_LIKE,
        /**
         * 右模糊查询
         */
        RIGHT_LIKE,
        /**
         * 包含
         */
        IN,
        /**
         * 不包含
         */
        NOT_IN,
        /**
         * 在...之间
         */
        BETWEEN,
        /**
         * 为空
         */
        IS_NULL,
        /**
         * 不为空
         */
        IS_NOT_NULL,
        ;
    }

    enum Join {
        LEFT,
        INNER,
        RIGHT,
        ;
    }
}
