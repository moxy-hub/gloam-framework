package com.gloamframework.core.boot.scanner;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 资源扫描器
 *
 * @author 晓龙
 */
public interface ResourceScanner {

    static ResourceScanner getDefault() {
        return PathMatchingResourceScanner.instance();
    }

    /**
     * 扫描对应的包以下，获取对应资源
     *
     * @param targetPackage          扫描包路径
     * @param targetResourceBeanName 目标资源名字
     * @param classLoader            类加载器
     * @return 全部符合名字的资源
     */
    Resource[] scannerForResource(String targetPackage, ClassLoader classLoader, String targetResourceBeanName) throws IOException;

    /**
     * 扫描对应的包以下，获取全部资源
     *
     * @param targetPackage 扫描包路径
     * @param classLoader   类加载器
     * @return 全部的资源
     */
    default Resource[] scannerForResource(String targetPackage, ClassLoader classLoader) throws IOException {
        return scannerForResource(targetPackage, classLoader, null);
    }

    /**
     * 扫描目标包以下资源，获取对应的类
     *
     * @param targetPackage          扫描包路径
     * @param targetResourceBeanName 目标资源名字
     * @param classLoader            类加载器
     * @return 全部符合的类对象
     */
    List<Class<?>> scannerForClasses(String targetPackage, ClassLoader classLoader, String targetResourceBeanName) throws IOException;

    /**
     * 扫描目标包以下资源，全部的类
     *
     * @param targetPackage 扫描包路径
     * @param classLoader   类加载器
     * @return 全部的类对象
     */
    default List<Class<?>> scannerForClasses(String targetPackage, ClassLoader classLoader) throws IOException {
        return scannerForClasses(targetPackage, classLoader, null);
    }

    List<Class<?>> scannerForClassesWithAnnotation(String targetPackage, ClassLoader classLoader, Class<? extends Annotation> annotationClass) throws IOException;

}
