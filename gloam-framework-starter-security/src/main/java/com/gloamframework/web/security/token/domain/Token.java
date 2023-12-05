package com.gloamframework.web.security.token.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * token实体类
 *
 * @author 晓龙
 */
@Getter
@AllArgsConstructor
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
}
