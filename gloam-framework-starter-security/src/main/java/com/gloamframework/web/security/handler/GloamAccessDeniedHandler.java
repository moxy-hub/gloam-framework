package com.gloamframework.web.security.handler;

import com.gloamframework.web.response.WebResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 晓龙
 */
public class GloamAccessDeniedHandler extends GloamResponseWriter implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        super.writeResponse(httpServletResponse, WebResult.fail(e.getMessage()));
    }

}
