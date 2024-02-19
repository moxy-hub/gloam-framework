package com.gloamframework.cache;

/**
 * 自定义缓冲key
 *
 * @author 晓龙
 */
public interface CustomKey {

    String key(String originalKey);

}
