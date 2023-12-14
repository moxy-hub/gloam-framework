package com.gloamframework.web.security.plugin.envelope;

import lombok.Data;

/**
 * 信封格式
 *
 * @author 晓龙
 */
@Data
public class WebEnvelopeData {

    /**
     * 加密信息
     */
    private String data;

    /**
     * 加密密钥
     */
    private String key;
}
