package com.gloamframework.file.client.s3;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.gloamframework.file.client.AbstractFileClient;
import com.gloamframework.file.exception.FileDeleteException;
import com.gloamframework.file.exception.FileDownloadException;
import com.gloamframework.file.exception.FileUploadException;
import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.gloamframework.file.client.s3.S3FileProperties.ENDPOINT_ALIYUN;
import static com.gloamframework.file.client.s3.S3FileProperties.ENDPOINT_TENCENT;

/**
 * 基于s3协议实现的文件管理客户端
 *
 * @author 晓龙
 */
public class S3FileClient extends AbstractFileClient<S3FileProperties> {

    private static final String SPLIT = "/";
    private MinioClient client;

    public S3FileClient(String symbol, S3FileProperties properties) {
        super(symbol, properties);
    }

    @Override
    protected void startup() {
        // 补全 domain
        if (StrUtil.isEmpty(obtainProperties().getDomain())) {
            obtainProperties().setDomain(buildDomain());
        }
        // 初始化客户端
        client = MinioClient.builder()
                // Endpoint URL
                .endpoint(buildEndpointURL())
                // Region
                .region(buildRegion())
                // 认证密钥
                .credentials(obtainProperties().getAccessKey(), obtainProperties().getAccessSecret())
                .build();
    }

    @Override
    public void upload(String fileName, String relativePath, InputStream fileStream) throws FileUploadException {
        if (StrUtil.isBlank(fileName) || fileStream == null) {
            throw new FileUploadException("文件上传失败,错误的文件名或文件流");
        }
        // 获取文件类型
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (StrUtil.isNotBlank(relativePath)) {
            fileName = relativePath + SPLIT + fileName;
        }
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(this.checkBucket(obtainProperties().getBucket())).object(fileName)
                    .stream(fileStream, fileStream.available(), -1)
                    .contentType(fileType).build();
            client.putObject(objectArgs);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new FileUploadException("创建或查询存储桶失败", e);
        }
    }

    @Override
    public void delete(String path) throws FileDeleteException {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(obtainProperties().getBucket())
                    .object(path)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new FileDeleteException("文件删除失败", e);
        }
    }

    @Override
    public void download(String path, OutputStream outputStream) throws FileDownloadException {
        try {
            InputStream inputStream = client.getObject(GetObjectArgs.builder()
                    .bucket(obtainProperties().getBucket())
                    .object(path)
                    .build());
            IoUtil.copy(inputStream, outputStream);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new FileDownloadException("文件下载失败", e);
        }
    }

    /**
     * 基于 bucket + endpoint 构建访问的 Domain 地址
     *
     * @return Domain 地址
     */
    private String buildDomain() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (HttpUtil.isHttp(obtainProperties().getEndpoint()) || HttpUtil.isHttps(obtainProperties().getEndpoint())) {
            return StrUtil.format("{}/{}", obtainProperties().getEndpoint(), obtainProperties().getBucket());
        }
        // 阿里云、腾讯云、华为云都适合。七牛云比较特殊，必须有自定义域名
        return StrUtil.format("https://{}.{}", obtainProperties().getBucket(), obtainProperties().getEndpoint());
    }

    /**
     * 基于 endpoint 构建调用云服务的 URL 地址
     *
     * @return URI 地址
     */
    private String buildEndpointURL() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (HttpUtil.isHttp(obtainProperties().getEndpoint()) || HttpUtil.isHttps(obtainProperties().getEndpoint())) {
            return obtainProperties().getEndpoint();
        }
        return StrUtil.format("https://{}", obtainProperties().getEndpoint());
    }

    /**
     * 基于 bucket 构建 region 地区
     *
     * @return region 地区
     */
    private String buildRegion() {
        // 阿里云必须有 region，否则会报错
        if (obtainProperties().getEndpoint().contains(ENDPOINT_ALIYUN)) {
            return StrUtil.subBefore(obtainProperties().getEndpoint(), '.', false)
                    // 去除内网 Endpoint 的后缀
                    .replaceAll("-internal", "")
                    .replaceAll("https://", "");
        }
        // 腾讯云必须有 region，否则会报错
        if (obtainProperties().getEndpoint().contains(ENDPOINT_TENCENT)) {
            return StrUtil.subAfter(obtainProperties().getEndpoint(), ".cos.", false)
                    // 去除 Endpoint
                    .replaceAll("." + ENDPOINT_TENCENT, "");
        }
        return null;
    }

    /**
     * 检查桶是否存在
     */
    private String checkBucket(String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 检查桶是否存在
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            // 创建桶
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
        return bucket;
    }

}
