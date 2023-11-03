package com.gloamframework.core.http.peoperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * http配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloam.http")
@Data
public class HttpProperties {

    private final Client client = new Client();

    @Data
    public static class Client {
        /**
         * 连接超时，单位秒，默认3秒
         */
        private int connectTimeout = 3;

        /**
         * 读超时，单位秒，默认3
         */
        private int readTimeout = 3;

        /**
         * 写超时，单位秒，默认3
         */
        private int writeTimeout = 3;
    }

}
