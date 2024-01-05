package com.gloamframework.file;

import com.gloamframework.file.sharding.domain.Chunk;

/**
 * 文件管理器，通用文件操作类
 * todo 目前第一版本暂时先不做分片上传和断点续传的功能，先保证正常的上传和下载
 *
 * @author 晓龙
 */
public interface FileManager {

    /**
     * 上传文件，上传的为文件块，如果不进行分片上传，那么当前块即为文件全部的内容
     *
     * @param chunk 文件块
     */
    void upload(Chunk chunk);

}
