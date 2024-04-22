
package com.gloamframework.scanner;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import org.apache.commons.logging.Log;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.*;

/**
 * gloam自动扫包注册器
 * <p>
 * 会获取到全部注册在spring.factory中的{@link ResourcePackage}实现类,
 * 然后将注册的包路径进行注册，会在对应的路径中寻找资源
 * </p>
 *
 * @author 晓龙
 */
abstract class ResourcePackagesRegister {

    protected final Log log;

    /**
     * 全部注册的包
     */
    private static final Set<String> PACKAGES = new HashSet<>();

    private static final String DEFAULT_BASE_PACKAGE = "com.gloamframework";

    /**
     * 在实例化时传入log对象，这里考虑到使用留影的log，在env之前打印
     */
    ResourcePackagesRegister(Log log) {
        this.log = log;
    }

    protected void doRegister(ClassLoader classLoader) {
        List<ResourcePackage> resourcePackages = SpringFactoriesLoader.loadFactories(ResourcePackage.class, classLoader);
        if (CollectionUtil.isEmpty(resourcePackages)) {
            if (logExist()) {
                log.warn("un found any package register,fallback to use default ");
            }
            PACKAGES.add(DEFAULT_BASE_PACKAGE);
            return;
        }
        resourcePackages.forEach(register -> PACKAGES.addAll(Arrays.asList(register.register())));
    }

    public static void registerPackages(String... packages) {
        if (ArrayUtil.isEmpty(packages)) {
            return;
        }
        PACKAGES.addAll(Arrays.asList(packages));
    }

    protected Set<String> getPackages() {
        return PACKAGES;
    }

    protected String[] getPackageArrays() {
        return PACKAGES.toArray(new String[]{});
    }

    protected boolean logExist() {
        return Objects.nonNull(log);
    }

}
