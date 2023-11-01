package com.gloamframework.core.boot.env.convert.converter;

import com.gloamframework.core.boot.env.convert.GloamConverter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.unit.DataSize;

import java.util.Collections;
import java.util.Set;

/**
 * 数据大小格式转String
 *
 * @author 晓龙
 */
@GloamConverter
public class DataSizeToStringConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(DataSize.class, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }
        return String.valueOf(source);
    }

}
