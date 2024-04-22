package com.gloamframework.scanner;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.scanner.annotation.GloamResource;
import com.gloamframework.scanner.utils.ClassUtil;
import org.apache.commons.logging.Log;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 默认的资源中心实现类
 *
 * @author 晓龙
 */
final class DefaultResourceCentre extends ResourcePackagesRegister implements ResourceCentre {

    private static final String DEFAULT_GROUP = "DEFAULT";

    private static final String PATTERN = "{}{}/**/*.class";

    private static final Map<String, Set<Class<?>>> resources = new ConcurrentHashMap<>();

    private final ClassLoader classLoader;

    public DefaultResourceCentre(ClassLoader classLoader, Log log) throws IOException {
        super(log);
        this.classLoader = classLoader;
        // 先执行路径注册
        doRegister(this.classLoader);
        // 进行资源扫描
        this.doScanner();
    }

    private void doScanner() throws IOException {
        // 创建路径资源处理器
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Set<String> packages = super.getPackages();
        if (logExist()) {
            log.info("[资源扫描]:启动包资源扫描,扫描包范围:" + packages);
        }
        for (String targetPackage : packages) {
            // 创建路径匹配
            String regex = StrUtil.format(PATTERN,
                    ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX,
                    ClassUtils.convertClassNameToResourcePath(targetPackage));
            for (Resource resource : resourceResolver.getResources(regex)) {
                this.addResource(resource);
            }
        }
    }

    private void addResource(Resource resource) {
        if (Objects.isNull(resource)) {
            return;
        }
        // 加载资源
        Class<?> targetClass = ClassUtil.loadClass(this.classLoader, resource);
        if (Objects.isNull(targetClass)) {
            if (logExist()) {
                log.warn("[资源扫描]:资源加载失败:" + targetClass);
            }
            return;
        }
        // 如果目标class为注解，则不进行感知
        if (Annotation.class.isAssignableFrom(targetClass)) {
            return;
        }
        // 检查资源是否为匹配的资源
        GloamResource annotation = AnnotationUtils.findAnnotation(targetClass, GloamResource.class);
        if (Objects.isNull(annotation)) {
            return;
        }
        // 获取分组参数
        String group = annotation.group();
        if (StrUtil.isBlank(group)) {
            group = DEFAULT_GROUP;
        }
        synchronized (resources) {
            Set<Class<?>> classes = resources.getOrDefault(group, new HashSet<>());
            classes.add(targetClass);
            resources.put(group, classes);
        }
        if (logExist()) {
            log.trace("[资源扫描]:扫描到资源:" + targetClass);
        }
    }

    @Override
    public Set<Class<?>> getResourcesClassesByAnnotation(String group, Class<? extends Annotation> annotationClass) {
        if (StrUtil.isBlank(group)) {
            group = DEFAULT_GROUP;
        }
        Set<Class<?>> classes = resources.get(group);
        if (CollectionUtil.isEmpty(classes)) {
            return new HashSet<>();
        }
        if (Objects.isNull(annotationClass)) {
            return classes;
        }
        return classes.stream().filter(c -> Objects.nonNull(c.getAnnotation(annotationClass)))
                .collect(Collectors.toSet());
    }
}
