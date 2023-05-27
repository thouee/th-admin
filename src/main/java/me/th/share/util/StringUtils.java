package me.th.share.util;

public class StringUtils {

    /**
     * 将类似 me.th.log.service.LogServiceImpl 的字符串压缩成 m.t.l.s.LogServiceImpl
     *
     * @param str -
     * @return String
     */
    public static String compressPackage(String str) {
        String[] split = str.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            String s = split[i];
            if (s.length() >= 1) {
                builder.append(s.charAt(0));
            }
        }
        builder.append(split[split.length - 1]);
        return builder.toString();
    }
}
