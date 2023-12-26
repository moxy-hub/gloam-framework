package com.gloamframework.web.security.feign;

import com.gloamframework.web.security.attribute.Attribute;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 晓龙
 */
public enum FeignAttribute implements Attribute {
    NO_ENCRYPT;

    public static void clearAll(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        for (FeignAttribute attribute : FeignAttribute.values()) {
            request.removeAttribute(attribute.name());
        }
    }
}
