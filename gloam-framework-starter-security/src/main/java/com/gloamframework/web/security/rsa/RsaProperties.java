package com.gloamframework.web.security.rsa;

import lombok.Data;

/**
 * rsa配置
 *
 * @author 晓龙
 */
@Data
public class RsaProperties {

    /**
     * service的请求头字段
     */
    private String serviceHeader = "service_code";

    /**
     * 支持的serviceCode
     */
    private String[] supportService = {"none"};

}
