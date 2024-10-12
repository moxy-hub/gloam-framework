package com.gloamframework.core.json;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.gloamframework.core.json.databind.LocalDateTimeDeserializer;
import com.gloamframework.core.json.databind.LocalDateTimeSerializer;
import com.gloamframework.core.json.databind.NumberSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Json配置
 *
 * @author 晓龙
 */
@Configurable
@Slf4j
public class JsonConfigure {

    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public JsonUtils jsonUtils(List<ObjectMapper> objectMappers) {
        // 全局配置序列化返回 JSON 处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule
                // Jackson 序列化 Long 类型超出 JS 最大最小值序列化位字符串转为 String，解决后端返回的类型在前端精度丢失的问题
                .addSerializer(Long.class, NumberSerializer.INSTANCE)
                .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                .addSerializer(BigInteger.class, NumberSerializer.INSTANCE)
                // Jackson 序列化 BigDecimal 类型为 String，解决后端返回的类型在前端精度丢失的问题
                .addSerializer(BigDecimal.class, ToStringSerializer.instance)
                // ======================= 时间序列化规则 =======================
                .addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE)
                .addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE)
                .addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE)
                // ======================= 时间反序列化规则 =======================
                .addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        // 注册到 objectMapper
        objectMappers.forEach(objectMapper -> objectMapper.registerModule(javaTimeModule));
        // 设置 objectMapper 到 JsonUtils {
        JsonUtils.init(CollUtil.getFirst(objectMappers));
        log.info("[Jackson]:初始化配置成功");
        return new JsonUtils();
    }
}
