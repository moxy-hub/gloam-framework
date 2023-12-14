package com.gloamframework.cloud.sentinel.properties.limit;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.slots.block.ClusterRuleConstant;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 限流配置
 *
 * @author 晓龙
 */
@Data
public class LimitRuleProperties {

    /**
     * 限流异常返回信息
     */
    private String exception = "您点击的太快啦";
    /**
     * 默认限流规则的标识，用于@SentinelResource注解识别
     */
    private String defaultSymbol = "default";

    /**
     * 默认限流配置
     */
    @NestedConfigurationProperty
    private LimitRule defaultRule = new LimitRule();

    private Map<String, LimitRule> rule = new HashMap<>();

    @Data
    public static class LimitRule {
        /**
         * 限流规则针对的来源
         * 如果当前限流规则的 limitApp 为 default，则说明该限流规则对任何调用来源都生效，针对所有调用来源限流，否则只针对指定调用来源限流。
         * strategy为STRATEGY_DIRECT使用
         */
        private String limitApp = "default";

        /**
         * 限流阈值类型
         * FLOW_GRADE_QPS = 1 = QPS限流
         * FLOW_GRADE_THREAD = 0 = 线程限流
         * <li> 线程限流
         * <p>
         * 并发数控制用于保护业务线程池不被慢调用耗尽。
         * 例如，当应用所依赖的下游应用由于某种原因导致服务不稳定、响应延迟增加，对于调用者来说，意味着吞吐量下降和更多的线程数占用，极端情况下甚至导致线程池耗尽。
         * 为了应对太多线程占用的情况，业内有使用隔离的方案，比如通过不同业务逻辑使用不同线程池来隔离业务自身之间的资源争抢（线程池隔离）。
         * 这种隔离方案虽然隔离性比较好，但是代价就是线程数目太多，线程上下文切换的overhead(开销)比较大，特别是对低延时的调用有比较大的影响。
         * Sentinel 并发控制不负责创建和管理线程池，而是简单统计当前请求上下文的线程数目（正在执行的调用数目），如果超出阈值，新的请求会被立即拒绝，效果类似于信号量隔离。
         * 并发线程数控制通常在调用端进行配置。
         * 并发线程数控制参数配置：
         * grade: RuleConstant.FLOW_GRADE_THREAD
         * count: 此时它的含义是并发线程数量
         * </p>
         * <li> QPS限流
         * <p>
         * 当 QPS 超过某个阈值的时候，则采取措施进行流量控制行为（类似于我们前面说过的限流算法上的差异），Sentinel提供了四种流量控制行为
         * 直接拒绝（CONTROL_BEHAVIOR_DEFAULT）
         * Warm Up（CONTROL_BEHAVIOR_WARM_UP）
         * 匀速排队（CONTROL_BEHAVIOR_RATE_LIMITER，漏桶算法 ）
         * 冷启动+匀速器（CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER），除了让流量缓慢增加，还控制的了请求的间隔时间，让请求已均匀速度通过。这种策略是1.4.0版本新增的。
         * 这四个行为，是通过FlowRule中的controlBehavior属性来控制，默认是直接拒绝
         * </p>
         */
        private int grade = RuleConstant.FLOW_GRADE_QPS;

        /**
         * 限流阈值，在QPS下表示，每秒可以请求多少次
         */
        private double count = 5;

        /**
         * 判断的根据是资源自身，还是根据其它关联资源 (refResource)，还是根据链路入口
         * STRATEGY_DIRECT = 0 =  根据调用方限流;
         * STRATEGY_RELATE = 1 = 具有关系的资源流量控制;
         * STRATEGY_CHAIN = 2 = 根据调用链路入口限流;
         */
        private int strategy = RuleConstant.STRATEGY_DIRECT;

        /**
         * 关联资源
         * <p>
         * 设置strategy=STRATEGY_CHAIN
         * 设置refResource为Entrance1来表示只有从入口Entrance1的调用才会进行流量控制
         * </p>
         * <p>
         * 设置strategy=STRATEGY_RELATE
         * 设置refResource为write_db表示设置关联资源
         * </p>
         */
        private String refResource;

