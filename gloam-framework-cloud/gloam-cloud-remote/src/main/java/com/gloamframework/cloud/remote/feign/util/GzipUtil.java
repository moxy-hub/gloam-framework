package com.gloamframework.cloud.remote.feign.util;

import com.gloamframework.cloud.remote.feign.exception.FeignException;
import feign.Response;
import org.springframework.cloud.openfeign.encoding.HttpEncoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.zip.GZIPInputStream;

/**
 * @author 晓龙
 */
public class GzipUtil {

    public static Response decode(Response response) {
        Collection<String> encoding = response.headers().getOrDefault(HttpEncoding.CONTENT_ENCODING_HEADER, null);
        if (encoding != null) {
            if (encoding.contains(HttpEncoding.GZIP_ENCODING)) {
                String decompressedBody;
                try {
                    decompressedBody = decompress(response);
                } catch (IOException e) {
                    throw new FeignException("feign:解压请求失败", e);
                }
                if (decompressedBody != null) {
                    response = response.toBuilder().body(decompressedBody.getBytes()).build();
                }
            }
        }
        return response;
    }

    private static String decompress(Response response) throws IOException {
        if (response.body() == null) {
            return null;
        }
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(
                response.body().asInputStream());
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8))) {
            StringBuilder outputString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputString.append(line);
            }
            return outputString.toString();
        }
    }
}
