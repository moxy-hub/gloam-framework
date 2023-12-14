package com.gloamframework.web.security.envelope.wrapper;

import org.springframework.http.HttpHeaders;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * 响应封装类
 */
public class WebEnvelopeResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream buffer;
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public WebEnvelopeResponseWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
        super(response);
        // 存储数据的流
        buffer = new ByteArrayOutputStream();
        outputStream = new WrapperOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
        super.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        super.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * 重载父类获取flushBuffer的方法
     */
    @Override
    public void flushBuffer() throws IOException {
        if (outputStream != null) {
            outputStream.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    /**
     * 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据
     */
    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    /**
     * 内部类，对ServletOutputStream进行包装
     */
    private static class WrapperOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream bos;

        public WrapperOutputStream(ByteArrayOutputStream stream) {
            bos = stream;
        }

        @Override
        public void write(int b) {
            bos.write(b);
        }

        @Override
        public void write(byte[] b) {
            bos.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }
}
