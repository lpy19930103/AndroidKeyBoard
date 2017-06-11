package com.lipy.keyboard.library.keyboard;

/**
 * 键盘类型枚举
 * Created by lipy on 16/5/18.
 */
public enum KeyboardType {
    // 英文键盘，与NUM_FULL配合使用，数字键盘，带小数整数键盘，和英文键盘配合的数字键盘
    ENGLISH,
    // 数字键盘，与ENGLISH配合使用，可以返回到英文键盘，适用于：登陆密码、身份证等的输入
    NUM_FULL,
    // 密码键盘，主要用于交易密码的设置和输入
    NUM_PWD,
    // 整数键盘，不带小数点，适用于：手机号、验证码等的输入
    INTEGER,
    // 整数键盘，带小数点，适用于：金额等的输入
    DECIMAL,
}
