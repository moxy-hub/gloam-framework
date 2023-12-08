package com.gloamframework.data.redis;

import com.gloamframework.data.redis.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Redis配置类
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(RedisProperties.class)
public class GloamRedisConfigure {

    /**
     * 默认支持的Lettuce客户端
     */
    @Bean
    @ConditionalOnProperty(value = "gloam.redis.client", havingValue = "LETTUCE")
    public GloamRedisTemplate lettuceRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return new GloamRedisTemplate(lettuceConnectionFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "gloam.redis.client", havingValue = "JEDIS")
    public GloamRedisTemplate jedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        return new GloamRedisTemplate(jedisConnectionFactory);
    }

}
