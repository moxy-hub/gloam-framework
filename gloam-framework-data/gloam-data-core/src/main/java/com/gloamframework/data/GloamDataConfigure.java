package com.gloamframework.data;

import com.gloamframework.data.properties.GloamDataProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * gloam数据配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(GloamDataProperties.class)
@EnableTransactionManagement
public class GloamDataConfigure {

}
