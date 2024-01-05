package com.gloamframework.web.covert;

import com.gloamframework.core.exception.GloamRuntimeException;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间戳转localDateTime转化器
 *
 * @author 晓龙
 */
public class Timestamp2LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        long timestamp;
        try {
            timestamp = Long.parseLong(source);
        } catch (Exception e) {
            throw new GloamRuntimeException("传入的时间格式不是时间戳类型:{}", source);
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

}
