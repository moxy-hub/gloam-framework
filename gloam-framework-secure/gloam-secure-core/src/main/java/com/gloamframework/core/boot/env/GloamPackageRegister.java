package com.gloamframework.core.boot.env;

/**
 * gloam包扫描注册接口，通过spring.factory进行指定
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface GloamPackageRegister {

    /**
     * 实现该接口后，将需要进行包扫描的类进行注册
     */
    String[] register();
}
