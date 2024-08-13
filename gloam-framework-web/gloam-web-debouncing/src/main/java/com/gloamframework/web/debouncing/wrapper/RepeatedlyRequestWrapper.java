package com.gloamframework.web.debouncing.wrapper;

import lombok.Cleanup;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author 晓龙
 * 可重复读取inputStream的request
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public RepeatedlyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 读取body
        body = this.getBodyString(request);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getBodyString() {
        return this.body;
    }

    @Override
    public ServletInputStream getInputStream() {
        byte[] bodyArray = body.getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream bais = new ByteArrayInputStream(bodyArray);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public int available() {
                return bodyArray.length;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    private String getBodyString(ServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        @Cleanup InputStream inputStream = request.getInputStream();
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
