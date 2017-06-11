package com.lipy.keyboard.library.key;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 切换大小写key
 * Created by lipy on 16/5/24.
 */
public class CaseSwitchKey extends BaseKey {
    public CaseSwitchKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CaseSwitchKey(Context context, KeyParams params, KeyListener listener) {
        super(context, params, listener);
    }

    public CaseSwitchKey(Context context, AttributeSet attrs, KeyParams params, KeyListener
            listener) {
        super(context, attrs, params, listener);
    }

    @Override
    public void updateView(Context context) {
        super.updateView(context);
        if (mParams.backupKeyParams != null && mParams.backupKeyParams.keyBgImage > 0) {
            if (mKeyListener.isUpperCase()) {
                setBackgroundResource(mParams.backupKeyParams.keyBgImage);
            } else {
                if (mParams.keyBgImage > 0) {
                    setBackgroundResource(mParams.keyBgImage);
                }
            }
        }
    }

}
