package me.th.system.log.aspect;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.th.system.log.annotation.Logging;
import me.th.system.log.entity.Log;
import me.th.system.log.service.LogService;
import me.th.share.constant.Constant;
import me.th.share.util.RequestHolder;
import me.th.share.util.SecurityUtils;
import me.th.share.util.StringUtils;
import me.th.share.util.ThrowableUtils;
import me.th.share.util.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Aspect
public class LogAspect {

    private final LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("@annotation(me.th.system.log.annotation.Logging)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Log log = new Log();
        log.setLogType(Constant.LOG_TYPE_INFO);
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        log.setCostTime(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setResult(JSONUtil.toJsonStr(result).getBytes());
        saveLog(joinPoint, log);
        return result;
    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log();
        log.setLogType(Constant.LOG_TYPE_ERROR);
        log.setCostTime(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setResult(ThrowableUtils.getStackTrace(e).getBytes());
        saveLog(((ProceedingJoinPoint) joinPoint), log);
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Log log) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        log.setUrl(request.getRequestURI());
        log.setUsername(getUsername());
        log.setBrowser(WebUtils.getBrowser(request));
        log.setRequestIp(WebUtils.getIP(request));
        log.setAddress(WebUtils.getCityInfo(log.getRequestIp()));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Logging logging = method.getAnnotation(Logging.class);
        String packageName = joinPoint.getTarget().getClass().getName();
        String methodName = StringUtils.compressPackage(packageName) + "#" + signature.getName() + "()";
        log.setDescription(logging.value());
        log.setMethod(methodName);
        log.setParams(getParams(method, joinPoint.getArgs()));
        logService.save(log);
    }

    private String getParams(Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        Map<String, Object> map = new HashMap<>();
        if (args != null && parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                Object value = args[i];
                if (value instanceof HttpServletRequest || value instanceof HttpServletResponse
                        || value instanceof MultipartFile) {
                    continue;
                }
                map.put(parameterNames[i], value);
            }
        }
        if (!map.isEmpty()) {
            return JSONUtil.toJsonStr(map);
        }
        return "";
    }

    private String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return "";
        }
    }
}
