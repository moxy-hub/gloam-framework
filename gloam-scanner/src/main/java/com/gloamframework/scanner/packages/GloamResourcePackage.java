package com.gloamframework.scanner.packages;

import com.gloamframework.scanner.ResourcePackage;

/**
 * gloam框架默认的包扫描路径
 *
 * @author 晓龙
 */
public class GloamResourcePackage implements ResourcePackage {

    @Override
    public String[] register() {
        return new String[]{"com.gloamframework"};
    }

}
