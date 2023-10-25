package com.gloamframework.core.boot.diagnostics;

import com.gloamframework.core.boot.exception.GloamRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

@AllArgsConstructor
@Getter
public class GloamStartException extends GloamRuntimeException {

    private final String description;
    private final String action;
    private final Throwable cause;

    public GloamStartException(String description, String action) {
        this(description, action, (Throwable) null);
    }

    public GloamStartException(String description) {
        this(description, "");
    }

    public GloamStartException(String description, String action, Object... params) {
        this(MessageFormatter.arrayFormat(description, params).getMessage(), action);
    }
}
