package com.gloamframework.web.security;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.security.attribute.AuthorityAttribute;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于获取请求的权限标识
 *
 * @author 晓龙
 */
public class GloamRequestAuthorityFilter extends GloamOncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(securityProperties.getAuthoritySymbolHeader());
        if (StrUtil.isBlank(header)) {
            header = "*";
        }
        AuthorityAttribute.AUTHORITY_SYMBOL.setAttributes(request, header);
        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthorityAttribute.clearAll(request);
        }
    }

}
