package com.gloamframework.file.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import com.gloamframework.file.FileClientSupport;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * gloam文件系统的配置
 *
 * @author 晓龙
 */
@Data
@ConfigurationProperties("gloam.file")
public class FileProperties {

    /**
     * 主客户端设置，默认为local
     * 注意:如果在properties中和java configuration中同时设置了master，则以设置的工厂中的master为主
     */
    private FileClientSupport master = FileClientSupport.LOCAL;

    /**
     * 最大的容量为50MB，再大了应该使用分片上传功能
     */
    @MappingConfigurationProperty("spring.servlet.multipart.max-file-size")
    private DataSize maxFileSize = DataSize.ofMegabytes(50);

    /**
     * 请求最大的容量
     */
    @MappingConfigurationProperty("spring.servlet.multipart.max-request-size")
    private DataSize maxRequestSize = DataSize.ofMegabytes(100);

    /**
     * 懒加载
     */
    @MappingConfigurationProperty("spring.servlet.multipart.resolve-lazily")
    private boolean resolveLazily = true;
}
