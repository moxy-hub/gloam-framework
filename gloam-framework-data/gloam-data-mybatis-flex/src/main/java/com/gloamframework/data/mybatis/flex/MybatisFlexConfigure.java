package com.gloamframework.data.mybatis.flex;

import com.gloamframework.data.mybatis.flex.interceptor.MybatisFlexInterceptor;
import com.gloamframework.data.mybatis.flex.properties.MybatisFlexProperties;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * mybatis flex配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(MybatisFlexProperties.class)
@MapperScan({"${gloam.data.mybatis.flex.mapper-scan-package:com.wdzh.**.mapper}", "com.gloam.**.mapper"})
public class MybatisFlexConfigure implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        globalConfig.setPrintBanner(false);
    }

    @Bean
    public MybatisFlexInterceptor mybatisFlexInterceptor() {
        return new MybatisFlexInterceptor();
    }
}
