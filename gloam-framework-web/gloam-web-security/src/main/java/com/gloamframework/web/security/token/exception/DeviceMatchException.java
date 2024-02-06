package com.gloamframework.web.security.token.exception;

/**
 * 设备匹配异常
 *
 * @author 晓龙
 */
public class DeviceMatchException extends TokenException {

    public DeviceMatchException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public DeviceMatchException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
