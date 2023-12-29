package com.gloamframework.file.local;

import com.gloamframework.file.client.AbstractFileClient;
import com.gloamframework.file.exception.FileDeleteException;
import com.gloamframework.file.exception.FileDownloadException;
import com.gloamframework.file.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;

/**
 * 默认实现，本地存储客户端
 *
 * @author 晓龙
 */
@Slf4j
public class LocalFileClient extends AbstractFileClient<LocalFileClientProperties> {

    private final LocalFileClientProperties fileClientProperties;

    public LocalFileClient(String symbol, LocalFileClientProperties properties) {
        super(symbol, properties);
        this.fileClientProperties = properties;
    }

    @Override
    protected void startup() {
        log.debug("本地文件存储客户端创建成功:{}", super.symbol());
    }

    @Override
    public String upload(byte[] content, String relativePath, String type) throws FileUploadException {
        System.out.println("上传了文件");
        return null;
    }

    @Override
    public void delete(String path) throws FileDeleteException {

    }

    @Override
    public void download(String path, OutputStream outputStream) throws FileDownloadException {

    }

}
