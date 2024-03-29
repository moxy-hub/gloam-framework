package com.gloamframework.web.security.token.properties;

import com.gloamframework.web.security.token.constant.TokenStrategy;
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
     * token实现的策略，默认为jwt
     */
    private TokenStrategy strategy = TokenStrategy.JWT;

    /**
     * 是否启动同端排斥，会自动挤下线
     */
    private boolean enableDeviceRepel = false;

    /**
     * token存储的header名称
     */
    private String header = "Authorization";

    /**
     * 请求随机数
     */
    private String nonceHeader = "Nonce";

    /**
     * token在请求头中的aes密钥
     */
    private String requestAesSecret = "03f6e675478d6df0229955cf2f90c6d8";

    /**
     * token加密字符串的分割字符符号
     */
    private String split = "-:GLOAM_SPLIT:-";

    /**
     * 请求携带token的有效时间，默认60秒
     */
    private long requestValidTime = 60 * 1000;

    /**
     * accessToken的过期时间，默认20分钟
     */
    private long accessTokenExpire = 20 * 60 * 1000;

    /**
     * refreshToken的过期时间，默认一个小时
     */
    private long refreshTokenExpire = 60 * 60 * 1000;

    /**
     * token密钥
     */
    private String secret = "gloam-token-#@!secret";

    /**
     * 认证类配置
     */
    @NestedConfigurationProperty
    private AuthenticationProperties authentication = new AuthenticationProperties();

    /**
     * 安全环境配置
     */
    @NestedConfigurationProperty
    private Env env = new Env();

    @Data
    public static class AuthenticationProperties {

        /**
         * token被踢下线的时长
         */
        private long kickOffTime = 60 * 60 * 1000;
    }

    @Data
    public static class Env {

        /**
         * 平台
         */
        private String platform;

    }
}
