package com.gloamframework.core.http.bean;

import com.gloamframework.core.http.exception.HttpInterfaceBeanRegisterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import retrofit2.Retrofit;

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

    public HttpRetrofitFactoryBean(Class<I> retrofit2Interface) {
        if (!retrofit2Interface.isInterface()) {
            throw new HttpInterfaceBeanRegisterException("标注@RemoteClient的类不是接口！", "创建retrofit2代理对象失败");
        }
        this.retrofit2Interface = retrofit2Interface;
    }

    @Override
    public I getObject() throws Exception {
        log.debug("get bean:{}", this.retrofit2Interface);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
        return retrofit.create(this.retrofit2Interface);
    }

    @Override
    public Class<?> getObjectType() {
        return retrofit2Interface;
    }

}
