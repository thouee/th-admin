package me.th.share.exception;

import lombok.Getter;
import me.th.share.common.IResponseMode;

@Getter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -5746341429859031791L;
    private final IResponseMode aMode;
    /**
     * 打印参数
     */
    private final Object[] args;
    /**
     * 异常信息
     */
    private final String message;
    /**
     * 堆栈信息
     */
    private final Throwable cause;

    public BaseException(IResponseMode aMode) {
        this(aMode, null, aMode.getMessage(), null);
    }

    public BaseException(IResponseMode aMode, String message) {
        this(aMode, null, message, null);
    }

    public BaseException(IResponseMode aMode, Object[] args, String message) {
        this(aMode, args, message, null);
    }

    public BaseException(IResponseMode aMode, Object[] args, String message, Throwable cause) {
        this.aMode = aMode;
        this.args = args;
        this.message = message;
        this.cause = cause;
    }
}
