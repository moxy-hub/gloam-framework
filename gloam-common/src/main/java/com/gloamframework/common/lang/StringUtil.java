package com.gloamframework.common.lang;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author 晓龙
 */
public class StringUtil extends StrUtil {

    /**
     * 检查是否有一个字符串为空
     */
    public static boolean isAnyBlank(CharSequence... strings) {
        if (ArrayUtil.isEmpty(strings)) {
            return true;
        }
        for (CharSequence sequence : strings) {
            if (StrUtil.isBlank(sequence)) {
                return true;
            }
        }
        return false;
    }

    public static String isBlankOrGet(String stringValue, String defaultValue) {
        return isBlank(stringValue) ? defaultValue : stringValue;
    }

    public static String isBlankOrGetNonnull(String stringValue, String defaultValue, RuntimeException exception) {
        String blankOrGet = isBlankOrGet(stringValue, defaultValue);
        if (isNotBlank(blankOrGet)) {
            return blankOrGet;
        }
        throw exception;
    }

    public static String isBlankOrThrow(String stringValue, RuntimeException exception) {
        if (isNotBlank(stringValue)) {
            return stringValue;
        }
        throw exception;
    }
}
