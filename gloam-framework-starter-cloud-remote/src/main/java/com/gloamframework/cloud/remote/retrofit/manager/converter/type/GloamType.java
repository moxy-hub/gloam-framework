package com.gloamframework.cloud.remote.retrofit.manager.converter.type;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Type;

/**
 * java type强化
 *
 * @author 晓龙
 */
public class GloamType implements Type {

    private final String typeName;

    public GloamType(Type type) {
        this.typeName = type.getTypeName();
    }

    public boolean is(Class<?> targetClass) {
        return StrUtil.equalsIgnoreCase(this.getTypeName(), targetClass.getTypeName());
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }
}
