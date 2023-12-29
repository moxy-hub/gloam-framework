package com.gloamframework.file.local;

import com.gloamframework.file.FileClientSupport;
import com.gloamframework.file.client.factory.FileClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 本地文件存储创建配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(LocalFileClientProperties.class)
public class LocalFileClientConfigure {

    @Autowired
    public void startup(FileClientFactory fileClientFactory, LocalFileClientProperties localFileClientProperties) {
        fileClientFactory.createOrUpdateFileClient(FileClientSupport.LOCAL.name(), LocalFileClient.class, localFileClientProperties);
    }

}
