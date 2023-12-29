package com.gloamframework.file.local;

import com.gloamframework.file.client.properties.FileClientProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地存储配置
 *
 * @author 晓龙
 */
@Data
@ConfigurationProperties("gloam.file.client.local")
public class LocalFileClientProperties implements FileClientProperties {

    /**
     * 本地上传目录
     */
    private String uploadDir = "/path";

}
