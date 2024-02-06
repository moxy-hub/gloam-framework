package com.gloamframework.web.doc;

/**
 * http doc注册，用于添加不在扫描路径的文档
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface HttpDocRegister {

    Package packageInfo();

}
