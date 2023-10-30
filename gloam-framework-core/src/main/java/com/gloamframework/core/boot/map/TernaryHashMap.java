package com.gloamframework.core.boot.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

/**
 * 三元MAP
 * 一个key，两个value
 *
 * @author 晓龙
 */
public class TernaryHashMap<K, V1, V2> extends HashMap<K, TernaryHashMap.TernaryEntry<V1, V2>> {

    /**
     * 存储于父类hashMap中的value对象，可存储两个值
     *
     * @param <V1> 值1的类型
     * @param <V2> 值2的类型
     */
    @Getter
    @AllArgsConstructor
    public static class TernaryEntry<V1, V2> {
        private V1 value1;
        private V2 value2;
    }

    public TernaryEntry<V1, V2> put(K key, V1 value1, V2 value2) {
        return super.put(key, new TernaryEntry<>(value1, value2));
    }

    public V1 getFirst(K key) {
        TernaryEntry<V1, V2> ternaryEntry = this.getTernaryEntry(key);
        if (ternaryEntry == null) {
            return null;
        }
        return ternaryEntry.getValue1();
    }

    public V2 getSecond(K key) {
        TernaryEntry<V1, V2> ternaryEntry = this.getTernaryEntry(key);
        if (ternaryEntry == null) {
            return null;
        }
        return ternaryEntry.getValue2();
    }

    public TernaryEntry<V1, V2> getTernaryEntry(K key) {
        return super.get(key);
    }

}
