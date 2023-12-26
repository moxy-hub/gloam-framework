package com.gloamframework.web.security.feign;

import cn.hutool.crypto.symmetric.AES;
import com.gloamframework.common.lang.StringUtil;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 专属feign的过滤器，对参数不进行加解密
 *
 * @author 晓龙
 */

public class FeignFilter extends GloamOncePerRequestFilter {

    private static final String FEIGN_HEADER = "feign_client";
    private static final String FEIGN_NONCE_HEADER = "feign_nonce";
    private static final AES aes = new AES("gloam675478d6df4359987cf290c6d89".getBytes());

    @Override
    public int getOrder() {
        // 在加解密之前进行
        return 1;
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String feignClient = request.getHeader(FEIGN_HEADER);
        String feignNonce = request.getHeader(FEIGN_NONCE_HEADER);
        // 检查
        if (StringUtil.isAnyBlank(feignClient, feignNonce)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 校验
        String decryptClient = aes.decryptStr(feignClient);
        if (!StringUtil.equals(decryptClient, feignNonce)) {
            throw new FeignFilterException("非法服务间调用");
        }
        try {
            FeignAttribute.NO_ENCRYPT.setAttributes(request, true);
            filterChain.doFilter(request, response);
        } finally {
            FeignAttribute.clearAll(request);
        }
    }

}
