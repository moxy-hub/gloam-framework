package com.gloamframework.scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.Objects;

/**
 * 资源中心工程获取类，通过本工厂可以获取到系统默认的资源中心
 *
 * @author 晓龙
 * @see ResourceCentre
 */
public class ResourceCentreFactory {

    private static volatile ResourceCentre resourceCentre;

    /**
     * 获取系统内置的默认实现的资源中心，获取到的资源中心为单例模式
     * 在第一次获取时传入的classLoader和log有效，后续的则无效
     * <p>默认实现:{@link DefaultResourceCentre}</p>
     *
     * @param classLoader 获取spring.factory的类加载器，
     *                    默认为{@link SpringFactoriesLoader}.class.getClassLoader()
     *                    和{@link ClassLoader#getSystemClassLoader()}
     * @param log         外部传入的日志模块，主要为了方便延迟日志的实现，
     *                    默认为{@link LogFactory#getLog(String)}获取的日志
     */
    public static synchronized ResourceCentre ofSingleDefault(ClassLoader classLoader, Log log) throws IOException {
        if (Objects.isNull(resourceCentre)) {
            resourceCentre = ofDefault(classLoader, log);
        }
        return resourceCentre;
    }

    /**
     * 获取系统内置的默认实现的资源中心，获取到的资源中心为单例模式
     * <p>默认实现:{@link DefaultResourceCentre}</p>
     */
    public static ResourceCentre ofSingleDefault() throws IOException {
        return ofSingleDefault(null, null);
    }

    /**
     * 获取系统内置的默认实现的资源中心，获取到的资源中心为多例模式，每次调用都会创建新的资源中心
     * <p>
     * <b>Importance:</b>
     * 每次创建资源中心都会进行资源扫描，由于会产生额外的资源浪费，谨慎使用
     * </p>
     * <p>默认实现:{@link DefaultResourceCentre}</p>
     *
     * @param classLoader 获取spring.factory的类加载器，默认为{@link SpringFactoriesLoader}.class.getClassLoader()
     * @param log         外部传入的日志模块，主要为了方便延迟日志的实现，默认为{@link LogFactory#getLog(String)}获取的日志
     */
    public static ResourceCentre ofDefault(ClassLoader classLoader, Log log) throws IOException {
        if (Objects.isNull(log)) {
            log = LogFactory.getLog(ResourceCentreFactory.class);
        }
        return new DefaultResourceCentre(classLoader, log);
    }


}
