package com.gloamframework.web.undertow;

import com.gloamframework.web.properties.WebServerProperties;
import com.gloamframework.web.undertow.properties.UndertowProperties;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * undertow配置注册
 *
 * @author 晓龙
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(UndertowProperties.class)
@Configurable
@Slf4j
public class UndertowConfigure {

    @Bean
    public BufferPoolCustomizer bufferPoolCustomizer(UndertowProperties properties) {
        return new BufferPoolCustomizer(properties.getBufferPool());
    }

    @Bean
    @ConditionalOnProperty(value = "com.gloam.web.server.ssl.enabled", havingValue = "true")
    @ConditionalOnBean(UndertowServletWebServerFactory.class)
    @ConditionalOnClass(Undertow.class)
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowContainer(WebServerProperties webServerProperties) {
        if (webServerProperties.getHttpPort() < 0) {
            return undertow -> {
            };
        }
        return undertow -> {
            undertow.addBuilderCustomizers(builder -> {
                builder.addHttpListener(webServerProperties.getHttpPort(), "0.0.0.0");
                // 开启HTTP2
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            });
            if (webServerProperties.getSsl().isEnabledHttp2Https()) {
                undertow.addDeploymentInfoCustomizers(deploymentInfo -> {
                    // 开启HTTP自动跳转至HTTPS
                    deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                                    .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                            .setConfidentialPortManager(exchange -> webServerProperties.getPort());
                });
                log.debug("enable undertow http redirect to https");
            }
            log.debug("undertow servlet web server add port:{}", webServerProperties.getHttpPort());
        };
    }
}
