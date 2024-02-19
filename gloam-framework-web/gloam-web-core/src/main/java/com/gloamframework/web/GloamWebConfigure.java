package com.gloamframework.web;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.gloamframework.core.json.databind.LocalDateTimeDeserializer;
import com.gloamframework.core.json.databind.LocalDateTimeSerializer;
import com.gloamframework.core.json.databind.NumberSerializer;
import com.gloamframework.web.covert.Timestamp2LocalDateTimeConverter;
import com.gloamframework.web.exception.GloamValidationExceptionResolver;
import com.gloamframework.web.exception.GlomaHandlerExceptionResolver;
import com.gloamframework.web.properties.WebServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Configurable
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebServerProperties.class)
public class GloamWebConfigure {

    @Bean
    @ConditionalOnMissingBean(GlomaHandlerExceptionResolver.class)
    public GlomaHandlerExceptionResolver gloamHandlerExceptionResolver() {
        return new GlomaHandlerExceptionResolver();
    }

    @Bean
    @ConditionalOnMissingBean(GloamValidationExceptionResolver.class)
    public GloamValidationExceptionResolver gloamValidationExceptionResolver() {
        return new GloamValidationExceptionResolver();
    }

    @Bean
    public Converter<String, LocalDateTime> timestamp2LocalDateTimeConverter() {
        return new Timestamp2LocalDateTimeConverter();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder
                    // Jackson 序列化 Long 类型超出 JS 最大最小值序列化位字符串转为 String，解决后端返回的类型在前端精度丢失的问题
                    .serializerByType(Long.class, NumberSerializer.INSTANCE)
                    .serializerByType(Long.TYPE, NumberSerializer.INSTANCE)
                    .serializerByType(BigInteger.class, NumberSerializer.INSTANCE)
                    // Jackson 序列化 BigDecimal 类型为 String，解决后端返回的类型在前端精度丢失的问题
                    .serializerByType(BigDecimal.class, ToStringSerializer.instance)
                    // ======================= 时间序列化规则 =======================
                    .serializerByType(LocalDate.class, LocalDateSerializer.INSTANCE)
                    .serializerByType(LocalTime.class, LocalTimeSerializer.INSTANCE)
                    .serializerByType(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE)
                    // ======================= 时间反序列化规则 =======================
                    .deserializerByType(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                    .deserializerByType(LocalDate.class, LocalDateDeserializer.INSTANCE)
                    .deserializerByType(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        };
    }
}
