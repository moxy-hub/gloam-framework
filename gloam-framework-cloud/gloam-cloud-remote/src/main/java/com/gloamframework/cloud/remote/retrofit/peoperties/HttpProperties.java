package com.gloamframework.cloud.remote.retrofit.peoperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * http配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.http")
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

        /**
         * 设置完整调用的默认超时时间。取值为0表示不超时，否则取值必须在1到整数之间，默认为0
         */
        private int callTimeout = 0;
    }

}
