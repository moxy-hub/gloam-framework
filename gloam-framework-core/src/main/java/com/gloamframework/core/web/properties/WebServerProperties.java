package com.gloamframework.core.web.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.util.unit.DataSize;

import java.net.InetAddress;

/**
 * web配置重写
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloam.web.server")
@Data
public class WebServerProperties {

    /**
     * 服务http端口，默认6387
     */
    @MappingConfigurationProperty("server.port")
    private Integer port = 6387;

    /**
     * 服务绑定的ip地址
     */
    @MappingConfigurationProperty("server.address")
    private InetAddress address;

    /**
     * 响应头
     */
    @MappingConfigurationProperty("server.server-header")
    private String serverHeader = "gloamframework";

    /**
     * htpp消息头最大大小，默认10kb
     */
    @MappingConfigurationProperty("server.max-http-header-size")
    private DataSize maxHttpHeaderSize = DataSize.ofKilobytes(10);

    /**
     * 服务关闭的策略，默认优雅关闭
     */
    @MappingConfigurationProperty("server.shutdown")
    private Shutdown shutdown = Shutdown.GRACEFUL;

//    @NestedConfigurationProperty
//    private Ssl ssl;
//
//    @NestedConfigurationProperty
//    private final Compression compression = new Compression();
}