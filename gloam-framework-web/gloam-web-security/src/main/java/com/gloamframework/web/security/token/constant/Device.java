package com.gloamframework.web.security.token.constant;

import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgent;
import com.gloamframework.web.security.token.exception.DeviceMatchException;

/**
 * 设备常量
 *
 * @author 晓龙
 */
public enum Device {

    /**
     * 电脑设备
     */
    PC,

    /**
     * 手机设备
     */
    MOBILE,

    /**
     * 未知
     */
    UNKNOWN;

    /**
     * 匹配对应的设备
     *
     * @param userAgent 用户请求信息
     */
    public static Device match(UserAgent userAgent) {
        Platform platform = null;
        if (userAgent == null || (platform = userAgent.getPlatform()) == null) {
            throw new DeviceMatchException("匹配请求来源设备失败,用户请求信息:{},平台信息:{}", userAgent, platform);
        }
        if (platform.isMobile()) {
            // 匹配苹果、安卓、WINDOWS_PHONE等系统返回
            return MOBILE;
        } else if (Platform.desktopPlatforms.contains(platform)) {
            // 匹配PC设备
            return PC;
        }
        return UNKNOWN;
    }

}
