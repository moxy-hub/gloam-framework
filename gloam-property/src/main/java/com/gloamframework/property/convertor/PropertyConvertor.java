package com.gloamframework.property.convertor;

import com.gloamframework.property.PropertyMapperDefinition;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import org.apache.commons.logging.Log;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

/**
 * 配置映射器，由业务进行实现，对不同的配置对象进行映射
 * <h3>配置转换器</h3>
 * 执行具体到的一个配置的转化，如果存在自定义的单位对象，如Spring中的{@link org.springframework.util.unit.DataSize}对象
 * 则需要通过实现当前转换器进行处理，需要将最终的结果转化为String类型
 * [TIP]
 * <li>如果需要自定义转换器，实现了当前接口后，
 * 只需要在上面标注@{@link com.gloamframework.property.convertor.annotation.PropertyConvertorRegister}注解
 * 即可完成注册
 *
 * @author 晓龙
 */
public interface PropertyConvertor extends Ordered {

    /**
     * 转换配置
     *
     * @param originPropertyPath           目前得到的原始路径，可以继续进行修改
     * @param mappingProperPath            目前得到的映射路径，可以继续进行修改
     * @param propertyType                 配置类型
     * @param environment                  Spring环境
     * @param defaultFieldValue            默认值
     * @param mappingConfigurationProperty 配置注解
     * @param log                          日志系统
     * @return 处理好的定义集合
     */
    Set<PropertyMapperDefinition> convert(String originPropertyPath,
                                          String mappingProperPath,
                                          Class<?> propertyType,
                                          ConfigurableEnvironment environment,
                                          Object defaultFieldValue,
                                          MappingConfigurationProperty mappingConfigurationProperty,
                                          Log log);

    /**
     * 检查一个配置是否可以转换，在工厂中会调用实现类的当时方法实现，如果匹配到支持的类型，则进行使用
     *
     * @param propertyType 配置类型
     * @param environment  环境
     */
    boolean canConvert(Class<?> propertyType, ConfigurableEnvironment environment);

}
