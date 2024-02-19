package com.gloamframework.file.client.s3;

import com.gloamframework.common.lang.StringUtil;
import com.gloamframework.file.FileClientSupport;
import com.gloamframework.file.client.factory.FileClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(S3FileProperties.class)
@Slf4j
public class S3FileConfigure {

    /**
     * 装配s3协议的客户端
     */
    @Autowired
    public void startup(FileClientFactory fileClientFactory, S3FileProperties s3FileProperties) {
        if (StringUtil.isAllBlank(
                s3FileProperties.getAccessKey(),
                s3FileProperties.getAccessSecret(),
                s3FileProperties.getEndpoint(),
                s3FileProperties.getBucket()
        )) {
            log.warn("[File]: s3客户端配置参数不足,不进行初始化");
            return;
        }
        fileClientFactory.createOrUpdateFileClient(FileClientSupport.S3.name(), S3FileClient.class, s3FileProperties);
    }
}
