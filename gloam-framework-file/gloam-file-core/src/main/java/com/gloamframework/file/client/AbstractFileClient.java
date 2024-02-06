package com.gloamframework.file.client;

import com.gloamframework.core.lang.Assert;
import com.gloamframework.file.exception.FileDownloadException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 抽象文件客户端，所有文件客户端的实现可以基于当前抽象类
 *
 * @author 晓龙
 */
@Slf4j
public abstract class AbstractFileClient<Properties extends FileClientProperties> implements FileClient<Properties> {

    /**
     * 文件客户端唯一标志
     */
    private final String symbol;

    /**
     * 文件客户端配置
     */
    private Properties properties;

    public AbstractFileClient(String symbol, Properties properties) {
        this.symbol = symbol;
        this.properties = properties;
        this.startup();
    }

    @Override
    public String symbol() {
        return this.symbol;
    }

    @Override
    public synchronized void refresh(Properties properties) {
        Assert.notNull(properties, "文件客户端更新配置失败: 配置为null");
        log.info("文件客户端:[{}] 配置已更新", this.symbol);
        this.properties = properties;
    }

    protected Properties obtainProperties() {
        return this.properties;
    }

    /**
     * 下载文件的byte数组，默认实现，如果客户端有优化写法，建议重写该方法，比如在本地存储时可以直接文件读取到byte中
     */
    public byte[] download(String path) throws FileDownloadException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            this.download(path, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new FileDownloadException(e);
        }
    }

    /**
     * 创建初始化方法
     */
    protected abstract void startup();
}
