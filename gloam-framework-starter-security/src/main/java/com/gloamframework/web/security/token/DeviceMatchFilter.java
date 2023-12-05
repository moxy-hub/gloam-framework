package com.gloamframework.web.security.token;

import cn.hutool.http.useragent.UserAgent;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.token.constant.Attribute;
import com.gloamframework.web.security.token.constant.Device;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求设备映射过滤器
 *
 * @author 晓龙
 */
public class DeviceMatchFilter extends GloamOncePerRequestFilter {

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Device device;
        try {
            UserAgent userAgent = WebContext.obtainUserAgent(request);
            device = Device.match(userAgent);
        } catch (Exception e) {
            device = Device.UNKNOWN;
        }
        Attribute.setAttributes(request, Attribute.DEVICE, device);
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
