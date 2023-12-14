package com.gloamframework.web.security.envelope;

import com.gloamframework.web.security.attribute.Attribute;
import com.gloamframework.web.security.attribute.AuthorityAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 晓龙
 */
public enum WebEnvelopeAttribute implements Attribute {

    AES;

    public static void clearAll(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        for (WebEnvelopeAttribute attribute : WebEnvelopeAttribute.values()) {
            request.removeAttribute(attribute.name());
        }
    }
}
