package com.gloamframework.web.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * gloam认证token
 *
 * @author 晓龙
 */
@Getter
public class GloamAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -4694770549653377759L;
    /**
     * 认证的信息存储
     */
    private final Object principal;
    /**
     * 认证凭证
     */
    private Object credentials;

    // ========== 上下文 ==========
    /**
     * 上下文字段，不进行持久化
     * 1. 用于基于 GloamAuthenticationToken 维度的临时缓存
     */
    @JsonIgnore
    private Map<String, Object> context;

    public GloamAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        if (CollectionUtil.isNotEmpty(authorities)) {
            super.setAuthenticated(true);
            context = new HashMap<>();
        }
    }

    public void setContext(String key, Object value) {
        if (context == null) {
            context = new HashMap<>();
        }
        context.put(key, value);
    }

    public <T> T getContext(String key, Class<T> type) {
        return MapUtil.get(context, key, type);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    @Override
    public String toString() {
        return "GloamAuthenticationToken{" +
                "principal=" + principal +
                ", credentials=" + credentials +
                '}';
    }
}
