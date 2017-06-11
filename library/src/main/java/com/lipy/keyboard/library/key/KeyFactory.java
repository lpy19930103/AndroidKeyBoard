package com.lipy.keyboard.library.key;

import android.content.Context;

/**
 * key工厂类
 * Created by lipy on 16/5/19.
 */
public class KeyFactory {

    public static BaseKey createKey(Context context, BaseKey.KeyParams params, BaseKey.KeyListener keyListener) {
        BaseKey key;
        switch (params.keyType) {
            case CHARACTER:
                key = new CharacterKey(context, params, keyListener);
                break;
            case CASE_SWITCH:
                key = new CaseSwitchKey(context, params, keyListener);
                break;
            case PLACEHOLDER:
                key = new PlaceHolderKey(context, params, keyListener);
                break;
            case DELETE:
                key = new DeleteKey(context, params, keyListener);
                break;
            case HIDE:
                key = new HideKey(context, params, keyListener);
                break;
            case DONE:
            case KEYBOARD_SWITCH:
            case SYMBOL_MORE:
            default:
                key = new BaseKey(context, params, keyListener);
                break;
        }

        return key;
    }
}
