package com.gloamframework.core.json.mask;

/**
 * 手机号脱敏
 *
 * @author 晓龙
 */
public class MobilePhoneMask extends AbstractMask {

    public MobilePhoneMask() {
        // 前面保留3位，后面保留4位
        super(3, 4, 0);
    }

}
