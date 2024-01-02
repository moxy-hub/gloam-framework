package com.gloamframework.file.client;

import com.gloamframework.file.client.properties.FileClientProperties;
import com.gloamframework.file.exception.FileDeleteException;
import com.gloamframework.file.exception.FileDownloadException;
import com.gloamframework.file.exception.FileUploadException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件客户端
 *
 * @author 晓龙
 */
public interface FileClient<Properties extends FileClientProperties> {

    /**
     * 文件客户端的唯一标识,用于识别client的唯一性
     */
    String symbol();

    /**
     * 刷新客户端配置
     */
    void refresh(Properties properties);

    /**
     * 上传文件
     *
     * @param fileStream   文件流
     * @param relativePath 相对路径
     * @return 完整路径，即 HTTP 访问地址
     * @throws FileUploadException 上传文件时，抛出 Exception 异常
     */
    String upload(InputStream fileStream, String relativePath, String fileName) throws FileUploadException;

    /**
     * 删除文件
     *
     * @param path 相对路径
     * @throws FileDeleteException 删除文件时，抛出 Exception 异常
     */
    void delete(String path) throws FileDeleteException;

    /**
     * 将文件下载为流的形式输出
     *
     * @param path 相对路径
     */
    void download(String path, OutputStream outputStream) throws FileDownloadException;

    /**
     * 下载文件的byte数组
     */
    byte[] download(String path) throws FileDownloadException;

}
