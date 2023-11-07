package com.gloamframework.data.mybatis.flex;

import com.gloamframework.data.mybatis.flex.properties.MybatisFlexProperties;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * mybatis flex配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(MybatisFlexProperties.class)
@MapperScan("${com.gloam.data.mybatis.flex.mapper-scan-package:com.gloam.**.mapper}")
public class MybatisFlexConfigure implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        globalConfig.setPrintBanner(false);
    }

}
