package com.gloamframework.web.security.token.constant;

import cn.hutool.core.util.EnumUtil;
import com.gloamframework.web.security.token.domain.Token;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求过滤处理参数
 *
 * @author 晓龙
 */
public enum Attribute {

    /**
     * token解析属性
     */
    TOKEN,
    /**
     * 票据
     */
    TICKET,
    /**
     * 请求设备
     */
    DEVICE;

    /**
     * 将请求携带的信息转化为内部使用属性
     *
     * @param request 请求
     */
    public static void setAttributes(HttpServletRequest request, Attribute attributeName, Object attribute) {
        if (request == null) {
            return;
        }
        // 如果为TOKEN，则必须为规定的类型
        if (EnumUtil.equals(attributeName, Attribute.TOKEN.name()) && !Token.class.isAssignableFrom(attribute.getClass())) {
            return;
        }
        // 设置
        request.setAttribute(attributeName.name(), attribute);
    }

    /**
     * 移除请求的内部处理属性
     *
     * @param request 请求
     */
    public static void removeAttributes(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        for (Attribute attribute : Attribute.values()) {
            request.removeAttribute(attribute.name());
        }
    }

    /**
     * 获取内部参数
     *
     * @param request 请求
     */
    public Object obtain(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getAttribute(this.name());
    }

    /**
     * 仅对TOKEN枚举有效
     *
     * @param request 请求
     */
    public Token obtainToken(HttpServletRequest request) {
        if (!EnumUtil.equals(Attribute.TOKEN, this.name())) {
            return null;
        }
        return (Token) obtain(request);
    }

}
