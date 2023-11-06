package com.gloamframework.core.http.manager.converter.convert;

import com.gloamframework.core.http.manager.converter.GloamHttpResponseConverter;
import com.gloamframework.core.http.manager.converter.exception.GloamHttpConvertException;
import com.gloamframework.core.http.manager.converter.type.GloamType;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 默认支持流接受
 *
 * @author 晓龙
 */
public class HttpStreamConverter implements GloamHttpResponseConverter<ByteArrayInputStream> {

    private static final MediaType[] supportMediaType;

    static {
        supportMediaType = GloamHttpResponseConverter.parse(
                // 图片类型
                "image/gif", "image/png", "image/jpeg", "image/bmp", "image/webp", "image/x-icon", "image/vnd.microsoft.icon",
                // 音频类型
                "audio/midi", "audio/mpeg", "audio/webm", "audio/ogg", "audio/wav",
                // 视频类型
                "video/webm", "video/ogg",
                // 二进制类型
                "application/octet-stream", "application/pkcs12", "application/vnd.mspowerpoint",
                // 文件类型
                "multipart/form-data"
        );

    }

    @Override
    public MediaType[] contentTypeSupport() {
        return supportMediaType;
    }

    @Override
    public ByteArrayInputStream convert(ResponseBody responseBody, Type type) throws IOException {
        if (!new GloamType(type).is(ByteArrayInputStream.class)) {
            throw new GloamHttpConvertException("该响应需要使用流失对象进行接受，请使用ByteArrayInputStream");
        }
        byte[] byteDate = Objects.requireNonNull(Objects.requireNonNull(responseBody).bytes());
        return new ByteArrayInputStream(byteDate);
    }

}
