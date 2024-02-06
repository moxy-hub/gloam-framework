package com.gloamframework.core.boot.env;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.gloamframework.core.logging.GloamLog;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * gloam自动扫包注册器
 *
 * @author 晓龙
 */
public class GloamAutoScannerPackages {

    private static final Log log = GloamLog.getLogger();
    private static final Logger logger = LoggerFactory.getLogger(GloamAutoScannerPackages.class);

    /**
     * 全部注册的包
     */
    private static final Set<String> PACKAGES = new HashSet<>();

    private static final String DEFAULT_BASE_PACKAGE = "com.gloamframework";

    private static boolean SPRING_INSTALL = false;

    static void doRegister(ClassLoader classLoader) {
        List<GloamPackageRegister> gloamPackageRegisters = SpringFactoriesLoader.loadFactories(GloamPackageRegister.class, classLoader);
        if (CollectionUtil.isEmpty(gloamPackageRegisters)) {
            log.warn("un found any package register,fallback to use default ");
            PACKAGES.add(DEFAULT_BASE_PACKAGE);
            return;
        }
        gloamPackageRegisters.forEach(register -> PACKAGES.addAll(Arrays.asList(register.register())));
        log.info("gloam start in packages:" + PACKAGES);
    }

    static void addPackage(String... packages) {
        if (ArrayUtil.isEmpty(packages)) {
            return;
        }
        PACKAGES.addAll(Arrays.asList(packages));
    }

    public static Set<String> getPackages() {
        return PACKAGES;
    }

    public static String[] getPackageArrays() {
        return PACKAGES.toArray(new String[]{});
    }

    public static synchronized void installSpring(BeanFactory beanFactory) {
        if (SPRING_INSTALL) {
            return;
        }
        if (AutoConfigurationPackages.has(beanFactory)) {
            logger.info("gloam packages register spring packages");
            PACKAGES.addAll(AutoConfigurationPackages.get(beanFactory));
        }
        SPRING_INSTALL = true;
    }
}
