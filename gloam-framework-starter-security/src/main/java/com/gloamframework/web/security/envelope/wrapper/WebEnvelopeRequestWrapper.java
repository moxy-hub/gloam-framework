package com.gloamframework.web.security.envelope.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 请求封装类
 */
public class WebEnvelopeRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public WebEnvelopeRequestWrapper(HttpServletRequest request, String body) {
        super(request);
        this.body = body;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(this.body));
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private final InputStream input = new ByteArrayInputStream(body.getBytes(getCharacterEncoding()));

            @Override
            public int read() throws IOException {
                return input.read();
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

}
