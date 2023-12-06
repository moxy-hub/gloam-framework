package com.gloamframework.web.security.token;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.token.constant.Attribute;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.domain.Token;
import com.gloamframework.web.security.token.exception.TicketGenerateException;
import com.gloamframework.web.security.token.exception.TokenGenerateException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * token 实现的抽象类
 * @author 晓龙
 */
@Slf4j
public abstract class AbstractTokenManager implements TokenManager {

    /**
     * token的缓冲信息
     */
    @Data
    @Accessors(chain = true)
    private final static class TokenInfo{
        /**
         * token主体信息
         */
        private Token token;

        /**
         * token有效次数
         */
        private int validCount;

        /**
         * token设备
         */
        private Device device;

        /**
         * token是否被踢出
         */
        private boolean kickOff = false;
    }

    private static final String TICKET_SUBJECT = "GLOAM_TICKET_#NO#_{}/{}";
    private final TokenProperties tokenProperties;
    private final GloamSecurityCacheManager cacheManager;

    protected AbstractTokenManager(TokenProperties tokenProperties, GloamSecurityCacheManager cacheManager) {
        this.tokenProperties = tokenProperties;
        this.cacheManager = cacheManager;
    }

    /**
     * 生成票据，可以作为临时访问token
     */
    @Override
    public void ticket(String subject,Device device) {
        if (StrUtil.isBlank(subject)){
            throw new TicketGenerateException("票据生成失败:没有指定token的主题");
        }
        // 生成票据主题
        String ticketSubject = StrUtil.format(TICKET_SUBJECT, subject,System.currentTimeMillis());
        // 生成有效时间
        long validTime = System.currentTimeMillis() + tokenProperties.getTicket().getValidTime();
        Date expiration = new Date(validTime);
        // 生成票据
        String ticket = this.generateToken(ticketSubject, expiration);
        log.info("生成临时通行票据:{}",ticket);
        Token ticketToken = new Token(ticket, "", expiration);
        // 缓冲存储
        TokenInfo tokenInfo = new TokenInfo().setToken(ticketToken).setDevice(device).setValidCount(tokenProperties.getTicket().getValidCount());
        cacheManager.getCache().put("ticket:"+ticket,tokenInfo);
        // 写入响应头
        HttpServletResponse response = WebContext.obtainResponse();
        if (response==null){
            log.error("票据写入时获取当前响应失败");
            throw new TicketGenerateException("票据生成失败");
        }
        response.setHeader(tokenProperties.getTicket().getHeader(),JSON.toJSONString(ticketToken));
    }

    @Override
    public void authenticate(String subject, Device device) {
        if (StrUtil.isBlank(subject)){
            throw new TokenGenerateException("Token生成失败:没有指定token的主题");
        }
        Date currentTime = new Date();
        Date tokenExpirationTime = DateUtils.addSeconds(currentTime, (int)this.tokenProperties.getAccessTokenExpire());
        Date refreshTokenExpirationTime = DateUtils.addSeconds(currentTime, (int)this.tokenProperties.getRefreshTokenExpire());
        String accessToken = this.generateToken(subject, tokenExpirationTime);
        String refreshToken = this.generateToken(subject, refreshTokenExpirationTime);
        Token token = new Token(accessToken, refreshToken, tokenExpirationTime);
        // 缓冲存储
        TokenInfo tokenInfo = new TokenInfo().setToken(token).setDevice(device).setValidCount(-1);
        cacheManager.getCache().put("token:"+accessToken,tokenInfo);
        // 写入响应头
        HttpServletResponse response = WebContext.obtainResponse();
        if (response==null){
            log.error("token写入时获取当前响应失败");
            throw new TokenGenerateException("Token生成失败");
        }
        response.setHeader(tokenProperties.getTokenHeader(), JSON.toJSONString(token));
    }

    @Override
    public boolean checkAuthentication(Device device) {
        // 先检查票据，如果有票据，则先使用票据
        // 如果票据失效，检查是否存在token
        // 如果token失效，进行刷新
        // 销毁之前的token，重写认证token
        return false;
    }

    @Override
    public void revoke(String subject, Device device) {
        // 查询缓冲中的token，将token进行移除
    }

    @Override
    public void kickOff(String subject, Device device) {
        // 查询缓冲中的token，将token标记为踢出
    }

    @Override
    public Token obtainToken() {
        return Attribute.TOKEN.obtainToken(WebContext.obtainRequest());
    }

    /**
     * 生成token
     * @param subject 主题
     * @param expirationTime 过期时间
     * @return token
     */
    protected abstract String generateToken(String subject, Date expirationTime);
}
