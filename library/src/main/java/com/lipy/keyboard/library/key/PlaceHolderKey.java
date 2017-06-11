package com.lipy.keyboard.library.key;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 占位key
 * Created by lipy on 16/5/24.
 */
public class PlaceHolderKey extends BaseKey {
    public PlaceHolderKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceHolderKey(Context context, KeyParams params, KeyListener listener) {
        super(context, params, listener);
    }

    public PlaceHolderKey(Context context, AttributeSet attrs, KeyParams params, KeyListener
            listener) {
        super(context, attrs, params, listener);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        setEnabled(false);
    }
}
