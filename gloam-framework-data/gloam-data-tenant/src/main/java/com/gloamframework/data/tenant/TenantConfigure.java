package com.gloamframework.data.tenant;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.cache.CustomKey;
import com.gloamframework.common.error.GloamInternalException;
import com.gloamframework.data.tenant.attribute.TenantAttribute;
import com.gloamframework.data.tenant.matcher.TenantProtectMatcher;
import com.gloamframework.web.context.WebContext;
import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 多租户配置
 *
 * @author 晓龙
 */
@Configurable
public class TenantConfigure {

    @Bean
    public TenantProtectMatcher tenantProtectMatcher() {
        return new TenantProtectMatcher();
    }

    @Bean
    public TenantFilter tenantFilter() {
        return new TenantFilter();
    }

    @Bean
    public CustomKey tenantCacheKey() {
        return originalKey -> {
            Long tenantId = TenantHolder.obtainCurrentTenantId();
            if (Objects.isNull(tenantId)) {
                return originalKey;
            }
            return originalKey + StrUtil.format(":tenantId[{}]", tenantId);
        };
    }


    @Bean
    public TenantFactory tenantFactory() {
        return () -> {
            HttpServletRequest request = WebContext.obtainRequest();
            if (request == null) {
                throw new GloamInternalException("多租户查询获取请求失败");
            }
            Long tenantId = (Long) TenantAttribute.TENANT_ID.obtain(request);
            if (tenantId == null) {
                return new Object[0];
            }
            return new Object[]{tenantId};
        };

    }

}
