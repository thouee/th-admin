package me.th.share.exception;

import me.th.share.common.IResponseMode;

import java.text.MessageFormat;

public interface BizExceptionCheck extends IResponseMode, BizCheck {

    @Override
    default BaseException newException(Object... args) {
        String message = MessageFormat.format(this.getMessage(), args);
        return new BizException(this, args, message);
    }

    @Override
    default BaseException newException(Throwable cause, Object... args) {
        String message = MessageFormat.format(this.getMessage(), args);
        return new BizException(this, args, message, cause);
    }
}
