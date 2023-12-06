package com.gloamframework.cache;

import com.gloamframework.cache.dynamic.DynamicCacheConfigure;
import com.gloamframework.cache.properties.CacheProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

/**
 * cache配置类
 *
 * @author 晓龙
 */
@Configurable
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
@Import(DynamicCacheConfigure.class)
public class CacheConfigure {

}
