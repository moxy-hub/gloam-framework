package com.gloamframework.data.tenant;

import com.gloamframework.data.tenant.annotation.TenantProtect;
import com.gloamframework.data.tenant.attribute.TenantAttribute;
import com.gloamframework.data.tenant.exception.TenantException;
import com.gloamframework.data.tenant.matcher.TenantProtectMatcher;
import com.gloamframework.web.security.GloamSecurityContext;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 晓龙
 */
public class TenantFilter extends GloamOncePerRequestFilter {

    @Autowired(required = false)
    private TenantGetter tenantGetter;

    @Autowired
    private TenantProtectMatcher tenantProtectMatcher;

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TenantProtect tenantProtect = tenantProtectMatcher.match(request);
        if (tenantProtect == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (tenantGetter == null) {
            throw new TenantException("没有找到配置的租户获取器");
        }
        // 获取认证用户
        // 这里考虑到用户的租户安 id不会变换，做缓冲进行处理，但是redis的缓冲中long取出来后会自动转为int，所以多做一步转换
        Number tenantId = tenantGetter.obtainTenantId(GloamSecurityContext.obtainAuthenticationPrincipal());
        if (tenantId == null) {
            throw new TenantException("获取租户失败");
        }
        // 把租户id放入请求中
        TenantAttribute.TENANT_ID.setAttributes(request, tenantId.longValue());
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantAttribute.clearAll(request);
        }
    }

    @Override
    public int getOrder() {
        return 5;
    }
}
