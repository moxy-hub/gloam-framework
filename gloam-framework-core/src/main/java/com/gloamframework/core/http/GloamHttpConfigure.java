package com.gloamframework.core.http;

import com.gloamframework.core.http.bean.HttpRetrofitBeanDefinitionRegistrar;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

/**
 * http服务配置类
 *
 * @author 晓龙
 */
@Configurable
@Import(HttpRetrofitBeanDefinitionRegistrar.class)
public class GloamHttpConfigure {

}
