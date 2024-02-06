package com.gloamframework.file.client.s3;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gloamframework.file.client.FileClientProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * s3协议文件管理配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.file.client.s3")
@Data
public class S3FileProperties implements FileClientProperties {

    public static final String ENDPOINT_QINIU = "qiniucs.com";
    public static final String ENDPOINT_ALIYUN = "aliyuncs.com";
    public static final String ENDPOINT_TENCENT = "myqcloud.com";

    /**
     * 节点地址
     * 1. MinIO：<a href="https://www.iocoder.cn/Spring-Boot/MinIO">...</a> 。例如说，http://127.0.0.1:9000
     * 2. 阿里云：<a href="https://help.aliyun.com/document_detail/31837.html">...</a>
     * 3. 腾讯云：<a href="https://cloud.tencent.com/document/product/436/6224">...</a>
     * 4. 七牛云：<a href="https://developer.qiniu.com/kodo/4088/s3-access-domainname">...</a>
     * 5. 华为云：<a href="https://developer.huaweicloud.com/endpoint?OBS">...</a>
     */
    private String endpoint;

    /**
     * 自定义域名
     * 1. MinIO：通过 Nginx 配置
     * 2. 阿里云：<a href="https://help.aliyun.com/document_detail/31836.html">...</a>
     * 3. 腾讯云：<a href="https://cloud.tencent.com/document/product/436/11142">...</a>
     * 4. 七牛云：<a href="https://developer.qiniu.com/kodo/8556/set-the-custom-source-domain-name">...</a>
     * 5. 华为云：<a href="https://support.huaweicloud.com/usermanual-obs/obs_03_0032.html">...</a>
     */
    private String domain;

    /**
     * 存储 Bucket
     */
    private String bucket;

    /**
     * 访问 Key
     * 1. MinIO：<a href="https://www.iocoder.cn/Spring-Boot/MinIO">...</a>
     * 2. 阿里云：<a href="https://ram.console.aliyun.com/manage/ak">...</a>
     * 3. 腾讯云：<a href="https://console.cloud.tencent.com/cam/capi">...</a>
     * 4. 七牛云：<a href="https://portal.qiniu.com/user/key">...</a>
     * 5. 华为云：<a href="https://support.huaweicloud.com/qs-obs/obs_qs_0005.html">...</a>
     */
    private String accessKey;

    /**
     * 访问 Secret
     */
    private String accessSecret;

    @SuppressWarnings("RedundantIfStatement")
    @JsonIgnore
    public boolean isDomainValid() {
        // 如果是七牛，必须带有 domain
        if (StrUtil.contains(endpoint, ENDPOINT_QINIU) && StrUtil.isEmpty(domain)) {
            return false;
        }
        return true;
    }

}
