package me.th.share.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtils {

    /**
     * 获取堆栈信息
     *
     * @param throwable -
     * @return String
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 获取指定包名前缀的异常堆栈信息
     *
     * @param throwable     -
     * @param packagePrefix 包名前缀
     * @return String
     */
    public static String getStackTraceByPrefix(Throwable throwable, String packagePrefix) {
        StringBuilder s = new StringBuilder("\n").append(throwable);
        for (StackTraceElement traceElement : throwable.getStackTrace()) {
            if (!traceElement.getClassName().startsWith(packagePrefix)) {
                break;
            }
            s.append("\n\tat ").append(traceElement);
        }
        return s.toString();
    }
}
