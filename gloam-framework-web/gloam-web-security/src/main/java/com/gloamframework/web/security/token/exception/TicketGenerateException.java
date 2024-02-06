package com.gloamframework.web.security.token.exception;

/**
 * 临时票据生成异常
 *
 * @author 晓龙
 */
public class TicketGenerateException extends TokenException {
    public TicketGenerateException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public TicketGenerateException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
