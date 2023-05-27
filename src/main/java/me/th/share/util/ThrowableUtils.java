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
}
