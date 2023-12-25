package com.gloamframework.data.permission.rule.dept.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 晓龙
 */
@Data
public class DeptDataPermissionDomain {

    private Boolean all;

    private Boolean self;

    private Set<Long> deptIds;

    public DeptDataPermissionDomain() {
        this.all = false;
        this.self = false;
        this.deptIds = new HashSet<>();
    }
}
