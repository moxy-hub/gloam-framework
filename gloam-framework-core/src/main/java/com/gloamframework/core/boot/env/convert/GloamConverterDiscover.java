package com.gloamframework.core.boot.env.convert;


import cn.hutool.core.util.ArrayUtil;
import com.gloamframework.core.boot.env.convert.exception.ConverterDiscoverException;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import com.gloamframework.core.logging.GloamLog;
import org.apache.commons.logging.Log;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ConfigurableConversionService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GloamConverterDiscover {

    /**
     * gloam log
     */
    private static final Log log = GloamLog.getLogger();
    /**
     * gloam scanner
     */
    private static final ResourceScanner resourceScanner = ResourceScanner.getDefault();
    private final ConfigurableConversionService conversionService;
    private final ClassLoader classLoader;

    public GloamConverterDiscover(ConfigurableConversionService conversionService, ClassLoader classLoader) {
        this.conversionService = conversionService;
        this.classLoader = classLoader;
    }

    public void doRegister(String... packagePaths) {
        if (ArrayUtil.isEmpty(packagePaths)) {
            log.debug("no package path found to discover converter");
            return;
        }
        log.debug("start discover converter with annotation @GloamConverter in package:" + Arrays.toString(packagePaths));
        // 使用set去重
        Set<Class<?>> converterClasses = new HashSet<>();
        try {
            for (String packagePath : packagePaths) {
                converterClasses.addAll(resourceScanner.scannerForClassesWithAnnotation(packagePath, classLoader, GloamConverter.class));
            }
            for (Class<?> converterClass : converterClasses) {
                if (!GenericConverter.class.isAssignableFrom(converterClass)) {
                    log.error("类" + converterClass + "需要实现GenericConverter接口");
                    continue;
                }
                GenericConverter converter = (GenericConverter) converterClass.newInstance();
                conversionService.addConverter(converter);
                log.debug("conversionService register converter:" + converterClass);
            }
        } catch (IOException e) {
            throw new ConverterDiscoverException("发现转换器失败", e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConverterDiscoverException("实例转换器对象失败", e);
        }


    }
}
