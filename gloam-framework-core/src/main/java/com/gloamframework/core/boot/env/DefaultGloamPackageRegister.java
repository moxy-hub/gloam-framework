package com.gloamframework.core.boot.env;

/**
 * 默认的包扫描路径
 *
 * @author 晓龙
 */
public class DefaultGloamPackageRegister implements GloamPackageRegister {

    @Override
    public String[] register() {
        return new String[]{"com.gloamframework", "com.gloam"};
    }

}
