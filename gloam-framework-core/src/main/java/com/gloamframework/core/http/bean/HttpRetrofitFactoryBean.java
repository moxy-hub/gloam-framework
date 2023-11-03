package com.gloamframework.core.http.bean;

import com.gloamframework.core.http.annotation.WebService;
import com.gloamframework.core.http.exception.HttpInterfaceBeanRegisterException;
import com.gloamframework.core.http.manager.RetrofitManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 使用spring的工厂bean来注册为全部的接口的代理对象
 *
 * @author 晓龙
 */
@Slf4j
public class HttpRetrofitFactoryBean<I> implements FactoryBean<I> {

    /**
     * 代理retrofit2接口
     */
    private final Class<I> retrofit2Interface;

    private final RetrofitManager retrofitManager;

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
        return retrofitManager.obtainRetrofit(webService).create(this.retrofit2Interface);
    }

    @Override
    public Class<?> getObjectType() {
        return retrofit2Interface;
    }

}
