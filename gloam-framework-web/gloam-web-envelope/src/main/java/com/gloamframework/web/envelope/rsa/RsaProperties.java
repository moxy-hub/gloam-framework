package com.gloamframework.web.envelope.rsa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rsa配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.web.security.rsa")
@Data
public class RsaProperties {

    /**
     * service的请求头字段
     */
    private String serviceHeader = "serviceCode";

    /**
     * 支持的serviceCode
     */
    private String[] supportService = {"none"};

}
