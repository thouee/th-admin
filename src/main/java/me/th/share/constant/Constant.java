package me.th.share.constant;

public class Constant {

    /**
     * 用于 IP 定位转换
     */
    public static final String REGION = "内网IP|内网IP";
    /**
     * win 系统
     */
    public static final String WIN = "win";

    /**
     * mac 系统
     */
    public static final String MAC = "mac";

    /**
     * 常用接口
     */
    public static class Url {
        /*
            太平洋网络 IP 地址查询 Web 接口：http://whois.pconline.com.cn
         */
        
        /** 
         * ip 地址查询
         */
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }
}
