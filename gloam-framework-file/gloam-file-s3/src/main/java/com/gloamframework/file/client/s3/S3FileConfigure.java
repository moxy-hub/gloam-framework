package com.gloamframework.file.client.s3;

import com.gloamframework.file.FileClientSupport;
import com.gloamframework.file.client.factory.FileClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(S3FileProperties.class)
public class S3FileConfigure {

    /**
     * 装配s3协议的客户端
     */
    @Autowired
    public void startup(FileClientFactory fileClientFactory, S3FileProperties s3FileProperties) {
        fileClientFactory.createOrUpdateFileClient(FileClientSupport.S3.name(), S3FileClient.class, s3FileProperties);
    }
}
