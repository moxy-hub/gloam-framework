package com.gloamframework.core.web.context;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * web上下文
 *
 * @author 晓龙
 */
@Slf4j
@SuppressWarnings("unused")
public class WebContext {

    /**
     * 请求头中的userAgent字段
     */
    private static final String USER_AGENT_HEADER = "user-agent";
    private static final String IP_UTILS_FLAG = ",";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IP1 = "127.0.0.1";

    /**
     * 获取当前请求
     */
    public static HttpServletRequest obtainRequest() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获取响应
     */
    public static HttpServletResponse obtainResponse() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getResponse();
    }

    /**
     * 获取ServletRequestAttributes
     */
    private static ServletRequestAttributes getServletRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return (ServletRequestAttributes) requestAttributes;
    }

    /**
     * 获取用户代理信息
     */
    public static UserAgent obtainUserAgent(HttpServletRequest request) {
        if (request == null) {
            throw new WebContextException("获取请求失败");
        }
        return UserAgentUtil.parse(request.getHeader(USER_AGENT_HEADER));
    }

    /**
     * 获取用户代理信息
     */
    public static UserAgent obtainUserAgent() {
        return obtainUserAgent(obtainRequest());
    }

    /**
     * 获取IP公网地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String obtainIp(HttpServletRequest request) {
        if (request == null) {
            throw new WebContextException("获取请求失败");
        }
        /*
         * 以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
         */
        String ip = request.getHeader("X-Original-Forwarded-For");
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        // 获取nginx等代理的ip
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 兼容k8s集群获取ip
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCALHOST_IP1.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("获取ip错误:{}", e.getMessage());
                    throw new WebContextException("获取ip错误", e);
                }
                ip = inetAddress.getHostAddress();
            }
        }
        //使用代理，则获取第一个IP地址
        if (StrUtil.isNotBlank(ip) && ip.indexOf(IP_UTILS_FLAG) > 0) {
            ip = ip.substring(0, ip.indexOf(IP_UTILS_FLAG));
        }
        return ip;
    }

    /**
     * 获取公网ip
     */
    public static String obtainIp() {
        return obtainIp(obtainRequest());
    }

}
