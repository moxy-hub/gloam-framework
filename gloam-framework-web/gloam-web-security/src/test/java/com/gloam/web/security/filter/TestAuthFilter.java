package com.gloam.web.security.filter;

import com.gloamframework.web.security.GloamAuthenticationToken;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 晓龙
 */
@Component
public class TestAuthFilter extends GloamOncePerRequestFilter {

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("sss:ss");
        SecurityContextHolder.getContext().setAuthentication(new GloamAuthenticationToken("aa", "ss", authorities));
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
