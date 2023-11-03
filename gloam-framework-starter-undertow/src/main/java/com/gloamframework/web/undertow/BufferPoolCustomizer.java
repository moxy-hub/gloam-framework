package com.gloamframework.web.undertow;

import com.gloamframework.web.undertow.properties.UndertowProperties;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * 为undertow提供缓冲池
 *
 * @author 晓龙
 */
@Slf4j
public class BufferPoolCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    private final static String CONTEXT_ATTRIBUTE_DEPLOYMENT_INFO = "io.undertow.websockets.jsr.WebSocketDeploymentInfo";
    private final UndertowProperties.BufferPool bufferPoolProperties;

    public BufferPoolCustomizer(UndertowProperties.BufferPool bufferPoolProperties) {
        this.bufferPoolProperties = bufferPoolProperties;
    }

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        int size = (int) bufferPoolProperties.getBufferSize().toBytes();
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.addServletContextAttribute(CONTEXT_ATTRIBUTE_DEPLOYMENT_INFO,
                    new WebSocketDeploymentInfo()
                            .setBuffers(new DefaultByteBufferPool(false, size)));
        });
        log.info("undertow buffer pool set initialize with size {}", size);
    }

}
