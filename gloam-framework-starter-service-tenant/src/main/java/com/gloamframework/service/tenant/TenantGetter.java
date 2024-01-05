package com.gloamframework.service.tenant;

/**
 * 业务系统实现该接口，获取用户租户
 *
 * @author 晓龙
 */
public interface TenantGetter {

    /**
     * 通过认证的用户获取租户id
     *
     * @param authUser 认证用户
     * @return 租户ID
     */
    Number obtainTenantId(Object authUser);

}
