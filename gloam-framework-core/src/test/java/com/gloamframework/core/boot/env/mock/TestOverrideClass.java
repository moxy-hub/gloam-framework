package com.gloamframework.core.boot.env.mock;

import com.gloamframework.core.boot.env.OverrideClass;
import com.gloamframework.core.boot.env.OverrideProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.net.InetAddress;
import java.net.UnknownHostException;

@OverrideClass
@ConfigurationProperties("com.moxy")
@Data
public class TestOverrideClass {

    @OverrideProperty("server.address")
    private InetAddress t1;

    @OverrideProperty("server.port")
    @NestedConfigurationProperty
    private Integer t2 = 9897;

    public TestOverrideClass() throws UnknownHostException {
        this.t1 = InetAddress.getLocalHost();
    }
}
