package com.gloamframework.web.security.filter;

import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Comparator;
import java.util.List;


public class GloamFilterConfigurerAdapter extends GloamHttpSecurityConfigurerAdapter {

    private final List<GloamOncePerRequestFilter> gloamOncePerRequestFilters;

    public GloamFilterConfigurerAdapter(List<GloamOncePerRequestFilter> gloamOncePerRequestFilters) {
        this.gloamOncePerRequestFilters = gloamOncePerRequestFilters;
    }

    @Override
    public void configure(HttpSecurity http) {
        // 处理排序
        gloamOncePerRequestFilters.stream().sorted(Comparator.comparingInt(Ordered::getOrder)).forEachOrdered(filter -> {
            http.addFilterBefore(filter, LogoutFilter.class);
        });
    }

    @Override
    public int getOrder() {
        // 优先级最高
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
