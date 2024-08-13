package com.gloamframework.web.debouncing;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.web.debouncing.annotation.Debouncing;
import com.gloamframework.web.security.match.AbstractSpringMvcPathMatcher;
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
final class DebouncingMatcher extends AbstractSpringMvcPathMatcher<Debouncing> {

    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private static final List<DebouncingPath> debouncingPaths = new ArrayList<>();
    private static boolean reversed = false;

    @AllArgsConstructor
    private static class DebouncingPath {
        private final String path;
        private final HttpMethod httpMethod;
        private final Debouncing debouncing;

        public Debouncing match(String path, String httpMethod) {
            // 路径没匹配上
            if (!pathMatcher.match(this.path, path)) {
                return null;
            }
            // 匹配方法
            if (this.httpMethod == null || StrUtil.isBlank(httpMethod)) {
                // 如果没有指定拦截方法，则表示全部
                return this.debouncing;
            }
            if (this.httpMethod.matches(httpMethod.toUpperCase())) {
                return this.debouncing;
            }
            // 没有匹配到
            return null;
        }
    }

    @Override
    protected Class<Debouncing> annotation() {
        return Debouncing.class;
    }

    @Override
    protected void registerFilter(String pathUrl, HttpMethod httpMethod, Debouncing annotation) {
        debouncingPaths.add(new DebouncingPath(pathUrl, httpMethod, annotation));
        log.debug("接口防抖保护 -> 接口:{} 请求方法:{}", pathUrl, httpMethod);
    }

    public Debouncing match(HttpServletRequest request) {
        if (request == null) {
            throw new SecurityException("匹配WebEnvelope策略，请求为获取失败");
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (!reversed) {
            CollectionUtil.reverse(debouncingPaths);
            reversed = true;
        }
        for (DebouncingPath debouncingPath : debouncingPaths) {
            Debouncing debouncing = debouncingPath.match(uri, method);
            if (debouncing != null) {
                return debouncing;
            }
        }
        return null;
    }
}
