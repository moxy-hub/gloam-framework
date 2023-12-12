package com.gloamframework.web.security;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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

    public GloamAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        if (CollectionUtil.isNotEmpty(authorities)) {
            super.setAuthenticated(true);
        }
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
}
