package com.gloamframework.data.tenant;

import com.gloamframework.data.tenant.attribute.TenantAttribute;
import com.gloamframework.web.context.WebContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 租户holder
 *
 * @author 晓龙
 */
public class TenantHolder {

    /**
     * 获取当前租户
     */
    public static Long obtainCurrentTenantId() {
        return (Long) TenantAttribute.TENANT_ID.obtain(WebContext.obtainRequest());
    }

    /**
     * 设置租户
     */
    public static void setTenantId(HttpServletRequest request, Long tenantId) {
        TenantAttribute.TENANT_ID.setAttributes(request, tenantId);
    }

    public static void setTenantId(Long tenantId) {
        setTenantId(WebContext.obtainRequest(), tenantId);
    }

    /**
     * 使用新的租户执行代码，在任务执行完后，将租户id还原
     */
    public static synchronized void executeWithReSet(Long tenantId, Runnable runnable) {
        Long currentTenantId = obtainCurrentTenantId();
        try {
            setTenantId(tenantId);
            // 执行逻辑
            runnable.run();
        } finally {
            setTenantId(currentTenantId);
        }
    }
}
