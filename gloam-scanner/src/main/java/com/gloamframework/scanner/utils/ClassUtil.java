package com.gloamframework.scanner.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * 操作class相关的工具类
 *
 * @author 晓龙
 */
@Slf4j
public class ClassUtil {

    private static final ClassLoader DEFAULT_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    /**
     * 加载资源
     */
    public static Class<?> loadClass(ClassLoader classLoader, Resource resource) {
        // 初始化类加载器
        if (classLoader == null) {
            classLoader = DEFAULT_CLASS_LOADER;
        }
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
