package com.lipy.keyboard.library.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lipy.keyboard.library.R;
import com.lipy.keyboard.library.key.BaseKey;
import com.lipy.keyboard.library.key.KeyFactory;
import com.lipy.keyboard.library.key.KeyType;


/**
 * 整数数字键盘类
 * Created by lipy on 16/5/18.
 */
public class IntegerKeyboard extends BaseKeyboard {

    public IntegerKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntegerKeyboard(Context context, KeyboardParams params, BaseKey.KeyListener
            keyListener) {
        super(context, params, new String[]{"1", "4", "7", "2", "5", "8", "0", "3", "6", "9"},
                keyListener);
    }

    @Override
    protected View initKeyBoard(DisplayMetrics dm) {
        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOrientation(LinearLayout.HORIZONTAL);
        setPadding(0, 0, 0, 0);

        int itemHeight = KeyboardUtil.dip2px(getContext(), 54);
        int avg = dm.widthPixels / 4;

        for (int i = 0; i < 4; i++) {
            LinearLayout columnView = new LinearLayout(getContext());
            columnView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            columnView.setOrientation(LinearLayout.VERTICAL);
            view.addView(columnView);

            LinearLayout.LayoutParams lp;
            int column = (i < 3) ? 4 : 2;
            for (int j = 0; j < column; j++) {
                if (i < 3) {
                    lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                } else {
                    lp = new LinearLayout.LayoutParams(avg, itemHeight * 2 + 2, 1);
                }
                lp.setMargins(2, 2, 0, 0);

                BaseKey key;
                if (i == 3 && j == 0) {
                    key = getNextKey(KeyType.DELETE);
                } else if (i == 0 && j == 3) {
                    key = getNextKey(KeyType.PLACEHOLDER);
                } else if (i == 2 && j == 3) {
                    key = getNextKey(KeyType.HIDE);
                } else if (i == 3 && j == 1) {
                    key = getNextKey(KeyType.DONE);
                } else {
                    key = getNextKey(KeyType.CHARACTER);
                }
                key.setLayoutParams(lp);
                key.setOnClickListener(key);
                columnView.addView(key);
            }
        }

        return view;
    }

    protected BaseKey getNextKey(KeyType type) {
        BaseKey.KeyParams keyParams = null;
        switch (type) {
            case CHARACTER:
                keyParams = new BaseKey.KeyParams(type, getNextChar(), R.drawable
                        .keyboard_number_white_selector, this);
                break;
            case DONE:
                keyParams = new BaseKey.KeyParams(type, params.getFinishKeyName(), R.drawable
                        .keyboard_number_white_selector, this);
                keyParams.charColor = R.color.black;
                break;
            case HIDE:
                keyParams = new BaseKey.KeyParams(type, R.drawable.keyboard_number_white_selector,
                        "keyboard_icon", this);
                break;
            case DELETE:
                keyParams = new BaseKey.KeyParams(type, R.drawable.keyboard_number_white_selector,
                        "keyboard_delete", this);
                break;
            case PLACEHOLDER:
                keyParams = new BaseKey.KeyParams(type, null, R.drawable
                        .keyboard_number_white_selector, this);
                break;
            default:
                break;
        }

        return KeyFactory.createKey(getContext(), keyParams, keyListener);
    }

}
