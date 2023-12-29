package com.gloamframework.file.properties;

import com.gloamframework.file.FileClientSupport;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

}
