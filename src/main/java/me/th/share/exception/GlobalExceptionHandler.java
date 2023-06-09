package me.th.share.exception;

import lombok.extern.slf4j.Slf4j;
import me.th.share.common.CommonResponseMode;
import me.th.share.common.ER;
import me.th.share.util.ThrowableUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ENV_PROD = "prod";
    private final MessageSource messageSource;
    private final Boolean isProd;
    private static final String PACKAGE_PREFIX = "me.th";

    public GlobalExceptionHandler(@Value("${spring.profiles.active:dev}") String activeProfile,
                                  MessageSource messageSource) {
        this.messageSource = messageSource;
        this.isProd = new HashSet<>(Arrays.asList(activeProfile.split(","))).contains(ENV_PROD);
    }

    @ExceptionHandler({
            // 缺少 servlet 请求参数异常
            MissingServletRequestParameterException.class,
            // Servlet 请求绑定异常
            ServletRequestBindingException.class,
            // 类型不匹配异常
            TypeMismatchException.class,
            // 消息无法检索异常
            HttpMessageNotReadableException.class,
            // 缺少 servlet 请求部分异常
            MissingServletRequestPartException.class
    })
    public ER badRequestException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.BAD_REQUEST.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }


    @ExceptionHandler({
            // 没有发现处理程序异常
            NoHandlerFoundException.class
    })
    public ER noHandlerFoundException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.NOT_FOUND.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 不支持的 Http 请求方法异常
            HttpRequestMethodNotSupportedException.class
    })
    public ER httpRequestMethodNotSupportedException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.METHOD_NOT_ALLOWED.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 不接受的 Http 媒体类型异常
            HttpMediaTypeNotAcceptableException.class
    })
    public ER httpMediaTypeNotAcceptableException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.NOT_ACCEPTABLE.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 不支持的 Http 媒体类型异常
            HttpMediaTypeNotSupportedException.class
    })
    public ER httpMediaTypeNotSupportedException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.UNSUPPORTED_MEDIA_TYPE.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 异步请求超时异常
            AsyncRequestTimeoutException.class
    })
    public ER asyncRequestTimeoutException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.SERVICE_UNAVAILABLE.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 请求路径参数缺失异常
            MissingPathVariableException.class,
            // Http 消息不可写异常
            HttpMessageNotWritableException.class,
            // 不支持转换异常
            ConversionNotSupportedException.class
    })
    public ER handleServletException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        Integer code = CommonResponseMode.INTERNAL_SERVER_ERROR.getCode();
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 参数绑定异常
            BindException.class
    })
    public ER handleBindException(BindException e, WebRequest request) {
        log.error("参数绑定异常", e);
        return wrapperBindingResult(e.getBindingResult(), request);
    }

    @ExceptionHandler({
            // 方法参数无效异常
            MethodArgumentNotValidException.class
    })
    public ER handleValidException(MethodArgumentNotValidException e, WebRequest request) {
        log.error("参数校验异常", e);
        return wrapperBindingResult(e.getBindingResult(), request);
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 参数校验结果
     * @param request       -
     * @return ER
     */
    private ER wrapperBindingResult(BindingResult bindingResult, WebRequest request) {
        final List<String> errorMessage = new ArrayList<>();
        for (ObjectError aError : bindingResult.getAllErrors()) {
            final StringBuilder msg = new StringBuilder();
            if (aError instanceof FieldError) {
                msg.append(((FieldError) aError).getField().equals("; "));
            }
            msg.append(aError.getDefaultMessage() == null ? "" : aError.getDefaultMessage());
            errorMessage.add(msg.toString());
        }
        final String desc = isProd ? getLocaleMessage(CommonResponseMode.BAD_REQUEST.getCode(), "") :
                String.join("，", errorMessage);
        return new ER(CommonResponseMode.BAD_REQUEST.getCode(), desc);
    }

    @ExceptionHandler({
            // security 认证失败
            BadCredentialsException.class
    })
    public ER handleBadCredentialsException(BadCredentialsException e) {
        Integer code = Checker.USERNAME_OR_PASSWORD_INCORRECT.getCode();
        log.error(ThrowableUtils.getStackTraceByPrefix(e, PACKAGE_PREFIX));
        return new ER(code, getLocaleMessage(code, e.getMessage()));
    }

    @ExceptionHandler({
            // 业务异常
            BizException.class
    })
    public ER handleBizException(BaseException e) {
        String localeMessage = getLocaleMessage(e);
        log.error("业务异常 {}", ThrowableUtils.getStackTraceByPrefix(e, PACKAGE_PREFIX));
        return new ER(e.getAMode().getCode(), localeMessage);
    }

    @ExceptionHandler({
            // 自定义非业务异常
            BaseException.class
    })
    public ER handleBaseException(BizException e) {
        String localeMessage = getLocaleMessage(e);
        log.error(localeMessage, e);
        return new ER(e.getAMode().getCode(), localeMessage);
    }

    @ExceptionHandler({
            // 未定义异常
            Exception.class
    })
    public ER handleExceptionInternal(Exception e, WebRequest request) {
        log.error("未捕捉异常：" + e.getMessage(), e);
        Integer code = Checker.SERVER_ERROR.getCode();
        return new ER(code, getLocaleMessage(code, Checker.SERVER_ERROR.getMessage()));
    }

    private String getLocaleMessage(BaseException e) {
        return getLocaleMessage(e.getAMode().getCode(), e.getArgs(), e.getMessage());
    }

    private String getLocaleMessage(Integer code, String defaultMessage) {
        return getLocaleMessage(code, null, defaultMessage);
    }

    /**
     * 获取本地化异常消息
     *
     * @param code           -
     * @param args           -
     * @param defaultMessage -
     * @return String
     */
    private String getLocaleMessage(Integer code, Object[] args, String defaultMessage) {
        try {
            return messageSource.getMessage("response.error" + code, args, defaultMessage,
                    LocaleContextHolder.getLocale());
        } catch (Exception ex) {
            log.warn("本地化异常消息发生异常：response.{}", code);
            return defaultMessage;
        }
    }
}
