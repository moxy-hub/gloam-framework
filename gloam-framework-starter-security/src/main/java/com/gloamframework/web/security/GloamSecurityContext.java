package com.gloamframework.web.security;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.boot.context.SpringContext;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.token.TokenManager;
import com.gloamframework.web.security.token.constant.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.List;

/**
 * gloam对于认证的上下文处理
 *
 * @author 晓龙
 */
public class GloamSecurityContext {

    private static final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
    private static TokenManager tokenManager;

    /**
     * 通过认证，该方法只会通过spring security的认证，不会有响应头的携带，使用场景：验证token
     */
    public static void passAuthentication(Object principal) {
        passAuthentication(principal, null);
    }

    /**
     * 通过认证
     */
    public static void passAuthentication(Object principal, Object credentials) {
        // todo 拉取权限
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("sss:sss");
        // 生成spring security认证token
        GloamAuthenticationToken gloamAuthenticationToken = new GloamAuthenticationToken(principal, credentials, authorities);
        // 通过认证
        SecurityContextHolder.getContext().setAuthentication(gloamAuthenticationToken);
    }

    /**
     * 通过认证，该方法会返回携带token的响应头，使用场景：登录
     *
     * @param principal   认证主体
     * @param credentials 凭证
     * @param device      认证设备
     */
    public static void passAuthenticationWithResponseHeader(String principal, Object credentials, Device device) {
        // 认证通过
        passAuthentication(principal, credentials);
        // 生成token
        tokenManager().authenticate(principal, device);
    }

    /**
     * 通过认证，该方法会返回携带token的响应头，使用场景：登录
     */
    public static void passAuthenticationWithResponseHeader(String principal, Device device) {
        passAuthenticationWithResponseHeader(principal, null, device);
    }

    /**
     * 通过认证，该方法会返回携带token的响应头，使用场景：登录
     */
    public static void passAuthenticationWithResponseHeader(String principal) {
        passAuthenticationWithResponseHeader(principal, null, null);
    }


    /**
     * 吊销指定设备认证信息，彻底抹除
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    public static void revokeAuthentication(String subject, Device device) {
        // 清除token
        tokenManager().revoke(subject, device);
        if (StrUtil.equals(obtainAuthenticationPrincipal(), subject)) {
            securityContextLogoutHandler.logout(WebContext.obtainRequest(), WebContext.obtainResponse(), obtainAuthentication());
        }
    }

    /**
     * 吊销全部设备认证信息，彻底抹除
     *
     * @param subject 认证主题
     */
    public static void revokeAuthentication(String subject) {
        revokeAuthentication(subject, null);
    }

    /**
     * 将用户对应设备踢下线，不会彻底抹除
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    public static void kickOffAuthentication(String subject, Device device) {
        tokenManager().kickOff(subject, device);
        if (StrUtil.equals(obtainAuthenticationPrincipal(), subject)) {
            securityContextLogoutHandler.logout(WebContext.obtainRequest(), WebContext.obtainResponse(), obtainAuthentication());
        }
    }

    /**
     * 将用户全部设备的登录踢下线，不会彻底抹除
     *
     * @param subject 认证主题
     */
    public static void kickOffAuthentication(String subject) {
        kickOffAuthentication(subject, null);
    }

    /**
     * 获取认证令牌
     */
    public static Authentication obtainAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取认证主体信息，使用场景：获取登录用户
     */
    public static String obtainAuthenticationPrincipal() {
        Authentication authentication = obtainAuthentication();
        if (authentication == null) {
            return null;
        }
        return String.valueOf(authentication.getPrincipal());
    }

    /**
     * 获取token管理器
     */
    private static TokenManager tokenManager() {
        if (tokenManager == null) {
            tokenManager = SpringContext.getContext().getBean("tokenManager", TokenManager.class);
        }
        return tokenManager;
    }

}
