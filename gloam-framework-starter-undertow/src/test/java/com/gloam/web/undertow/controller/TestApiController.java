package com.gloam.web.undertow.controller;

import cn.hutool.core.codec.Base64;
import com.gloamframework.web.response.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 晓龙
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class TestApiController {

    //    @WebServiceInject
    private AuthWebService authWebService;

    @GetMapping("/auth")
    public WebResult<Model> auth() {
        return WebResult.success(authWebService.auth());
    }

    @GetMapping("/base64/file")
    public WebResult<Void> testBase64(String base64File) {
        /*
            方案一：hutool
         */
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 解析base64
            Base64.decodeToStream(base64File, outputStream, true);
            // 上传
            // upload(outputStream); // 上传好像是input一般，这估计要转以下
        } catch (IOException e) {
            log.error("流错误", e);
        }
         /*
            方案二：hutool
         */
        byte[] dataByte = Base64.decode(base64File);
        // 上传 upload(new ByteArrayInputStream(dataByte))

        /*
            方案三：hutool
         */
        // java的base64
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        byte[] dataByte1 = decoder.decode(base64File);
        // 上传 upload(new ByteArrayInputStream(dataByte))

        return WebResult.success();
    }
}
