package com.gloamframework.web.security.match;

import cn.hutool.core.util.ArrayUtil;
import com.gloamframework.web.security.annotation.Authentication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

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

    private static final Set<AuthenticationRequest> authenticationRequests = new HashSet<>();

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
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        authenticationRequests.forEach(req -> {
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl antMatchers = expressionInterceptUrlRegistry.antMatchers(req.httpMethod, req.pathUrl);
            // 拦截则需要认证
            antMatchers.authenticated();
            // 认证后还需要权限的判断
            if (ArrayUtil.isNotEmpty(req.authentication.hasAuth())) {
                antMatchers.hasAnyAuthority(req.authentication.hasAuth());
            } else if (ArrayUtil.isNotEmpty(req.authentication.hasRole())) {
                antMatchers.hasAnyRole(req.authentication.hasRole());
            }
            log.debug("match path:{} method:{} for authentication with authorities:{} and roles:{}", req.pathUrl, req.httpMethod, req.authentication.hasAuth(), req.authentication.hasRole());
        });
        expressionInterceptUrlRegistry.anyRequest().permitAll();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