        /**
         * 流控效果（直接拒绝 / 排队等待 / 预热/冷启动）
         * <li> 0 => CONTROL_BEHAVIOR_DEFAULT
         * <p>
         * 直接拒绝 当QPS超过任意规则的阈值后，新的请求就会被立即拒绝，拒绝方式为抛出FlowException。这种方式适用于对系统处理能力确切已知的情况下，比如通过压测确定了系统的准确水位时;
         * </p>
         * <li> 1 => CONTROL_BEHAVIOR_WARM_UP
         * <p>
         * 即预热/冷启动方式。当系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。
         * 通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮。
         * 以下都会随着系统访问量增加逐步预热来提升性能的因素。
         * 1、缓存预热
         * 2、数据库连接池初始化
         * </p>
         * <li> 2 => CONTROL_BEHAVIOR_RATE_LIMITER
         * <p>
         * 匀速排队方式会严格控制请求通过的间隔时间，也即是让请求以均匀的速度通过，其实对应的是漏桶算法。
         * 当请求数量远远大于阈值时，这些请求会排队等待，这个等待时间可以设置，如果超过等待时间，那这个请求会被拒绝。
         * 假设qps=5，表示请求每200ms才能通过1个，多处的请求排队等待，超时时间达标最大排队时间，超过最大排队时间则直接拒绝。
         * </p>
         * <li> 3 => CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER
         * <p>
         * 排队等待+预热/冷启动，除了让流量缓慢增加，还控制的了请求的间隔时间，让请求已均匀速度通过
         * </p>
         */
        private int controlBehavior = RuleConstant.CONTROL_BEHAVIOR_DEFAULT;

        /**
         * 预热时间，默认30s
         */
        private int warmUpPeriodSec = 30;

        /**
         * 排队等待时间，表示每一次请求最长等待时间，默认是1000ms=>1秒
         */
        private int maxQueueingTimeMs = 1000;

        /**
         * 是否开启集群模式，默认管理
         */
        private boolean clusterMode;

        /**
         * 集群配置
         */
        @NestedConfigurationProperty
        private Cluster cluster = new Cluster();

        /**
         * 集群配置
         */
        @Data
        public static class Cluster {
            /**
             * 代表全局唯一的规则 ID，Sentinel 集群限流服务端通过此 ID 来区分各个规则，因此务必保持全局唯一。一般 flowId 由统一的管控端进行分配，或写入至 DB 时生成
             * 默认雪花算法生成
             */
            private Long flowId = IdUtil.getSnowflake(RandomUtil.randomLong(0, 31), RandomUtil.randomLong(0, 31)).nextId();
            /**
             * 0:单机均摊，1:全局阈值，默认为单机均摊
             * 代表集群限流阈值模式。其中
             * <li>单机均摊模式下配置的阈值等同于单机能够承受的限额，token server 会根据客户端对应的 namespace（默认为 project.name 定义的应用名）
             * 下的连接数来计算总的阈值（比如独立模式下有 3 个 client 连接到了 token server，然后配的单机均摊阈值为 10，则计算出的集群总量就为 30）；
             * <li>而全局模式下配置的阈值等同于整个集群的总阈值。
             */
            private int thresholdType = 0;
            /**
             * 集群流控失败是否降级为单机流控，默认true
             */
            private boolean fallbackToLocalWhenFail = true;
            /**
             * 策略目前只支持普通策略0
             */
            private int strategy = ClusterRuleConstant.FLOW_CLUSTER_STRATEGY_NORMAL;
            /**
             * 滑动窗口统计采样窗口数量，默认10个
             */
            private int sampleCount = 10;
            /**
             * 滑动窗口大小，默认1秒
             */
            private int windowIntervalMs = 1000;
            /**
             * 资源超时时间
             */
            private long resourceTimeout = 2000L;
            /**
             * 资源超时策略
             */
            private int resourceTimeoutStrategy = 0;
            /**
             * 令牌拒绝策略
             */
            private int acquireRefuseStrategy = 0;

            /**
             * 客户端下线
             */
            private long clientOfflineTime = 2000L;
        }
    }
}
