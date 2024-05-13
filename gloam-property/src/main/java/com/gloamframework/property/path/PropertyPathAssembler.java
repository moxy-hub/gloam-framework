package com.gloamframework.property.path;

/**
 * 路径装配器，将原有路径进行装配，会检查映射类中是否存在嵌套的对象，如果嵌套的对象同样标注的映射注解，则会继续进行解析<br>
 * 将解析到的路径进行拼接，符合Spring配置路径的风格<br>
 * [tip]:
 * <li> 嵌套对象必须包含SpringBoot的@{@link org.springframework.boot.context.properties.NestedConfigurationProperty}注解
 * <li> 对于特殊结构，如map，collection，因为泛形擦拭的问题，无法进行深入解析，必须通过转换工厂进行处理
 *
 * @author 晓龙
 */
public interface PropertyPathAssembler {

    /**
     * 路径装配
     *
     * @param originalPath         原始路径
     * @param mappingPath          映射路径
     * @param mappingClass         映射对象
     * @param pathAnalysisAcquirer 路径感知回调方法
     */
    void assemblePath(String originalPath, String mappingPath, Class<?> mappingClass, PathAnalysisAcquirer pathAnalysisAcquirer);
}
