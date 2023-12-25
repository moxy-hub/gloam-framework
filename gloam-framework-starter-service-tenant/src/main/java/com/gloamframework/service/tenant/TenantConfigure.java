package com.gloamframework.service.tenant;

import com.gloamframework.service.tenant.matcher.TenantProtectMatcher;
import com.gloamframework.service.tenant.properties.TenantProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 多租户配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(TenantProperties.class)
public class TenantConfigure {

    @Bean
    public TenantProtectMatcher tenantProtectMatcher() {
        return new TenantProtectMatcher();
    }

    @Bean
    public TenantFilter tenantFilter() {
        return new TenantFilter();
    }

}
