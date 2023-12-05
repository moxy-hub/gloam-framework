package com.gloamframework.web.security.token.properties;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * token配置参数
 *
 * @author 晓龙
 */
@Data
public class TokenProperties {

    /**
     * token存储的header名称
     */
    private String tokenHeader = "Authorization";

    /**
     * 请求随机数
     */
    private String nonceHeader = "Nonce";

    /**
     * token在请求头中的aes密钥
     */
    private String tokenAesSecret = "03f6e675478d6df0229955cf2f90c6d8";

    /**
     * token加密字符串的分割字符符号
     */
    private String tokenSplit = "-:GLOAM_SPLIT:-";

    /**
     * 请求携带token的有效时间，默认60秒
     */
    private long tokenValidTime = 60 * 1000;

    /**
     * 票据类配置
     */
    @NestedConfigurationProperty
    private TicketProperties ticket = new TicketProperties();

    /**
     * 认证类配置
     */
    @NestedConfigurationProperty
    private AuthenticationProperties authentication = new AuthenticationProperties();

    @Data
    public static class TicketProperties {

    }

    @Data
    public static class AuthenticationProperties {

    }
}
