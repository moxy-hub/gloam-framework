package com.gloamframework.core.json.mask;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * json序列化脱敏
 */
@AllArgsConstructor
@Slf4j
abstract class AbstractMask extends JsonSerializer<String> {

    /**
     * 脱敏字符
     */
    private static final String mask = "*";

    /**
     * 脱敏开始下标
     */
    private int retainPreFix;

    /**
     * 脱敏结束下标
     */
    private int retainTailFix;

    /**
     * 脱敏统一长度,当脱敏位数不够时,将自动填充 如果小于等于0,则标识,不格式化长度,按真实数据长度进行脱敏
     */
    private final int maskLen;

    protected String mask(String source) {
        if (StringUtils.isBlank(source)) {
            return "";
        }
        // 全脱敏
        if (source.length() < retainPreFix + retainTailFix || (retainPreFix == 0 && retainTailFix == 0)) {
            return StringUtils.leftPad("", retainPreFix + retainTailFix + maskLen, mask);
        }
        // 字段长度和保留长度相同，则后位脱敏
        if (source.length() == retainPreFix + retainTailFix) {
            retainTailFix = 0;
        }
        String head = StringUtils.substring(source, 0, retainPreFix);
        String masking = maskLen <= 0 ? StringUtils.leftPad("", source.length() - retainPreFix - retainTailFix, mask) : StringUtils.leftPad("", maskLen, mask);
        String tail = retainTailFix <= 0 ? "" : StringUtils.substring(source, -retainTailFix);
        return head + masking + tail;
    }

    @Override
    public void serialize(String string, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String maskStr = this.mask(string);
        log.debug("[json mask]: result -> {}", maskStr);
        jsonGenerator.writeString(maskStr);
    }

}
