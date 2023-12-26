package com.gloamframework.cloud.remote.retrofit.bean;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.cloud.remote.retrofit.annotation.WebService;
import com.gloamframework.cloud.remote.retrofit.exception.HttpInterfaceBeanRegisterException;
import com.gloamframework.cloud.remote.retrofit.manager.RetrofitManager;
import com.gloamframework.common.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

/**
 * 使用spring的工厂bean来注册为全部的接口的代理对象
 *
 * @author 晓龙
 */
@Slf4j
public class HttpRetrofitFactoryBean<I> implements FactoryBean<I>, EnvironmentAware {

    private static final Template template = new Template("$");

    /**
     * 代理retrofit2接口
     */
    private final Class<I> retrofit2Interface;

    private final RetrofitManager retrofitManager;

    private Environment environment;

    public HttpRetrofitFactoryBean(Class<I> retrofit2Interface, RetrofitManager retrofitManager) {
        if (!retrofit2Interface.isInterface()) {
            throw new HttpInterfaceBeanRegisterException("标注@WebService的类不是接口！", "创建retrofit2代理对象失败");
        }
        this.retrofit2Interface = retrofit2Interface;
        this.retrofitManager = retrofitManager;
    }

    @Override
    public I getObject() {
        log.trace("create http retrofit bean:{}", this.retrofit2Interface);
        // 获取注解
        WebService webService = AnnotationUtils.findAnnotation(this.retrofit2Interface, WebService.class);
        if (webService == null) {
            throw new HttpInterfaceBeanRegisterException("获取WebService的@WebService注解失败！", "创建retrofit2代理对象失败");
        }
        // 支持spring环境中的配置获取
        String renderURL = template.render(webService.remoteURL(), valueKey -> environment.getProperty(valueKey));
        if (!StrUtil.startWithAny(renderURL, "http://", "https://")) {
            renderURL = "http://" + renderURL;
        }
        return retrofitManager.obtainRetrofit(renderURL, webService).create(this.retrofit2Interface);
    }

    @Override
    public Class<?> getObjectType() {
        return retrofit2Interface;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
