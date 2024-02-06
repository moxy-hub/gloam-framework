package com.gloamframework.data.druid;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import javax.servlet.Servlet;

/**
 * @author 晓龙
 */
@Configurable
@Slf4j
@ConditionalOnWebApplication
public class DruidConfigure {

    static boolean enableAuthentication;
    static String username;
    static String password;
    static String[] deny;
    static String[] allow;
    static boolean resetEnable;

    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet() {
        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        //设置ip白名单
        servletRegistrationBean.addInitParameter("allow", StrUtil.join(",", allow));
        //设置ip黑名单，优先级高于白名单
        servletRegistrationBean.addInitParameter("deny", StrUtil.join(",", deny));
        if (enableAuthentication) {
            //设置控制台管理用户
            servletRegistrationBean.addInitParameter("loginUsername", username);
            servletRegistrationBean.addInitParameter("loginPassword", password);
        }
        //是否可以重置数据
        servletRegistrationBean.addInitParameter("resetEnable", String.valueOf(resetEnable));
        log.info("Druid monitor started at url /druid");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> webStatFilter() {
        //创建过滤器
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        //设置过滤器过滤路径
        filterRegistrationBean.addUrlPatterns("/*");
        //忽略过滤的形式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        statFilter.setConnectionStackTraceEnable(true);
        // 慢sql监控1秒
        statFilter.setSlowSqlMillis(1000);
        return statFilter;
    }

}
