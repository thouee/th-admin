package me.th.share.exception;

import me.th.share.common.IResponseMode;

public class BizException extends BaseException {

    private static final long serialVersionUID = 3254424972208895146L;

    public BizException(IResponseMode aMode, Object[] args, String message) {
        super(aMode, args, message);
    }

    public BizException(IResponseMode aMode, Object[] args, String message, Throwable cause) {
        super(aMode, args, message, cause);
    }
}
