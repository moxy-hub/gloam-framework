package com.gloamframework.ssh;

import com.gloamframework.common.error.GloamInternalException;
import com.gloamframework.ssh.properties.SSHProxyProperties;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年05月14日 15:11
 */
public class CommonSSHProxyConnect implements SSHProxyContext {
    private final Log log;
    private final boolean enable;
    private final Map<SSHProxyType, SSHProxyProperties.SSHProxyItems> sshProperties = new ConcurrentHashMap<>();
    private final Map<SSHProxyType, Integer> activateSSHPort = new ConcurrentHashMap<>();

    public CommonSSHProxyConnect(ConfigurableEnvironment environment, Log log) {
        if (Objects.nonNull(log)) {
            this.log = log;
        } else {
            this.log = new DeferredLog();
        }
        // 检查是否启动，如果没启动，则无需进行映射
        Boolean enable = environment.getProperty("gloam.ssh-enable", Boolean.class);
        this.enable = Objects.nonNull(enable) && enable;
        // 通过类型枚举匹配配置
        if (this.enable) {
            for (SSHProxyType sshProxyType : SSHProxyType.values()) {
                this.buildConfigurations(sshProxyType, environment);
            }
        }
    }

    @Override
    public void openSSHProxy() {
        if (!this.enable) {
            log.info("[GloamSSH]:SSH功能已关闭，如需使用，请配置gloam.ssh-enable=true");
            return;
        }
        log.info("[GloamSSH]:开始创建SSH通道...");
        this.sshProperties.forEach((type, properties) -> {
            if (!this.existSSHProxy(properties.getListenPort())) {
                int listenPort = this.openSSHByProperties(properties);
                this.activateSSHPort.put(type, listenPort);
                log.info("[GloamSSH]:创建SSH通道成功，通道类型:" + type + " 本地监听端口:" + listenPort);
            }
        });
    }

    @Override
    public boolean existSSHProxy(int listenPort) {
        return this.activateSSHPort.containsValue(listenPort);
    }

    @Override
    public Integer getSSHProxyPorts(SSHProxyType sshProxyType) {
        Integer listenPort = this.activateSSHPort.get(sshProxyType);
        if (Objects.isNull(listenPort)) {
            throw new GloamInternalException("[GloamSSH]:未找的与类型:{} 匹配的ssh通道", sshProxyType.name());
        }
        return listenPort;
    }


    private void buildConfigurations(SSHProxyType sshProxyType, ConfigurableEnvironment environment) {
        String propertiesPrefix = "gloam.ssh." + StringUtils.lowerCase(sshProxyType.name()) + ".";
        SSHProxyProperties.SSHProxyItems ssh = null;
        if (StringUtils.isNotBlank(environment.getProperty(propertiesPrefix + "host"))) {
            ssh = this.obtainValidItems(ssh);
            ssh.setHost(environment.getProperty(propertiesPrefix + "host"));
        }

        if (environment.getProperty(propertiesPrefix + "port", Integer.TYPE, -1) > 0) {
            ssh = this.obtainValidItems(ssh);
            ssh.setPort(environment.getProperty(propertiesPrefix + "port", Integer.TYPE, -1));
        }

        if (StringUtils.isNotBlank(environment.getProperty(propertiesPrefix + "username"))) {
            ssh = this.obtainValidItems(ssh);
            ssh.setUsername(environment.getProperty(propertiesPrefix + "username"));
        }

        if (StringUtils.isNotBlank(environment.getProperty(propertiesPrefix + "password"))) {
            ssh = this.obtainValidItems(ssh);
            ssh.setUsername(environment.getProperty(propertiesPrefix + "password"));
        }

        if (StringUtils.isNotBlank(environment.getProperty(propertiesPrefix + "cert-file"))) {
            ssh = this.obtainValidItems(ssh);
            ssh.setCertFile(environment.getProperty(propertiesPrefix + "cert-file", ClassPathResource.class));
        }

        if (StringUtils.isNotBlank(environment.getProperty(propertiesPrefix + "cert-phrase"))) {
            ssh = this.obtainValidItems(ssh);
            ssh.setCertPhrase(environment.getProperty(propertiesPrefix + "cert-phrase"));
        }

        if (StringUtils.isNotBlank(environment.getProperty(propertiesPrefix + "remote-host"))) {
            ssh = this.obtainValidItems(ssh);
            ssh.setRemoteHost(environment.getProperty(propertiesPrefix + "remote-host"));
        }

        if (environment.getProperty(propertiesPrefix + "remote-port", Integer.TYPE, -1) > 0) {
            ssh = this.obtainValidItems(ssh);
            ssh.setRemotePort(environment.getProperty(propertiesPrefix + "remote-port", Integer.TYPE, -1));
        }

        if (environment.getProperty(propertiesPrefix + "listen-port", Integer.TYPE, 0) > 0) {
            ssh = this.obtainValidItems(ssh);
            ssh.setListenPort(environment.getProperty(propertiesPrefix + "listen-port", Integer.class, 0));
        }
        if (ssh != null) {
            sshProperties.put(sshProxyType, ssh);
        }
    }

    private SSHProxyProperties.SSHProxyItems obtainValidItems(SSHProxyProperties.SSHProxyItems items) {
        if (items == null) {
            return new SSHProxyProperties.SSHProxyItems();
        }
        return items;
    }

    private int openSSHByProperties(SSHProxyProperties.SSHProxyItems ssh) {
        try {
            // 获取证书文件
            File certFile = ssh.getCertFile().getFile();
            JSch jsch = new JSch();
            jsch.setInstanceLogger(this.jschLoggerAdapter());
            if (ssh.getListenPort() <= 0) {
                // 如果没有设置，则进行自动分配
                ServerSocket so = new ServerSocket(0);
                ssh.setListenPort(so.getLocalPort());
                so.close();
            }
            // 设置通行短语
            byte[] certPhrase = ssh.getCertPhrase() == null ? null : ssh.getCertPhrase().getBytes();
            jsch.addIdentity(certFile.getAbsolutePath(), certPhrase);
            Session session = jsch.getSession(ssh.getUsername(), ssh.getHost(), ssh.getPort());
            // 设置密码
            if (StringUtils.isNotBlank(ssh.getPassword())) {
                session.setPassword(ssh.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            if (ssh.getKeepLive() > 0) {
                session.setServerAliveInterval(ssh.getKeepLive());
            }
            session.connect();
            session.setPortForwardingL(ssh.getListenPort(), ssh.getRemoteHost(), ssh.getRemotePort());
            return ssh.getListenPort();
        } catch (IOException | JSchException e) {
            throw new GloamInternalException("[GloamSSH]:ssh通道创建失败", e);
        }
    }

    private Logger jschLoggerAdapter() {
        return new Logger() {
            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void log(int leave, String message) {
                switch (leave) {
                    case 0:
                        log.debug(message);
                        break;
                    case 1:
                        log.info(message);
                        break;
                    case 2:
                        log.warn(message);
                        break;
                    case 3:
                        log.error(message);
                        break;
                    case 4:
                        log.fatal(message);
                        break;
                }
            }
        };
    }
}
