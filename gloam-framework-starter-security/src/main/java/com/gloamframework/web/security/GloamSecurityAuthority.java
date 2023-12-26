package com.gloamframework.web.security;

import java.util.Set;

/**
 * gloam 拉取用户权限接口，使用框架时，请实现当前接口，并放入spring中
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface GloamSecurityAuthority {

    /**
     * 获取权限接口，需要实现当前接口，实现当前方法，返回一系列的权限码，其中角色码添加前缀ROLE_<br>
     * 比如 {user:add,user:delete,ROLE_admin}
     *
     * @param switchUser 切换的用户，该参数用于实现同一用户不同角色下权限，当前值可能为null，请注意处理，该值的来源为请求头{@}
     * @return
     */
    Set<String> authorities(Object switchUser);

}
