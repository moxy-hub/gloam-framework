package com.gloamframework.web.security.token.domain;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;

import java.util.Date;

/**
 * token实体类
 *
 * @author 晓龙
 */
@Getter
public class Token {

    /**
     * 访问令牌
     */
    private final String accessToken;

    /**
     * 刷新令牌
     */
    private final String refreshToken;

    /**
     * 到期时间
     */
    private final Date expiration;

    public Token(String accessToken, String refreshToken, Date expiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        if (expiration == null) {
            this.expiration = DateUtil.yesterday();
        } else {
            this.expiration = expiration;
        }
    }
}
