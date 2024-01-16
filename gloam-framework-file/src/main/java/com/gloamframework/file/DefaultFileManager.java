package com.gloamframework.file;

import com.gloamframework.common.lang.StringUtil;
import com.gloamframework.file.client.FileClient;
import com.gloamframework.file.client.factory.FileClientFactory;
import com.gloamframework.file.client.factory.LevelFileClientFactory;
import com.gloamframework.file.exception.FileParamException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件管理的默认实现
 *
 * @author 晓龙
 */
@AllArgsConstructor
@Slf4j
final class DefaultFileManager implements FileManager {

    private static final char SPLIT = '/';
    private final FileClientFactory fileClientFactory;

    @Override
    public void upload(String fileName, String relativePath, InputStream file) {
        this.valid(fileName, relativePath, file);
        this.obtainFileClient().upload(fileName, relativePath, file);
    }

    @Override
    public void download(String fileName, String relativePath, OutputStream fileOut) {
        this.valid(fileName, relativePath, fileOut);
        this.obtainFileClient().download(this.buildPath(fileName, relativePath), fileOut);
    }

    @Override
    public byte[] download(String fileName, String relativePath) {
        this.valid(fileName, relativePath);
        return this.obtainFileClient().download(this.buildPath(fileName, relativePath));
    }

    @Override
    public void delete(String fileName, String relativePath) {
        this.valid(fileName, relativePath);
        this.obtainFileClient().delete(this.buildPath(fileName, relativePath));
    }

    private FileClient<?> obtainFileClient() {
        LevelFileClientFactory clientFactory = (LevelFileClientFactory) fileClientFactory;
        return clientFactory.getMasterFileClient();
    }

    private void valid(String fileName, String relativePath, Closeable stream) {
        valid(fileName, relativePath);
        if (stream == null) {
            throw new FileParamException("[文件服务]:文件输入流或输出流不能为空");
        }
    }

    private void valid(String fileName, String relativePath) {
        if (StringUtil.isAnyBlank(fileName, relativePath)) {
            throw new FileParamException("[文件服务]:文件名或文件路径不能为空");
        }
    }

    /**
     * 处理路径的拼接
     */
    private String buildPath(String fileName, String relativePath) {
        if (StringUtil.startWith(fileName, SPLIT)) {
            fileName = StringUtil.subSuf(fileName, 1);
        }
        if (StringUtil.endWith(relativePath, SPLIT)) {
            relativePath = StringUtil.subPre(relativePath, relativePath.length() - 1);
        }
        return relativePath + SPLIT + fileName;
    }
}
