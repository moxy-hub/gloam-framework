package com.gloamframework.web.security.token;

import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.domain.Token;

/**
 * token管理，统一处理请求的可信性
 *
 * @author 晓龙
 */
public interface TokenManager {

    /**
     * 生成认证票据，会在响应头中返回票据的
     */
    void ticket(String subject, Device device);

    /**
     * 生成认证票据
     */
    default void ticket(String subject) {
        ticket(subject, null);
    }

    /**
     * 认证用户，会在响应头中返回认证token
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    void authenticate(String subject, Device device);

    /**
     * 认证用户，会在响应头中返回认证token,会登录全部设备
     *
     * @param subject 认证主题
     */
    default void authenticate(String subject) {
        authenticate(subject, null);
    }

    /**
     * 检查当前请求的认证信息
     *
     * @param device 认证设备,用于查看是否在对应的设备上认证
     */
    void checkAuthentication(Device device);

    /**
     * 检查当前请求的认证信息，会自动获取请求的设备，进行自动匹配
     */
    default void checkAuthentication() {
        checkAuthentication(null);
    }

    /**
     * 切换token绑定的权限标识
     */
    void switchAuth(String subject, Device device, String authSymbol);

    /**
     * 切换token绑定的权限标识
     */
    default void switchAuth(String subject, String authSymbol) {
        switchAuth(subject, null, authSymbol);
    }

    /**
     * 吊销指定设备认证信息，彻底抹除
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    void revoke(String subject, Device device);

    /**
     * 吊销全部设备认证信息，彻底抹除
     *
     * @param subject 认证主题
     */
    default void revoke(String subject) {
        revoke(subject, null);
    }

    /**
     * 将用户对应设备踢下线，不会彻底抹除
     *
     * @param subject 认证主题
     * @param device  认证设备
     */
    void kickOff(String subject, Device device);

    /**
     * 将用户全部设备的登录踢下线，不会彻底抹除
     *
     * @param subject 认证主题
     */
    default void kickOff(String subject) {
        kickOff(subject, null);
    }

    /**
     * 获取当前请求的认证token信息，可能为null
     */
    Token obtainToken();
}
