package com.gloamframework.core.logging;

import com.gloamframework.core.logging.config.LoggingConfig;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年10月31日 17:29
 */
@Configurable
@EnableConfigurationProperties(LoggingConfig.class)
public class GloamLoggingConfigure {
}
