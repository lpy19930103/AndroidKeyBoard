package com.lipy.keyboard.library.keyboard;

import android.content.Context;

import com.lipy.keyboard.library.key.BaseKey;


/**
 * keyboard工厂类
 * Created by lipy on 16/5/19.
 */
public class KeyboardFactory {
    public static BaseKeyboard createKeyboard(Context context, KeyboardParams params, BaseKey.KeyListener keyListener) {
        BaseKeyboard keyboard;
        switch (params.getKeyboardType()) {
            case ENGLISH:
                keyboard = new EnglishKeyboard(context, params, keyListener);
                break;
            case NUM_FULL:
                keyboard = new NumFullKeyboard(context, params, keyListener);
                break;
            case NUM_PWD:
                params.setShuffle(true);
                keyboard = new NumPwdKeyboard(context, params, keyListener);
                break;
            case INTEGER:
                keyboard = new IntegerKeyboard(context, params, keyListener);
                break;
            case DECIMAL:
                keyboard = new DecimalKeyboard(context, params, keyListener);
                break;
            default:
                keyboard = new IntegerKeyboard(context, params, keyListener);
                break;
        }

        return keyboard;
    }
}
