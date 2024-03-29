package com.gloamframework.web.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.gloamframework.common.error.GloamInternalException;
import com.gloamframework.core.boot.context.SpringContext;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.token.TokenManager;
import com.gloamframework.web.security.token.constant.Device;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.*;

/**
 * gloam对于认证的上下文处理
 *
 * @author 晓龙
 */
@Slf4j
public class GloamSecurityContext {

    private static final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
    private static TokenManager tokenManager;
    private static List<GloamSecurityAuthority> gloamSecurityAuthorities;

    /**
     * 通过认证，该方法只会通过spring security的认证，不会有响应头的携带，使用场景：验证token
     */
    public static void passAuthentication(Object principal, String platform) {
        passAuthentication(principal, null, platform);
    }

    /**
     * 通过认证
     */
    public static void passAuthentication(Object principal, Object credentials, String platform) {
        // 拉取权限
        List<GrantedAuthority> authorities = pullAuthorities(principal, platform);
        // 生成spring security认证token
        GloamAuthenticationToken gloamAuthenticationToken = new GloamAuthenticationToken(principal, credentials, authorities);
        if (!gloamAuthenticationToken.isAuthenticated()) {
            throw new GloamInternalException("用户认证未通过");
        }
        // 通过认证
        SecurityContextHolder.getContext().setAuthentication(gloamAuthenticationToken);
        WebContext.setAuthenticatedUser(principal);
    }

    /**
     * 通过认证，该方法会返回携带token的响应头，使用场景：登录
     *
     * @param principal   认证主体
     * @param credentials 凭证
     * @param device      认证设备
     */
    public static void passAuthenticationWithResponseHeader(String principal, Object credentials, Device device, String platform) {
        // 认证通过
        passAuthentication(principal, credentials, platform);
        // 生成token
        tokenManager().authenticate(principal, device, platform);
    }

    /**
     * 通过认证，该方法会返回携带token的响应头，使用场景：登录
     */
    public static void passAuthenticationWithResponseHeader(String principal, Device device, String platform) {
        passAuthenticationWithResponseHeader(principal, null, device, platform);
    }

    /**
     * 通过认证，该方法会返回携带token的响应头，使用场景：登录
     */
    public static void passAuthenticationWithResponseHeader(String principal, Device device) {
        passAuthenticationWithResponseHeader(principal, device, null);
    }

    /**
     * 吊销指定设备认证信息，彻底抹除
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    public static void revokeAuthentication(String subject, Device device, String platform) {
        // 清除token
        tokenManager().revoke(subject, device, platform);
        if (StrUtil.equals(obtainAuthenticationPrincipal(), subject)) {
            securityContextLogoutHandler.logout(WebContext.obtainRequest(), WebContext.obtainResponse(), obtainAuthentication());
        }
    }

    /**
     * 吊销全部设备认证信息，彻底抹除
     *
     * @param subject 认证主题
     */
    public static void revokeAuthentication(String subject, Device device) {
        revokeAuthentication(subject, device, null);
    }

    /**
     * 将用户对应设备踢下线，不会彻底抹除
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    public static void kickOffAuthentication(String subject, Device device, String platform) {
        tokenManager().kickOff(subject, device, platform);
        if (StrUtil.equals(obtainAuthenticationPrincipal(), subject)) {
            securityContextLogoutHandler.logout(WebContext.obtainRequest(), WebContext.obtainResponse(), obtainAuthentication());
        }
    }

    /**
     * 将用户全部设备的登录踢下线，不会彻底抹除
     *
     * @param subject 认证主题
     */
    public static void kickOffAuthentication(String subject, Device device) {
        kickOffAuthentication(subject, device, null);
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
            tokenManager = SpringContext.getContext().getBean(TokenManager.class);
        }
        return tokenManager;
    }

    /**
     * 获取权限实现类
     */
    private static List<GloamSecurityAuthority> gloamSecurityAuthorities(String platform) {
        if (CollectionUtil.isNotEmpty(gloamSecurityAuthorities)) {
            return gloamSecurityAuthorities;
        }
        if (Objects.isNull(gloamSecurityAuthorities)) {
            gloamSecurityAuthorities = new ArrayList<>();
        }
        Map<String, GloamSecurityAuthority> beanMaps = SpringUtil.getBeansOfType(GloamSecurityAuthority.class);
        if (MapUtil.isEmpty(beanMaps)) {
            log.error("没有找到配置的权限注入实现类，请实现GloamSecurityAuthority接口，并注入spring中");
            return gloamSecurityAuthorities;
        }
        for (GloamSecurityAuthority securityAuthority : beanMaps.values()) {
            if (!StringUtils.equalsIgnoreCase(platform, securityAuthority.support())) {
                continue;
            }
            gloamSecurityAuthorities.add(securityAuthority);
        }
        return gloamSecurityAuthorities;
    }

    /**
     * 拉取全部权限
     */
    private static List<GrantedAuthority> pullAuthorities(Object principal, String platform) {
        List<GloamSecurityAuthority> authorities = gloamSecurityAuthorities(platform);
        Set<String> allAuths = new HashSet<>();
        authorities.forEach(a -> allAuths.addAll(a.authorities(principal)));
        return AuthorityUtils.createAuthorityList(allAuths.toArray(new String[]{}));
    }

}
