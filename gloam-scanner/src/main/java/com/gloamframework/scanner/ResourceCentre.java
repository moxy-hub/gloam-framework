package com.gloamframework.scanner;

import com.gloamframework.scanner.annotation.GloamResource;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 资源中心，可以获取到系统中标注了@{@link GloamResource}注解的资源或类
 * <p>获取本接口，可以通过工厂类获取 {@link ResourceCentreFactory}</p>
 *
 * @author 晓龙
 */
public interface ResourceCentre {

    /**
     * 获取到系统中的标注的@{@link GloamResource}注解和指定的注解的资源，并将资源加载为class
     * <p>Tip:
     * <li>查询的资源全部都为class，使用本接口不会获取到class之外的资源</li>
     * <li>如果没有传入类加载器，则会使用系统默认的类加载器：{@link ClassLoader#getSystemClassLoader()}</li>
     * </p>
     *
     * @param group           获取的资源分组，不传入则为默认的DEFAULT，需要和@GloamResource注解的group字段对应
     * @param annotationClass 资源class上绑定的其他注解
     * @see #getResourcesClasses(String)
     */
    Set<Class<?>> getResourcesClassesByAnnotation(String group, Class<? extends Annotation> annotationClass);

    /**
     * 获取到系统中的标注的@{@link GloamResource}注解的资源，并将资源加载为class
     * <p>Tip:
     * <li>查询的资源全部都为class，使用本接口不会获取到class之外的资源</li>
     * <li>如果没有传入类加载器，则会使用系统默认的类加载器：{@link ClassLoader#getSystemClassLoader()}</li>
     * </p>
     *
     * @param group 获取的资源分组，不传入则为默认的DEFAULT，需要和@GloamResource注解的group字段对应
     */
    default Set<Class<?>> getResourcesClasses(String group) {
        return this.getResourcesClassesByAnnotation(group, null);
    }

}
