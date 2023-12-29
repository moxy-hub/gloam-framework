package com.gloamframework.file.client.properties;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 文件客户端的配置文件，如果有需要实现的文件客户端，相关配置实现该接口即可
 * JsonTypeInfo 注解的作用，Jackson 多态
 * 1. 序列化到时数据库时，增加 @class 属性。
 * 2. 反序列化到内存对象时，通过 @class 属性，可以创建出正确的类型
 *
 * @author 晓龙
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface FileClientProperties {
}
