package com.gloamframework.web.security.token.constant;

import cn.hutool.core.util.EnumUtil;
import com.gloamframework.web.security.attribute.Attribute;
import com.gloamframework.web.security.token.domain.Token;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求过滤处理参数
 *
 * @author 晓龙
 */
public enum TokenAttribute implements Attribute {

    /**
     * token解析属性
     */
    TOKEN,
    /**
     * 请求设备
     */
    DEVICE,
    /**
     * token策略
     */
    TOKEN_STRATEGY,
    /**
     * 是否进行刷新
     */
    TOKEN_REFRESH,
    /**
     * token主题
     */
    TOKEN_SUBJECT;

    public static void clearAll(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        for (TokenAttribute attribute : TokenAttribute.values()) {
            request.removeAttribute(attribute.name());
        }
    }

    /**
     * 仅对TOKEN枚举有效
     *
     * @param request 请求
     */
    public Token obtainToken(HttpServletRequest request) {
        if (!EnumUtil.equals(TokenAttribute.TOKEN, this.name())) {
            return null;
        }
        return (Token) obtain(request);
    }

}
