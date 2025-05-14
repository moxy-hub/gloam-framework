package com.gloamframework.ssh;

/**
 * ssh代理上下文
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年05月14日 11:32
 */
public interface SSHProxyContext {

    void openSSHProxy();

    boolean existSSHProxy(int listenPort);

    Integer getSSHProxyPorts(SSHProxyType sshProxyType);

}
