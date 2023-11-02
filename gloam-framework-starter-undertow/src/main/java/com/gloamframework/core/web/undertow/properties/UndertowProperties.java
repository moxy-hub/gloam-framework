package com.gloamframework.core.web.undertow.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import io.undertow.UndertowOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.unit.DataSize;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 以下的配置会影响buffer,这些buffer会用于服务器连接的IO操作
 * 如果每次需要 ByteBuffer 的时候都去申请，对于堆内存的 ByteBuffer 需要走 JVM 内存分配流程（TLAB -> 堆），对于直接内存则需要走系统调用，这样效率是很低下的。
 * 所以，一般都会引入内存池。在这里就是 `BufferPool`。
 * 目前，UnderTow 中只有一种 `DefaultByteBufferPool`，其他的实现目前没有用。
 * 这个 DefaultByteBufferPool 相对于 netty 的 ByteBufArena 来说，非常简单，类似于 JVM TLAB 的机制
 * 对于 bufferSize，最好和你系统的 TCP Socket Buffer 配置一样
 * `/proc/sys/net/ipv4/tcp_rmem` (对于读取)
 * `/proc/sys/net/ipv4/tcp_wmem` (对于写入)
 * 在内存大于 128 MB 时，bufferSize 为 16 KB 减去 20 字节，这 20 字节用于协议头
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloam.web.undertow")
@Data
public class UndertowProperties {

    /**
     * 缓冲池配置
     */
    @NestedConfigurationProperty
    private BufferPool bufferPool = new BufferPool();

    /**
     * http post body 大小，默认为 -1B ，不限制
     */
    @MappingConfigurationProperty("server.undertow.max-http-post-size")
    private DataSize maxHttpPostSize = DataSize.ofBytes(-1);

    /**
     * 在内存大于 128 MB 时，bufferSize 为 16 KB 减去 20 字节，这 20 字节用于协议头
     * 16384-20
     */
    @MappingConfigurationProperty("server.undertow.buffer-size")
    private DataSize bufferSize = DataSize.ofBytes(16364);

    /**
     * 是否分配的直接内存(NIO直接分配的堆外内存)，这里开启，所以java启动参数需要配置下直接内存大小，减少不必要的GC
     * 在内存大于 128MB 时，默认就是使用直接内存的
     */
    @MappingConfigurationProperty("server.undertow.direct-buffers")
    private Boolean directBuffers = true;

    /**
     * 是否在启动时创建 filter，默认为true
     */
    @MappingConfigurationProperty("server.undertow.eager-filter-init")
    private boolean eagerFilterInit = true;

    /**
     * 限制路径参数数量，默认为1000
     */
    @MappingConfigurationProperty("server.undertow.max-parameters")
    private int maxParameters = UndertowOptions.DEFAULT_MAX_PARAMETERS;

    /**
     * 限制 http header 数量，默认为200
     */
    @MappingConfigurationProperty("server.undertow.max-headers")
    private int maxHeaders = UndertowOptions.DEFAULT_MAX_HEADERS;

    /**
     * 限制 http header 中 cookies 的键值对数量，默认为200
     */
    @MappingConfigurationProperty("server.undertow.max-cookies")
    private int maxCookies = 200;

    /**
     * 是否允许 / 与 %2F 转义。/ 是URL保留字,除非你的应用明确需要，否则不要开启这个转义，默认为false
     */
    @MappingConfigurationProperty("server.undertow.allow-encoded-slash")
    private boolean allowEncodedSlash = false;

    /**
     * 是否允许URL解码，默认为true，除了 %2F 其他的都会处理
     */
    @MappingConfigurationProperty("server.undertow.decode-url")
    private boolean decodeUrl = true;

    /**
     * url 字符编码集，默认是utf-8
     */
    @MappingConfigurationProperty("server.undertow.url-charset")
    private Charset urlCharset = StandardCharsets.UTF_8;

    /**
     * 响应的 http header 是否会加上 'Connection: keep-alive'，默认为 true
     */
    @MappingConfigurationProperty("server.undertow.always-set-keep-alive")
    private boolean alwaysSetKeepAlive = true;

    /**
     * 求超时，默认是不超时，我们的微服务因为可能有长时间的定时任务，所以不做服务端超时，都用客户端超时，所以我们保持这个默认配置
     */
    @MappingConfigurationProperty("server.undertow.no-request-timeout")
    private Duration noRequestTimeout;

    /**
     * undertow线程设置
     */
    @MappingConfigurationProperty("server.undertow.threads")
    @NestedConfigurationProperty
    private final UndertowThreadsProperties threads = new UndertowThreadsProperties();

    /**
     * undertow没有被spring抽象的配置设置
     */
    @MappingConfigurationProperty("server.undertow.options")
    @NestedConfigurationProperty
    private final UndertowOptionsProperties options = new UndertowOptionsProperties();


    @Data
    public static class BufferPool {

        /**
         * 缓冲池缓冲大小
         * 在内存大于 128 MB 时，bufferSize 为 16 KB 减去 20 字节，这 20 字节用于协议头 16384-20
         */
        private DataSize bufferSize = DataSize.ofBytes(16364);

    }

}
