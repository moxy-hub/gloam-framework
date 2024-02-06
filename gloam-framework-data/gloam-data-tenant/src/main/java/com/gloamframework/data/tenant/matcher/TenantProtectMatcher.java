package com.gloamframework.data.tenant.matcher;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.data.tenant.annotation.TenantProtect;
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
 * 多租户路径匹配
 *
 * @author 晓龙
 */
@Slf4j
public class TenantProtectMatcher extends AbstractSpringMvcPathMatcher<TenantProtect> {

    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private static final List<TenantProtectPath> tenantProtectPaths = new ArrayList<>();
    private static boolean reversed = false;

    @AllArgsConstructor
    private static class TenantProtectPath {
        private final String path;
        private final HttpMethod httpMethod;
        private final TenantProtect tenantProtect;

        public TenantProtect match(String path, String httpMethod) {
            // 路径没匹配上
            if (!pathMatcher.match(this.path, path)) {
                return null;
            }
            // 匹配方法
            if (this.httpMethod == null || StrUtil.isBlank(httpMethod)) {
                // 如果没有指定拦截方法，则表示全部
                return this.tenantProtect;
            }
            if (this.httpMethod.matches(httpMethod.toUpperCase())) {
                return this.tenantProtect;
            }
            // 没有匹配到
            return null;
        }
    }

    @Override
    protected Class<TenantProtect> annotation() {
        return TenantProtect.class;
    }

    @Override
    protected void registerFilter(String pathUrl, HttpMethod httpMethod, TenantProtect annotation) {
        tenantProtectPaths.add(new TenantProtectPath(pathUrl, httpMethod, annotation));
        log.debug("[Tenant]: 租户接口保护 -> 接口:{} 请求方法:{}", pathUrl, httpMethod);
    }

    /**
     * 匹配请求的租户保护
     */
    public TenantProtect match(HttpServletRequest request) {
        if (request == null) {
            throw new SecurityException("匹配TenantJob策略，请求为获取失败");
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (!reversed) {
            CollectionUtil.reverse(tenantProtectPaths);
            reversed = true;
        }
        for (TenantProtectPath tenantJobPaths : tenantProtectPaths) {
            TenantProtect tenantJob = tenantJobPaths.match(uri, method);
            if (tenantJob != null) {
                return tenantJob;
            }
        }
        return null;
    }
}
