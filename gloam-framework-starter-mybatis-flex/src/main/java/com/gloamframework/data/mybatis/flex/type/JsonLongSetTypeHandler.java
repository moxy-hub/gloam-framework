package com.gloamframework.data.mybatis.flex.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gloamframework.core.json.JsonUtils;
import com.mybatisflex.core.handler.BaseJsonTypeHandler;

import java.util.Set;

/**
 * 在我们将字符串反序列化为 Set 并且泛型为 Long 时，如果每个元素的数值太小，会被处理成 Integer 类型，导致可能存在隐性的 BUG。
 *
 * @author 晓龙
 */
public class JsonLongSetTypeHandler extends BaseJsonTypeHandler<Object> {

    private static final TypeReference<Set<Long>> TYPE_REFERENCE = new TypeReference<Set<Long>>() {
    };

    @Override
    protected Object parseJson(String json) {
        return JsonUtils.parseObject(json, TYPE_REFERENCE);
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }
}
