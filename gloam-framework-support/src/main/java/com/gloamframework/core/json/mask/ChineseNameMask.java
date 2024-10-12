package com.gloamframework.core.json.mask;

/**
 * 中国姓名脱敏
 *
 * @author 晓龙
 */
public class ChineseNameMask extends AbstractMask {

    public ChineseNameMask() {
        // 前后都保持一位
        super(1, 1, 0);
    }

}
