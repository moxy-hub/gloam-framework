package com.gloamframework.core.web.tomcat;

import com.gloamframework.core.web.properties.WebServerProperties;
import com.gloamframework.core.web.tomcat.properties.TomcatProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * tomcat配置注册
 *
 * @author 晓龙
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(TomcatProperties.class)
@Configurable
@Slf4j
public class TomcatConfigure {

    /**
     * 配置tomcat的连接对象
     */
    public Connector httpConnector(WebServerProperties webServerProperties) {
        // 执行该方法，则表示开启了ssl，默认创建一个http端口
        Connector httpConnector = new Connector(new Http11NioProtocol());
        httpConnector.setScheme("http");
        httpConnector.setPort(webServerProperties.getHttpPort());
        // 根据配置，是否开启http转https的重定向
        if (webServerProperties.getSsl().isEnabledHttp2Https()) {
            log.debug("enable tomcat http redirect to https");
            httpConnector.setSecure(false);
            httpConnector.setRedirectPort(webServerProperties.getPort());
        }
        log.debug("tomcat servlet web server add connector:{}", httpConnector);
        return httpConnector;
    }

    /**
     * 配置服务器，主要为服务器设置添加Connector
     */
    @Bean
    @ConditionalOnProperty(value = "com.gloam.web.server.ssl.enabled", havingValue = "true")
    @ConditionalOnBean(TomcatServletWebServerFactory.class)
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatContainer(WebServerProperties webServerProperties) {
        if (webServerProperties.getHttpPort()<0){
            return tomcat->{};
        }
        return tomcat -> {
            tomcat.addAdditionalTomcatConnectors(httpConnector(webServerProperties));
            if (!webServerProperties.getSsl().isEnabledHttp2Https()) {
                return;
            }
            tomcat.addContextCustomizers(context -> {
                org.apache.tomcat.util.descriptor.web.SecurityConstraint securityConstraint = new org.apache.tomcat.util.descriptor.web.SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            });
        };
    }

}
