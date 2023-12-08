package com.gloamframework.web.security.token;

import com.gloamframework.web.security.annotation.Token;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import com.gloamframework.web.security.token.exception.TokenAuthenticateException;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token认证过滤器
 *
 * @author 晓龙
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class TokenAuthenticationFilter extends GloamOncePerRequestFilter {

    private final TokenManager tokenManager;

    public TokenAuthenticationFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 匹配认证策略
        Token.Strategy strategy = (Token.Strategy) TokenAttribute.TOKEN_STRATEGY.obtain(request);
        switch (strategy) {
            case NONE: {
                break;
            }
            case WANT: {
                try {
                    this.authentication(request);
                    break;
                } catch (Exception ignore) {
                    // 尝试解析，不考虑成功率
                    break;
                }
            }
            default: {
                // 进行认证
                this.authentication(request);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authentication(HttpServletRequest request) {
        // 进行token认证
        boolean authRes = tokenManager.checkAuthentication((Device) request.getAttribute(TokenAttribute.DEVICE.name()));
        if (!authRes) {
            throw new TokenAuthenticateException("认证失败");
        }
        // todo 认证通过
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
