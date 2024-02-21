package com.gloamframework.web.security.token.strategy;

import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.token.AbstractCacheTokenManager;
import com.gloamframework.web.security.token.exception.TokenAuthenticateException;
import com.gloamframework.web.security.token.exception.TokenExpiredException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * token的jwt实现
 *
 * @author 晓龙
 */
public class JwtTokenManager extends AbstractCacheTokenManager {

    public JwtTokenManager(TokenProperties tokenProperties, GloamSecurityCacheManager cacheManager) {
        super(tokenProperties, cacheManager);
    }

    /**
     * 创建jwtToken
     */
    @Override
    protected String generateToken(String subject, Date expirationTime) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, this.tokenProperties.getSecret())
                .compact();
    }

    @Override
    protected String verifyToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new TokenAuthenticateException("JWT:验证token为空");
        } else {
            try {
                Claims claims = Jwts.parser().setSigningKey(this.tokenProperties.getSecret()).parseClaimsJws(accessToken).getBody();
                this.verifyTokenExpired(claims);
                return claims.getSubject();
            } catch (ExpiredJwtException jwtException) {
                throw new TokenExpiredException("token已过期", jwtException);
            }

        }
    }

    private void verifyTokenExpired(Claims claims) {
        Date currentTime = new Date();
        if (claims.getExpiration().before(currentTime)) {
            throw new TokenExpiredException("token已过期");
        }
    }

}
