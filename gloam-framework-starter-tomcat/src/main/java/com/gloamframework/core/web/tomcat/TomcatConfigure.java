package com.gloamframework.core.web.tomcat;

import com.gloamframework.core.web.tomcat.properties.TomcatProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * tomcat配置注册
 *
 * @author 晓龙
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(TomcatProperties.class)
@Configurable
public class TomcatConfigure {
}
