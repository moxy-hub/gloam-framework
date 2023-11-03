package com.gloamframework.web.undertow.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

/**
 * undertow线程设置
 *
 * @author 晓龙
 */
@Data
public class UndertowThreadsProperties {


    /**
     * 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个读线程和一个写线程
     * 默认为cpu核心数*2
     */
    @MappingConfigurationProperty("io")
    private Integer io = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 阻塞任务线程池, 当执行类似servlet请求阻塞IO操作, undertow会从这个线程池中取得线程
     * 它的值设置取决于系统线程执行任务的阻塞系数，默认值是IO线程数*16
     */
    @MappingConfigurationProperty("worker")
    private Integer worker = io * 16;

}
