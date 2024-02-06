package com.gloamframework.data.tenant.attribute;

import com.gloamframework.web.security.attribute.Attribute;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 晓龙
 */
public enum TenantAttribute implements Attribute {

    TENANT_ID;

    public static void clearAll(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        for (TenantAttribute attribute : TenantAttribute.values()) {
            request.removeAttribute(attribute.name());
        }
    }
}
