package com.lipy.keyboard.library;


import com.lipy.keyboard.library.key.KeyType;

import java.util.List;

/**
 * 键盘监听类
 * Created by lipy on 16/5/25.
 */
public class KeyBoardListener {

    /**
     * 在密码输入时由键盘的调用者确定加密算法，默认不加密
     * @param clearStr 键盘原始输入字符
     * @return 加密后的字符
     */
    public String encryption(String clearStr) {
        return clearStr;
    }

    /**
     * 按键按下回调
     * @param keyType 按键类型
     */
    public void onKeyDownListener(KeyType keyType) {

    }

    /**
     * 按键抬起回调
     * @param keyType 按键类型
     */
    public void onKeyUpListener(KeyType keyType) {

    }

    /**
     * 输入结束回调，主要用于密码输入结束自动进行下一步过程
     * @param result 输入字符集和
     */
    public void finish(List<String> result) {

    }
}
