package com.gloamframework.file;

import com.gloamframework.file.client.factory.FileClientFactoryConfigure;
import com.gloamframework.file.local.LocalFileClientConfigure;
import com.gloamframework.file.properties.FileProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * 文件系统配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(FileProperties.class)
@Import({FileClientFactoryConfigure.class, LocalFileClientConfigure.class})
public class FileConfigure {
}
