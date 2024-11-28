package com.gloamframework.core.boot.context;

import com.gloamframework.common.error.GloamNonSupportedFunctionException;
import com.gloamframework.common.lang.function.ProcessWithRes;
import com.gloamframework.core.lang.exception.GloamIllegalArgumentException;
import com.gloamframework.core.lang.exception.GloamIllegalStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * gloam全局上下文，用于各模块对于单例对象｜数据的获取
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月27日 09:44
 */
@Slf4j
public class GloamContext {

    private static final Map<String, Object> CONTENT_BEANS = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    @EventListener
    public void contextRefreshedEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
        log.info("[Gloam]:Spring上下文工具已准备就绪 --> com.gloamframework.core.boot.context.SpringContext");
    }

    public static ApplicationContext obtainSpringContext() {
        return applicationContext;
    }

    public static <T> void put(GloamObjectKey<T> key, T bean) {
        if (Objects.isNull(key) || Objects.isNull(bean)) {
            throw new GloamNonSupportedFunctionException("[GloamContext]:Gloam上下文不支持放入的key或bean为空");
        }
        CONTENT_BEANS.put(key.getKey(), bean);
    }

    public static <T> T get(GloamObjectKey<T> key) {
        if (Objects.isNull(key)) {
            throw new GloamNonSupportedFunctionException("[GloamContext]:Gloam上下文查询的key不能为null");
        }
        Object bean = CONTENT_BEANS.get(key.getKey());
        if (!key.canTransform(bean)) {
            throw new GloamIllegalArgumentException("[GloamContext]:Key-{}存储的对象不一致与定义的类型不一致", key.getKey());
        }
        return key.transform(bean);
    }

    public static <T> T getOrDefault(GloamObjectKey<T> key, T defaultObj) {
        T obj = get(key);
        return Objects.nonNull(obj) ? obj : defaultObj;
    }

    public static <T> T getOrDefaultByFunction(GloamObjectKey<T> key, ProcessWithRes<T> processFunction) {
        T obj = get(key);
        try {
            return Objects.nonNull(obj) ? obj : processFunction.doProcess();
        } catch (Throwable e) {
            throw new GloamIllegalStateException(e);
        }
    }
}
