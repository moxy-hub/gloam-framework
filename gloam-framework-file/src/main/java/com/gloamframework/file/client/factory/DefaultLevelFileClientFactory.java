package com.gloamframework.file.client.factory;

import cn.hutool.core.util.ObjectUtil;
import com.gloamframework.core.lang.Assert;
import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.properties.FileClientProperties;
import com.gloamframework.file.properties.FileProperties;

/**
 * 处理文件客户端的主次问题
 *
 * @author 晓龙
 */
class DefaultLevelFileClientFactory extends DefaultFileClientFactory implements LevelFileClientFactory {

    private volatile String currentMaster;

    private final FileProperties fileProperties;

    DefaultLevelFileClientFactory(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @Override
    public FileClient<? extends FileClientProperties> getMasterFileClient() {
        return getFileClient(getMasterSymbol());
    }

    @Override
    public boolean setMaster(String symbol) {
        Assert.notBlank(symbol, "文件客户度:设置主客户端失败 -> 标志为空");
        this.currentMaster = symbol;
        return true;
    }

    /**
     * 选择master策略
     */
    private synchronized String getMasterSymbol() {
        // 优先工厂中设置的，然后配置中的
        String master = ObjectUtil.defaultIfBlank(this.currentMaster, fileProperties.getMaster().name());
        Assert.notBlank(master, "文件客户端:没有找到设置的主客户端,请检查相关配置");
        return master;
    }

}
