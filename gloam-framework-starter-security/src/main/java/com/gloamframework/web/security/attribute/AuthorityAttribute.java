package com.gloamframework.web.security.attribute;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限参数
 *
 * @author 晓龙
 */
public enum AuthorityAttribute implements Attribute {

    AUTHORITY_SYMBOL;

    public static void clearAll(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        for (AuthorityAttribute attribute : AuthorityAttribute.values()) {
            request.removeAttribute(attribute.name());
        }
    }

}
