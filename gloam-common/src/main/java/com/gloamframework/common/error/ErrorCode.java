package com.gloamframework.common.error;

import com.gloamframework.common.lang.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码，通过改码支持国际化
 *
 * @author 晓龙
 */
@Getter
@AllArgsConstructor
public class ErrorCode {

    static final ErrorCode DEFAULT_ERROR_CODE = new ErrorCode("unknown.exception", "未知错误");

    /**
     * 错误code，在国际化配置下为英文配置
     */
    private final String code;

    /**
     * 错误信息，在国际化配置下为中文配置
     */
    private String message;

    /**
     * 通过调用当前方法，将错误消息中{}的站位符进行填充<br/>
     * e.g.: {@code throw new Exception(LetterError.ERROR_LETTER_IS_NOT_EXIST.params(1));}
     *
     * @param params 填充参数
     * @return 返回链本身
     */
    public ErrorCode params(Object... params) {
        // 设置消息
        return this.setMessage(StringUtil.format(this.message, params));
    }

    private ErrorCode setMessage(String message) {
        this.message = message;
        return this;
    }

}
