package com.gloamframework.data.mybatis.flex;

import com.gloamframework.core.exception.GloamRuntimeException;
import com.gloamframework.data.mybatis.flex.interceptor.MybatisFlexInterceptor;
import com.gloamframework.data.mybatis.flex.properties.MybatisFlexProperties;
import com.gloamframework.web.context.WebContext;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.tenant.TenantFactory;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;

/**
 * mybatis flex配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(MybatisFlexProperties.class)
@MapperScan({"${gloam.data.mybatis.flex.mapper-scan-package:com.wdzh.**.mapper}","com.gloam.**.mapper"})
public class MybatisFlexConfigure implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        globalConfig.setPrintBanner(false);
    }

    @Bean
    public TenantFactory tenantFactory() {
        return () -> {
            HttpServletRequest request = WebContext.obtainRequest();
            if (request == null) {
                throw new GloamRuntimeException("多租户查询获取请求失败");
            }
            Long tenantId = (Long) request.getAttribute("TENANT_ID");
            if (tenantId == null) {
                return new Object[0];
            }
            return new Object[]{tenantId};
        };

    }

    @Bean
    public MybatisFlexInterceptor mybatisFlexInterceptor() {
        return new MybatisFlexInterceptor();
    }
}
