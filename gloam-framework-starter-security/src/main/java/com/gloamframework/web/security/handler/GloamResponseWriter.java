package com.gloamframework.web.security.handler;

import com.alibaba.fastjson.JSON;
import com.gloamframework.web.response.Result;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 响应写出类
 *
 * @author 晓龙
 */
public abstract class GloamResponseWriter {
    private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";

    protected void writeResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        out.write(JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

}
