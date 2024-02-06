package com.gloamframework.file.client.factory;

import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.FileClientProperties;

/**
 * 工厂中对客户端的优先级进行配置
 *
 * @author 晓龙
 */
public interface LevelFileClientFactory extends FileClientFactory {

    /**
     * 获取设置为主要的文件客户端
     */
    FileClient<? extends FileClientProperties> getMasterFileClient();

    /**
     * 设置对应的客户端为主客户端，该方法优先于在properties中进行设置
     */
    boolean setMaster(String symbol);

}
