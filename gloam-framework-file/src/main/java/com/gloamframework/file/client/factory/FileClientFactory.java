package com.gloamframework.file.client.factory;

import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.properties.FileClientProperties;

/**
 * @author 晓龙
 */
public interface FileClientFactory {

    /**
     * 获得指定的文件客户端
     *
     * @param symbol 客户端标识
     * @return 文件客户端
     */
    FileClient<? extends FileClientProperties> getFileClient(String symbol);


    /**
     * 创建或更新文件客户端，如果客户端存在，但是properties配置有变化，则会进行更新
     *
     * @param symbol      客户端标识
     * @param clientClass 客户端的实现类class
     * @param properties  文件配置
     */
    <Properties extends FileClientProperties> void createOrUpdateFileClient(String symbol, Class<? extends FileClient<Properties>> clientClass, Properties properties);


}
