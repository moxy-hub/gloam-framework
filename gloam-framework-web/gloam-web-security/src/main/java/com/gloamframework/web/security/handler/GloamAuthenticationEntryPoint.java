package com.gloamframework.web.security.handler;

import com.gloamframework.web.response.WebResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证异常处理
 *
 * @author 晓龙
 */
public class GloamAuthenticationEntryPoint extends GloamResponseWriter implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        super.writeResponse(httpServletResponse, WebResult.fail(e.getMessage()));
    }

}
