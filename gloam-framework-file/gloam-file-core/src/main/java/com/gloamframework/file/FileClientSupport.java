package com.gloamframework.file;

/**
 * 支持的文件系统
 *
 * @author 晓龙
 */
public enum FileClientSupport {

    /**
     * 默认内置的本地存储方式，该方式在引入file基础包默认提供
     */
    LOCAL,

    /**
     * 基于 S3 协议的文件客户端，实现 MinIO、阿里云、腾讯云、七牛云、华为云等云服务
     * 使用时需要引入依赖 gloam-framework-starter-file-s3
     */
    S3,

    /**
     * Ftp 文件客户端
     * 使用时需要引入依赖 gloam-framework-starter-file-ftp
     */
    FTP,

    /**
     * Sftp 文件客户端
     * 使用时需要引入依赖 gloam-framework-starter-file-sftp
     */
    SFTP

}
