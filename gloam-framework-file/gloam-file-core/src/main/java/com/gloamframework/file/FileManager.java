package com.gloamframework.file;

import com.gloamframework.common.error.GloamNonSupportedFunctionException;
import com.gloamframework.file.sharding.domain.Chunk;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <h2>文件管理器，通用文件操作类</h2>
 * <p>todo 目前第一版本暂时先不做分片上传和断点续传的功能，先保证正常的上传和下载<p/>
 * <p>
 * 注意：
 * <li>文件服务不会对文件的名字进行处理，如果为了防止重复，请在调用时处理名字
 * <li>建议文件名字可以流向前端，但是文件的保存地址应该由后端保存不进行公开，防止第三方直接通过文件服务访问到服务器其他的资源
 * </p>
 *
 * @author 晓龙
 */
public interface FileManager {

    /**
     * todo 分片上传文件，上传的为文件块，如果不进行分片上传，那么当前块即为文件全部的内容
     *
     * @param chunk 文件块
     */
    default void upload(Chunk chunk) {
        throw new GloamNonSupportedFunctionException("当前版本不支持分片上传功能");
    }

    /**
     * 分片上传合并功能
     *
     * @param fileName           文件名
     * @param relativePath       文件保存地址
     * @param identificationHash 文件标识符，用于获取对应的碎片
     */
    default void merge(String fileName, String relativePath, String identificationHash) {
        throw new GloamNonSupportedFunctionException("当前版本不支持分片上传功能");
    }

    /**
     * 文件上传功能
     *
     * @param fileName     文件名
     * @param relativePath 文件保存路径
     * @param file         文件流
     */
    void upload(String fileName, String relativePath, InputStream file);

    /**
     * 文件下载功能
     *
     * @param fileName     文件名
     * @param relativePath 文件保存路径
     * @param fileOut      文件输出流
     */
    void download(String fileName, String relativePath, OutputStream fileOut);

    /**
     * 文件下载功能
     *
     * @param fileName     文件名
     * @param relativePath 文件保存路径
     * @return byte数组
     */
    byte[] download(String fileName, String relativePath);

    /**
     * 文件删除
     *
     * @param fileName     文件名
     * @param relativePath 文件保存路径
     */
    void delete(String fileName, String relativePath);
}
