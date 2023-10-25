package com.gloamframework.core.boot.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * gloam项目通用异常基类
 * 新增通log占位符方式进行传参，占位符{}
 *
 * @author 晓龙
 */
@SuppressWarnings("unused")
public class GloamRuntimeException extends RuntimeException {
	public GloamRuntimeException() {
	}

	public GloamRuntimeException(String message) {
		super(message);
	}

	public GloamRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GloamRuntimeException(Throwable cause) {
		super(cause);
	}

	public GloamRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GloamRuntimeException(String messagePattern, Throwable cause, Object... params) {
		this(MessageFormatter.arrayFormat(messagePattern, params).getMessage(), cause);
	}

	public GloamRuntimeException(String messagePattern, Object... params) {
		this(messagePattern, null, params);
	}

}
