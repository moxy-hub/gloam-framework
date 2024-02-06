package com.gloamframework.web.security.match;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.security.annotation.Authentication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 对认证注解的支持
 *
 * @author 晓龙
 */
@Slf4j
public class AuthenticationMatcher extends AbstractSpringMvcPathMatcher<Authentication> {

    @Data
    @AllArgsConstructor
    private static class AuthenticationRequest {
        private String pathUrl;
        private HttpMethod httpMethod;
        private Authentication authentication;

        public Authentication.TokenStrategy matchTokenStrategy(String path, String httpMethod) {
            // 路径没匹配上
            if (!pathMatcher.match(this.pathUrl, path)) {
                return null;
            }
            // 匹配方法
            if (this.httpMethod == null || StrUtil.isBlank(httpMethod)) {
                // 如果没有指定拦截方法，则表示全部
                return authentication.tokenStrategy();
            }
            if (this.httpMethod.matches(httpMethod.toUpperCase())) {
                return authentication.tokenStrategy();
            }
            // 没有匹配到
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthenticationRequest that = (AuthenticationRequest) o;

            if (!pathUrl.equals(that.pathUrl)) return false;
            return httpMethod == that.httpMethod;
        }

        @Override
        public int hashCode() {
            int result = pathUrl.hashCode();
            result = 31 * result + (httpMethod == null ? "" : httpMethod).hashCode();
            return result;
        }
    }

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    private static Set<AuthenticationRequest> authenticationRequests = new HashSet<>();

    @Override
    protected Class<Authentication> annotation() {
        return Authentication.class;
    }

    @Override
    protected void registerFilter(String pathUrl, HttpMethod httpMethod, Authentication annotation) {
        // 处理匹配到的路径
        authenticationRequests.add(new AuthenticationRequest(pathUrl, httpMethod, annotation));
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        // 反转set
        ArrayList<AuthenticationRequest> authenticationRequestList = new ArrayList<>(authenticationRequests);
        Collections.reverse(authenticationRequestList);
        authenticationRequests = new HashSet<>(authenticationRequestList);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        authenticationRequests.forEach(req -> {
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl antMatchers = expressionInterceptUrlRegistry.antMatchers(req.httpMethod, req.pathUrl);
            // 对于请求为需要token的策略，不进行认证拦截
            if (req.authentication.tokenStrategy().equals(Authentication.TokenStrategy.NEED)) {
                // 拦截则需要认证
                antMatchers.authenticated();
            }
            // 认证后还需要权限的判断
            if (ArrayUtil.isNotEmpty(req.authentication.hasAuth())) {
                antMatchers.hasAnyAuthority(req.authentication.hasAuth());
            } else if (ArrayUtil.isNotEmpty(req.authentication.hasRole())) {
                antMatchers.hasAnyRole(req.authentication.hasRole());
            }
            log.debug("Match path:{} method:{} for authentication with authorities:{} and roles:{}", req.pathUrl, req.httpMethod, req.authentication.hasAuth(), req.authentication.hasRole());
        });
        expressionInterceptUrlRegistry.anyRequest().permitAll();
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 匹配请求的策略
     */
    public Authentication.TokenStrategy matchTokenStrategy(HttpServletRequest request) {
        if (request == null) {
            throw new SecurityException("匹配token策略，请求为获取失败");
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();

        for (AuthenticationRequest authenticationRequest : authenticationRequests) {
            Authentication.TokenStrategy strategy = authenticationRequest.matchTokenStrategy(uri, method);
            if (strategy != null) {
                log.debug("请求:{} # {} 执行token策略:{}", method, request.getRequestURL(), strategy);
                return strategy;
            }
        }
        // 没有匹配到，默认need
        log.debug("请求:{} # {} 未匹配到token保护策略,不进行拦截", method, request.getRequestURL());
        return null;
    }
}
