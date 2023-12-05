package com.gloamframework.web.security.adapter;

import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import javax.servlet.Filter;

/**
 * Spring Security Web Configure Adapter
 * 继承后可对httpSecurity进行配置，可以通过{@link #getOrder()}方法来指定适配器加载的顺序
 *
 * @author 晓龙
 */
public abstract class GloamWebSecurityConfigurerAdapter extends SecurityConfigurerAdapter<Filter, WebSecurity> implements Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

}
