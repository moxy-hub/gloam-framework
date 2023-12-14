package com.gloamframework.web.security.match;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.security.annotation.WebEnvelope;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * web 信封加密保护请求匹配
 *
 * @author 晓龙
 */
@Slf4j
public class WebEnvelopeMatcher extends AbstractSpringMvcPathMatcher<WebEnvelope> {

    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private static final List<WebEnvelopePath> WebEnvelopePaths = new ArrayList<>();
    private static boolean reversed = false;

    @AllArgsConstructor
    private static class WebEnvelopePath {
        private final String path;
        private final HttpMethod httpMethod;
        private final WebEnvelope webEnvelope;

        public WebEnvelope match(String path, String httpMethod) {
            // 路径没匹配上
            if (!pathMatcher.match(this.path, path)) {
                return null;
            }
            // 匹配方法
            if (this.httpMethod == null || StrUtil.isBlank(httpMethod)) {
                // 如果没有指定拦截方法，则表示全部
                return this.webEnvelope;
            }
            if (this.httpMethod.matches(httpMethod.toUpperCase())) {
                return this.webEnvelope;
            }
            // 没有匹配到
            return null;
        }
    }

    @Override
    protected Class<WebEnvelope> annotation() {
        return WebEnvelope.class;
    }

    @Override
    protected void registerFilter(String pathUrl, HttpMethod httpMethod, WebEnvelope annotation) {
        WebEnvelopePaths.add(new WebEnvelopePath(pathUrl, httpMethod, annotation));
        log.info("信封加密保护 -> 接口:{} 请求方法:{}", pathUrl, httpMethod);
    }

    /**
     * 匹配请求的信封加密
     */
    public WebEnvelope match(HttpServletRequest request) {
        if (request == null) {
            throw new SecurityException("匹配WebEnvelope策略，请求为获取失败");
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (!reversed) {
            CollectionUtil.reverse(WebEnvelopePaths);
            reversed = true;
        }
        for (WebEnvelopePath webEnvelopePath : WebEnvelopePaths) {
            WebEnvelope webEnvelope = webEnvelopePath.match(uri, method);
            if (webEnvelope != null) {
                return webEnvelope;
            }
        }
        return null;
    }
}
