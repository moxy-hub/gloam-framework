package com.gloamframework.core.boot.env;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配置重写mapping
 *
 * @author 晓龙
 */
@AllArgsConstructor
@Getter
public class OverrideMapping {

    private String originalProperty;
    private String targetProperty;
    private Object value;
}
