package com.gloamframework.file.client.factory;

import com.gloamframework.file.properties.FileProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * 工厂bean的注册
 *
 * @author 晓龙
 */
@Configurable
public class FileClientFactoryConfigure {

    @Bean
    public FileClientFactory fileClientFactory(FileProperties fileProperties) {
        return new DefaultLevelFileClientFactory(fileProperties);
    }

}
