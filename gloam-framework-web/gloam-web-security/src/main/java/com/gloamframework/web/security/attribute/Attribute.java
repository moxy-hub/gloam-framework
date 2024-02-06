package com.gloamframework.web.security.attribute;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求属性
 *
 * @author 晓龙
 */
public interface Attribute {

    String name();

    /**
     * 将请求携带的信息转化为内部使用属性
     *
     * @param request 请求
     */
    default void setAttributes(HttpServletRequest request, Object attribute) {
        if (request == null) {
            return;
        }
        // 设置
        request.setAttribute(this.name(), attribute);
    }

    /**
     * 移除请求的内部处理属性
     *
     * @param request 请求
     */
    default void removeAttribute(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        request.removeAttribute(this.name());
    }

    /**
     * 获取内部参数
     *
     * @param request 请求
     */
    default Object obtain(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getAttribute(this.name());
    }

}
