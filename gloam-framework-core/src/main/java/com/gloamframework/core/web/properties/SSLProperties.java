package com.gloamframework.core.web.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.web.server.Ssl;

/**
 * ssl配置重写
 *
 * @author 晓龙
 */
@Data
public class SSLProperties {

    /**
     * 启动SSL认证
     */
    @MappingConfigurationProperty("enabled")
    private boolean enabled = false;

    /**
     * 是否开启http转https，默认不开启，因为默认SSL开启后必须要客户端证书，所以保留http端口
     * 相关支持需要引入gloam-framework-starter-tomcat或gloam-framework-starter-undertow
     */
    private boolean enabledHttp2Https = false;

    /**
     * 认证模式,默认为WANT
     * <li>NONE:不要求客户端证书
     * <li>WANT:希望客户端使用证书，不强制
     * <li>NEED:客户端必须使用证书
     */
    @MappingConfigurationProperty("client-auth")
    private Ssl.ClientAuth clientAuth = Ssl.ClientAuth.NEED;

    /**
     * 证书类型，gloam提供的默认证书的类型为JKS
     */
    @MappingConfigurationProperty("key-store-type")
    private String certificateType = "JKS";

    /**
     * 证书路径，可以使用classpath:...或file:...
     */
    @MappingConfigurationProperty("key-store")
    private String certificate = "classpath:gloam.keystore";

    /**
     * 证书密码
     */
    @MappingConfigurationProperty("key-store-password")
    private String certificatePassword = "gloamssl";

    /**
     * 证书别名
     */
    @MappingConfigurationProperty("key-alias")
    private String certificateAlias = "gloamssl";
}
