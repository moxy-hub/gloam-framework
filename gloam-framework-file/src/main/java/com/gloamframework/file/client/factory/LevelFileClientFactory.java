package com.gloamframework.file.client.factory;

import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.properties.FileClientProperties;

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

    boolean setMaster(String symbol);

}
