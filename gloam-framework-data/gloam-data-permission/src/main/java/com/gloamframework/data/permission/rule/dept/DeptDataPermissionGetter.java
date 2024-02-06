package com.gloamframework.data.permission.rule.dept;

import com.gloamframework.data.permission.rule.dept.domain.DeptDataPermissionDomain;

/**
 * @author 晓龙
 */
@FunctionalInterface
public interface DeptDataPermissionGetter {

    DeptDataPermissionDomain getDeptDataPermission(Object userId);

}
