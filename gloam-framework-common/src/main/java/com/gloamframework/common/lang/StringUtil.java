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

}
