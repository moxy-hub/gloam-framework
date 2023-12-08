package com.gloamframework.web.security.match;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.security.annotation.Token;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Token注解匹配
 *
 * @author 晓龙
 */
@Slf4j
public class TokenMatcher extends AbstractSpringMvcPathMatcher<Token> {

    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private static final List<TokenPath> TOKEN_PATHS = new ArrayList<>();

    @AllArgsConstructor
    private static class TokenPath {
        private final String path;
        private final HttpMethod httpMethod;
        private final Token token;

        public Token.Strategy matchStrategy(String path, String httpMethod) {
            // 路径没匹配上
            if (!pathMatcher.match(this.path, path)) {
                return null;
            }
            // 匹配方法
            if (this.httpMethod == null || StrUtil.isBlank(httpMethod)) {
                // 如果没有指定拦截方法，则表示全部
                return token.strategy();
            }
            if (this.httpMethod.matches(httpMethod.toUpperCase())) {
                return token.strategy();
            }
            // 没有匹配到
            return null;
        }
    }


    @Override
    protected Class<Token> annotation() {
        return Token.class;
    }

    @Override
    protected void registerFilter(String pathUrl, HttpMethod httpMethod, Token annotation) {
        // 将匹配路径存储下来
        TOKEN_PATHS.add(new TokenPath(pathUrl, httpMethod, annotation));
    }

    /**
     * 匹配请求的策略
     */
    public Token.Strategy matchStrategy(HttpServletRequest request) {
        if (request == null) {
            throw new SecurityException("匹配token策略，请求为获取失败");
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();
        for (TokenPath tokenPath : TOKEN_PATHS) {
            Token.Strategy strategy = tokenPath.matchStrategy(uri, method);
            if (strategy != null) {
                log.info("请求:{}#{} 执行token策略:{}", uri, method, strategy);
                return strategy;
            }
        }
        // 没有匹配到，默认need
        log.info("请求:{}#{} 执行token策略:{}", uri, method, Token.Strategy.NEED);
        return Token.Strategy.NEED;
    }
}
