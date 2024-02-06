package com.gloamframework.common.template;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 内容渲染模版
 *
 * @author 晓龙
 */
public class Template {

    @FunctionalInterface
    public interface TemplateValue {
        Object obtain(String valueKey);
    }

    private final String REGEX_SYMBOL;
    private final Pattern pattern;

    public Template(String SYMBOL) {
        REGEX_SYMBOL = String.format("\\%s\\{([^\\}]+)\\}", SYMBOL);
        pattern = Pattern.compile(REGEX_SYMBOL);
    }

    public String render(String templateStr, TemplateValue templateValue) {
        if (StrUtil.isBlank(templateStr)) {
            return templateStr;
        }
        Matcher matcher = pattern.matcher(templateStr);
        while (matcher.find()) {
            String group = matcher.group();
            // 调取value获取方法
            Object value = templateValue.obtain(group.replaceAll(REGEX_SYMBOL, "$1"));
            if (value != null) {
                templateStr = templateStr.replace(group, (CharSequence) value);
            } else {
                templateStr = templateStr.replace(group, "");
            }
        }
        return templateStr;
    }
}
