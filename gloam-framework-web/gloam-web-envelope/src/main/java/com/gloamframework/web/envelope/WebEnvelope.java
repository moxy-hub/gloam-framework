package com.gloamframework.web.envelope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 使用当前注解标识的接口，表示使用网络信封加密方式，对请求body中的信息进行加密，对应的响应也将会使用密钥进行加密
 * 注意，如果加在类上，请确保该类下的全部接口的数据在body体内
 *
 * @author 晓龙
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface WebEnvelope {
}
