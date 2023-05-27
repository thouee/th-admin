package me.th.share.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.th.share.constant.Constant;
import me.th.share.properties.AppProperties;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Slf4j
public class WebUtils {

    public static final String COMMA = ",";
    public static final String LOCALHOST = "127.0.0.1";
    private static final String UNKNOWN = "unknown";
    private static final Ip2regionSearcher IP_SEARCHER = SpringContextHolder.getBean(Ip2regionSearcher.class);
    private static final UserAgentAnalyzer USER_AGENT_ANALYZER = UserAgentAnalyzer
            .newBuilder().hideMatcherLoadStats().withCache(10_000).withField(UserAgent.AGENT_NAME_VERSION)
            .build();

    private WebUtils() {
    }

    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forward-for");
        if (ipIsNull(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ipIsNull(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipIsNull(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(COMMA)) {
            ip = ip.split(COMMA)[0];
        }
        if (LOCALHOST.equals(ip)) {
            // 获取本机真正的 ip 地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    private static boolean ipIsNull(String ip) {
        return ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip);
    }

    public static String getCityInfo(String ip) {
        if (AppProperties.localParse) {
            return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据 ip 获取详细地址
     *
     * @param ip -
     * @return String
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(Constant.Url.IP_URL, ip);
        JSONObject result = JSONUtil.parseObj(HttpUtil.get(ip));
        return result.get("addr", String.class);
    }

    /**
     * 根据 ip 获取本地详细地址
     *
     * @param ip -
     * @return String
     */
    public static String getLocalCityInfo(String ip) {
        IpInfo ipInfo = IP_SEARCHER.memorySearch(ip);
        return ipInfo != null ? ipInfo.getAddress() : null;
    }

    /**
     * 获取浏览器信息
     *
     * @param request -
     * @return String
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent userAgent = USER_AGENT_ANALYZER.parse(request.getHeader("User-Agent"));
        return userAgent.get(UserAgent.AGENT_NAME_VERSION).getValue();
    }

    /**
     * 获取当前机器 IP
     *
     * @return String
     */
    public static String getLocalIP() {
        InetAddress candidateAddress = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface anInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = anInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    // 排除本地回环地址
                    if (!address.isLoopbackAddress()) {
                        if (address.isSiteLocalAddress()) {
                            // 是局域网地址
                            return address.getHostAddress();
                        } else if (candidateAddress == null) {
                            // 未发现局域网地址，先记录候选地址
                            candidateAddress = address;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 最次选项
            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost != null) {
                return localHost.getHostAddress();
            }
            return "";
        } catch (SocketException | UnknownHostException e) {
            log.warn("获取本机地址发生错误：{}", e.getMessage());
            return "";
        }
    }
}
