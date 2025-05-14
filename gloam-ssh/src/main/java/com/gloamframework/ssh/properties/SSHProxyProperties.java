package com.gloamframework.ssh.properties;

import com.gloamframework.ssh.SSHProxyType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年05月14日 14:36
 */
@ConfigurationProperties("gloam")
@Data
public class SSHProxyProperties {

    private boolean sshEnable = false;

    private Map<SSHProxyType, SSHProxyItems> ssh;

    @Data
    public static class SSHProxyItems {

        /**
         * ssh的地址
         */
        private String host;

        /**
         * ssh的端口
         */
        private int port = 22;

        /**
         * 用户名
         */
        private String username = "root";

        /**
         * 密码，不传则不填入
         */
        private String password;

        /**
         * 证书文件:classpath:certs/test-server-rsa
         */
        private ClassPathResource certFile;

        /**
         * 通行短语，不传则不传入
         */
        private String certPhrase;

        /**
         * 需要转发的远程地址
         */
        private String remoteHost;

        /**
         * 需要转发的远程端口
         */
        private int remotePort;

        /**
         * 本地的监听端口,默认0为随机分配
         */
        private int listenPort = 0;

        /**
         * 设置发送保持活动消息的间隔。如果指定为零，则不得发送任何保持活动消息。默认间隔为零
         */
        private int keepLive = 15000;

    }

}
