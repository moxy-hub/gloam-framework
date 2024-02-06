package com.gloamframework.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * redis缓存增强，增加过期时间的设置
 *
 * @author 晓龙
 */
@Data
@AllArgsConstructor
public class ExpireValue {

    /**
     * 存入redis的值
     */
    private Object value;

    /**
     * 过期时间
     */
    private long expire = -1;

    public ExpireValue(Object value) {
        this.value = value;
    }
}
