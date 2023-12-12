package com.gloamframework.web.security.envelope;

import lombok.Data;

/**
 * 信封格式
 *
 * @author 晓龙
 */
@Data
public class Envelope {

    /**
     * 加密信息
     */
    private String data;

    /**
     * 加密密钥
     */
    private String key;
}
