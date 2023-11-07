package com.gloamframework.data;

import com.gloamframework.data.properties.MybatisFlexProperties;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * gloam数据配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(MybatisFlexProperties.class)
@EnableTransactionManagement
@MapperScan("${com.gloam.data.mapper-scan-package:com.gloam.**.mapper}")
public class GloamDataConfigure implements MyBatisFlexCustomizer {
    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        globalConfig.setPrintBanner(false);
    }
}
