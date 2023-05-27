package me.th.share.constant;

public interface Constant {

    /**
     * 操作日志类型
     */
    String LOG_TYPE_INFO = "INFO";
    /**
     * 异常日志类型
     */
    String LOG_TYPE_ERROR = "ERROR";

    /**
     * 用于 IP 定位转换
     */
    String REGION = "内网IP|内网IP";
    /**
     * win 系统
     */
    String WIN = "win";
    /**
     * mac 系统
     */
    String MAC = "mac";

    /**
     * 常用接口
     */
    interface Url {
        /*
            太平洋网络 IP 地址查询 Web 接口：http://whois.pconline.com.cn
         */

        /**
         * ip 地址查询
         */
        String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }
}
