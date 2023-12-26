package com.gloamframework.web.security.token.domain;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * token实体类
 *
 * @author 晓龙
 */
@Data
@NoArgsConstructor
public class Token {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 到期时间
     */
    private Date expiration;

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
