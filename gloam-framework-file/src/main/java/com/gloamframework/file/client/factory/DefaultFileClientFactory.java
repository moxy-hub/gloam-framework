package com.gloamframework.file.client.factory;

import cn.hutool.core.util.ReflectUtil;
import com.gloamframework.core.lang.Assert;
import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.properties.FileClientProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认的工厂实现
 *
 * @author 晓龙
 */
@Slf4j
class DefaultFileClientFactory implements FileClientFactory {

    /**
     * 文件客户端beans
     */
    private final ConcurrentMap<String, FileClient<? extends FileClientProperties>> clients = new ConcurrentHashMap<>();

    @Override
    public FileClient<? extends FileClientProperties> getFileClient(String symbol) {
        FileClient<? extends FileClientProperties> client = clients.get(symbol);
        if (client == null) {
            log.error("获取客户端:{}失败 -> 客户端不存在", symbol);
        }
        return client;
    }

    @Override
    public synchronized <Properties extends FileClientProperties> void createOrUpdateFileClient(String symbol, Class<? extends FileClient<Properties>> clientClass, Properties properties) {
        Assert.notBlank(symbol, "创建文件客户端失败:标识不可为空");
        @SuppressWarnings("unchecked")
        FileClient<Properties> fileClient = (FileClient<Properties>) this.clients.get(symbol);
        if (fileClient == null) {
            this.clients.put(symbol, this.createFileClient(symbol, clientClass, properties));
            return;
        }
        // 客户端存在，进行更新
        fileClient.refresh(properties);
    }

    private FileClient<? extends FileClientProperties> createFileClient(String symbol, Class<? extends FileClient<? extends FileClientProperties>> clientClass, FileClientProperties properties) {
        Assert.notNull(clientClass, "创建文件客户端失败:class不存在");
        // 创建客户端
        FileClient<? extends FileClientProperties> fileClient = ReflectUtil.newInstance(clientClass, symbol, properties);
        log.debug("文件客户端:[{}]创建成功 -> [{}]", symbol, fileClient);
        return fileClient;
    }

}
