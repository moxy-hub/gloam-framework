package com.gloamframework.data.redis.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * redis配置文件
 *
 * @author 晓龙
 */
@Data
@ConfigurationProperties("gloam.redis")
public class RedisProperties {

    /**
     * 使用的客户端类型，默认LETTUCE,对于Jedis,没有适配，需要外部引入相关依赖，并注入JedisConnectionFactory
     */
    @MappingConfigurationProperty("gloam.redis.client")
    private Client client = Client.LETTUCE;

    /**
     * Database index used by the connection factory.
     */
    @MappingConfigurationProperty("spring.redis.database")
    private int database = 0;

    /**
     * Connection URL. Overrides host, port, and password. User is ignored. Example:
     * redis://user:password@example.com:6379
     */
    @MappingConfigurationProperty("spring.redis.url")
    private String url;

    /**
     * Redis server host.
     */
    @MappingConfigurationProperty("spring.redis.host")
    private String host = "localhost";

    /**
     * Login password of the redis server.
     */
    @MappingConfigurationProperty("spring.redis.password")
    private String password;

    /**
     * Redis server port.
     */
    @MappingConfigurationProperty("spring.redis.port")
    private int port = 6379;

    /**
     * Whether to enable SSL support.
     */
    @MappingConfigurationProperty("spring.redis.ssl")
    private boolean ssl;

    /**
     * 连接超时时间，默认30秒
     */
    @MappingConfigurationProperty("spring.redis.timeout")
    private Duration timeout = Duration.ofMillis(30000);

    /**
     * Client name to be set on connections with CLIENT SETNAME.
     */
    @MappingConfigurationProperty("spring.redis.client-name")
    private String clientName;

    @MappingConfigurationProperty("spring.redis.sentinel")
    @NestedConfigurationProperty
    private Sentinel sentinel;

    @MappingConfigurationProperty("spring.redis.cluster")
    @NestedConfigurationProperty
    private Cluster cluster;

    @MappingConfigurationProperty("spring.redis.jedis")
    @NestedConfigurationProperty
    private final Jedis jedis = new Jedis();

    @MappingConfigurationProperty("spring.redis.lettuce")
    @NestedConfigurationProperty
    private final Lettuce lettuce = new Lettuce();

    /**
     * redis哨兵模式配置
     */
    @Data
    public static class Sentinel {
        /**
         * Redis服务名字
         */
        @MappingConfigurationProperty("master")
        private String master;

        /**
         * 哨兵节点：“host:port”对列表，以逗号分隔。
         */
        @MappingConfigurationProperty("nodes")
        private List<String> nodes = new ArrayList<>();

        /**
         * 哨兵的认证密码
         */
        @MappingConfigurationProperty("password")
        private String password;
    }

    /**
     * redis集群配置
     */
    @Data
    public static class Cluster {
        /**
         * 要启动的"host:port"对的逗号分隔列表。这代表了集群节点的“初始”列表，并且需要至少有一个条目。
         */
        @MappingConfigurationProperty("nodes")
        private List<String> nodes;

        /**
         * 执行命令时要遵循的最大重定向数集群。
         */
        @MappingConfigurationProperty("max-redirects")
        private Integer maxRedirects;
    }

    /**
     * 连接池配置
     */
    @Data
    public static class Pool {

        /**
         * 连接池中的最大空闲连接 默认 8
         */
        @MappingConfigurationProperty("max-idle")
        private int maxIdle = 8;

        /**
         * 连接池中的最小空闲连接 默认 0
         */
        @MappingConfigurationProperty("min-idle")
        private int minIdle = 0;

        /**
         * 连接池最大连接数（使用负值表示没有限制） 默认 8
         */
        @MappingConfigurationProperty("max-active")
        private int maxActive = 8;

        /**
         * 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
         */
        @MappingConfigurationProperty("max-wait")
        private Duration maxWait = Duration.ofMillis(-1);

        /**
         * Time between runs of the idle object evictor thread. When positive, the idle
         * object evictor thread starts, otherwise no idle object eviction is performed.
         */
        @MappingConfigurationProperty("time-between-eviction-runs")
        private Duration timeBetweenEvictionRuns;

    }

    @Data
    public static class Jedis {

        /**
         * Jedis pool configuration.
         */
        @MappingConfigurationProperty("pool")
        @NestedConfigurationProperty
        private Pool pool;

    }

    /**
     * Lettuce client properties.
     */
    @Data
    public static class Lettuce {

        /**
         * 在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭，默认100ms
         */
        @MappingConfigurationProperty("shutdown-timeout")
        private Duration shutdownTimeout = Duration.ofMillis(100);

        /**
         * Lettuce pool configuration.
         */
        @MappingConfigurationProperty("pool")
        @NestedConfigurationProperty
        private Pool pool;

        @MappingConfigurationProperty("cluster")
        @NestedConfigurationProperty
        private final Cluster cluster = new Cluster();

        @Data
        public static class Cluster {

            @MappingConfigurationProperty("refresh")
            @NestedConfigurationProperty
            private final Refresh refresh = new Refresh();


            @Data
            public static class Refresh {

                /**
                 * Cluster topology refresh period.
                 */
                @MappingConfigurationProperty("period")
                private Duration period;

                /**
                 * Whether adaptive topology refreshing using all available refresh
                 * triggers should be used.
                 */
                @MappingConfigurationProperty("adaptive")
                private boolean adaptive;

            }

        }

    }

    public enum Client {
        JEDIS,
        LETTUCE;
    }
}
