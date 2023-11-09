package com.gloam.web.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月09日 22:40
 */
@Slf4j
public class TestSuccennFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("成功");
        filterChain.doFilter(request,response);
    }

}
