package me.th.share.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StringUtils {

    /**
     * 以 k-v 形式保存压缩后的包名，避免同一个包下的接口每一次都要压缩
     */
    private static final Map<String, String> PACKAGES = new ConcurrentHashMap<>();

    /**
     * 将类似 me.th.log.service.LogServiceImpl 的字符串压缩成 m.t.l.s.LogServiceImpl
     *
     * @param str -
     * @return String
     */
    public static String compressPackage(String str) {
        return PACKAGES.computeIfAbsent(str, StringUtils::doCompress);
    }

    private static String doCompress(String str) {
        String[] split = str.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            String s = split[i];
            if (s.length() >= 1) {
                builder.append(s.charAt(0)).append(".");
            }
        }
        builder.append(split[split.length - 1]);
        return builder.toString();
    }
}
