package com.gloamframework.core.http.annotation;

import com.gloamframework.core.http.manager.assembler.OkHttpClientAssembler;
import com.gloamframework.core.http.manager.converter.GloamHttpResponseConverter;
import com.gloamframework.core.http.manager.converter.convert.HttpJsonConverter;
import com.gloamframework.core.http.manager.converter.convert.HttpStreamConverter;
import com.gloamframework.core.http.peoperties.HttpProperties;
import okhttp3.Interceptor;
import org.springframework.core.annotation.AliasFor;
import retrofit2.CallAdapter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebService {

    /**
     * 远程连接地址，同remoteURL
     */
    @AliasFor("remoteURL")
    String value() default "";

    /**
     * 远程连接地址，同value，可以理解为该注解标识的接口中，全部方法的公用请求路径
     */
    @AliasFor("value")
    String remoteURL() default "";

    /*
     * ===================== OkHttpClient配置 ================================
     */

    /**
     * 指定使用的httpClient名字，请确保该client已经正确的注册在spring容器中
     * <p>
     * <li>该项不填的话，则使用默认方式，使用内置的默认客户端
     * <li>默认客户端的相关参数调节请参考{@link HttpProperties#getClient()}
     * <li>如果基础配置无法满足您的需求，则可以参考{@link OkHttpClientAssembler}来定制化您的httpClient
     *
     * @apiNote 注意：如果不指定该项，我们也会优先在spring容器中获取您注册的客户端，spring中没有后，才会使用默认客户端
     */
    String clientName() default "";

    /**
     * 是否开启单例客户端（仅在默认客户端下生效）
     * <p>
     * <li><b>true:</b>程序会在启动时，创建一个默认的okHttpClient，在每个WebService中进行使用，这将意味着您无法为每个WebService进行定制化的配置
     * <li><b>false:</b>程序会在创建本WebService时进行实例化一个Client，可以自定专属本WebService的拦截器等相关配置
     */
    boolean singletonClient() default true;

    /**
     * 配置拦截器（仅在 singletonClient=false 时生效）
     * <p>
     * 为自定义的client配置拦截器，如果为全局统一client中，本条配置时不会生效的
     */
    Class<Interceptor>[] clientInterceptors() default {};

    /**
     * 高级特性，client builder的装配器（仅在 singletonClient=false 时生效）
     * <p>通过指定装器可以在创建client时对builder进行高级配置
     *
     * @see OkHttpClientAssembler
     */
    Class<OkHttpClientAssembler>[] clientAssembler() default {};

    /*
     * ===================== Retrofit配置 ================================
     */

    /**
     * 配置转化服务，默认配置有Json转换{@link com.gloamframework.core.http.manager.converter.convert.HttpJsonConverter}和流式转换{@link com.gloamframework.core.http.manager.converter.convert.HttpStreamConverter},
     * 如果全局配置，可以在spring中注入实现了{@link GloamHttpResponseConverter}接口的bean<br/>
     * 以下配置为局部配置，如果设置了，为了防止冲突，全部的全局设置将会失效，当前webService的转换器由您完全自定义,当然指定的转换器同样需要放在spring容器中
     */
    Class<? extends GloamHttpResponseConverter>[] converters() default {HttpJsonConverter.class, HttpStreamConverter.class};

    /**
     * 适用于当前接口的调用适配器工厂，优先级比全局调用适配器工厂更高。转换器实例从Spring容器获取
     */
    Class<? extends CallAdapter.Factory>[] callAdapterFactories() default {};
}
