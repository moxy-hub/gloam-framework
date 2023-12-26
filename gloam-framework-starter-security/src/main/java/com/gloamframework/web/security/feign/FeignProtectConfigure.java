package com.gloamframework.web.security.feign;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * feign保护相关配置
 *
 * @author 晓龙
 */
@Configurable
public class FeignProtectConfigure {

    @Bean
    public FeignFilter feignFilter() {
        return new FeignFilter();
    }

}
