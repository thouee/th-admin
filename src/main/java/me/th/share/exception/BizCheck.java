package me.th.share.exception;


/**
 * 业务检查，为真抛出异常
 */
public interface BizCheck {

    BaseException newException(Object... args);

    BaseException newException(Throwable cause, Object... args);

    default void isTrue(boolean expression, Object... args) {
        if (expression) {
            throw newException(args);
        }
    }

    default void isFalse(boolean expression, Object... args) {
        isTrue(!expression, args);
    }

    default void notNull(Object object, Object... args) {
        isTrue(object != null, args);
    }

    default void isNull(Object object, Object... args) {
        isTrue(object == null, args);
    }
}
