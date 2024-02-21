package com.gloamframework.web.security.properties;

import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 安全配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.web.security")
@Data
public class SecurityProperties {

    /**
     * 权限标识请求头，用于识别是否存在多权限配置
     */
    private String authoritySymbolHeader = "Authority";

    /**
     * token相关的配置
     */
    @NestedConfigurationProperty
    private final TokenProperties token = new TokenProperties();


}
