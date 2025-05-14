package com.gloamframework.test.ssh.properties;

import com.gloamframework.ssh.SSHProxyType;
import com.gloamframework.ssh.properties.SSHProxyProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月12日 16:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestProperties {

    @Autowired
    private SSHProxyProperties sshProxyProperties;

    @Test
    public void test() throws IOException {
        SSHProxyProperties.SSHProxyItems sshProxyItems = sshProxyProperties.getSsh().get(SSHProxyType.JDBC);
        File file = sshProxyItems.getCertFile().getFile();
        System.out.println(file.length());
    }
}
