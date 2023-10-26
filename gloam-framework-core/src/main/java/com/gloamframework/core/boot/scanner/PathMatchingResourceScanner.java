package com.gloamframework.core.boot.scanner;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于路径匹配实现的资源扫描器
 *
 * @author 晓龙
 */
@Slf4j
final class PathMatchingResourceScanner implements ResourceScanner {

    private static final PathMatchingResourceScanner pathMatchingResourceScanner = new PathMatchingResourceScanner();

    private static final String PATTERN = "{}{}/**/{}.class";

    private PathMatchingResourceScanner() {
    }

    @SuppressWarnings("unused")
    static PathMatchingResourceScanner instance() {
        return pathMatchingResourceScanner;
    }

    @Override
    public Resource[] scannerForResource(String targetPackage, ClassLoader classLoader, String targetResourceBeanName) throws IOException {
        // 初始化类加载器
        if (classLoader == null) {
            log.debug("资源扫描时未传入指定类加载器,使用默认类加载器");
            classLoader = this.getClass().getClassLoader();
        }
        // 初始化扫描目标对象名称
        targetResourceBeanName = StrUtil.isBlank(targetResourceBeanName) ? "*" : targetResourceBeanName;
        // 创建路径资源处理器
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(classLoader);
        // 创建路径匹配
        String regex = StrUtil.format(PATTERN,
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX,
                ClassUtils.convertClassNameToResourcePath(targetPackage),
                targetResourceBeanName);
        log.debug("启动包资源扫描,目标beanName:{},扫描包范围:{}", targetResourceBeanName, targetPackage);
        return resourceResolver.getResources(regex);
    }

    @Override
    public List<Class<?>> scannerForClasses(String targetPackage, ClassLoader classLoader, String targetResourceBeanName) throws IOException {
        // 初始化类加载器
        if (classLoader == null) {
            log.debug("资源扫描时未传入指定类加载器,使用默认类加载器");
            classLoader = this.getClass().getClassLoader();
        }
        List<Class<?>> foundClasses = new ArrayList<>();
        Resource[] resources = this.scannerForResource(targetPackage, classLoader, targetResourceBeanName);
        for (Resource resource : resources) {
            Class<?> clazz = loadClass(classLoader, resource);
            if (clazz == null) {
                continue;
            }
            log.debug("扫描到资源文件:{},目标位置:{}", clazz.getSimpleName(), clazz.getName());
            foundClasses.add(clazz);
        }
        return foundClasses;
    }

    @Override
    public List<Class<?>> scannerForClassesWithAnnotation(String targetPackage, ClassLoader classLoader, Class<? extends Annotation> annotationClass) throws IOException {
        return this.scannerForClasses(targetPackage, classLoader, null)
                .stream()
                .filter(c -> c.getAnnotation(annotationClass) != null)
                .collect(Collectors.toList());
    }

    private Class<?> loadClass(ClassLoader classLoader, Resource resource) {
        try {
            MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(classLoader);
            MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
            return ClassUtils.forName(reader.getClassMetadata().getClassName(), classLoader);
        } catch (ClassNotFoundException | LinkageError ex) {
            log.error("Ignoring candidate class resource:{} due to ", resource, ex);
            return null;
        } catch (Throwable ex) {
            log.warn("Unexpected failure when loading class resource:{} ", resource, ex);
            return null;
        }
    }

}
