package com.gloamframework.web.security.token;

import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.token.constant.Attribute;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.exception.TokenAuthenticateException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token认证过滤器
 * @author 晓龙
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private final TokenManager tokenManager;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, TokenManager tokenManager) {
        super(authenticationManager, authenticationEntryPoint);
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 进行token认证
        boolean authRes = tokenManager.checkAuthentication((Device) request.getAttribute(Attribute.DEVICE.name()));
        if (!authRes){
            throw new TokenAuthenticateException("认证失败");
        }
        chain.doFilter(request,response);
    }

    //    @Override
//    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
//        // 进行token认证
//        boolean authRes = tokenManager.checkAuthentication((Device) request.getAttribute(Attribute.DEVICE.name()));
//        if (!authRes){
//            throw new TokenAuthenticateException("认证失败");
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return 3;
//    }
}
