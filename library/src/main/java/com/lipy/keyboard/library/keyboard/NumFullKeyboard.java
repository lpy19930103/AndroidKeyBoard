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
 * 数字键盘，可以返回到英文键盘，适用于：登陆密码、身份证等的输入
 * Created by lipy on 16/5/18.
 */
public class NumFullKeyboard extends BaseKeyboard {

    public NumFullKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumFullKeyboard(Context context, KeyboardParams params, BaseKey.KeyListener
            keyListener) {
        super(context, params, new String[]{"1", "4", "7", "2", "5", "8", "0", "3", "6", "9"},
                keyListener);
    }

    @Override
    protected View initKeyBoard(DisplayMetrics dm) {
        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.setPadding(0, 10, 0, 5);

        int offsetX = (int) (2 * dm.density);
        int itemHeight = KeyboardUtil.dip2px(getContext(), 43);
        int avg = ((dm.widthPixels - 26) - 16 * offsetX) / 4;
        int[] columnLen = new int[]{4, 4, 4, 2};

        for (int i = 0; i < 4; i++) {
            LinearLayout columnView = new LinearLayout(getContext());
            columnView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            columnView.setOrientation(LinearLayout.VERTICAL);
            view.addView(columnView);

            LayoutParams lp;
            int column = columnLen[i];
            for (int j = 0; j < column; j++) {
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                        lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                        break;
                    case 3:
                        lp = new LinearLayout.LayoutParams(avg + 26, itemHeight * 2 + offsetX *
                                5, 1);
                        break;
                    default:
                        lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                        break;
                }

                lp.setMargins(offsetX * 2, offsetX * 4, offsetX * 2, offsetX);

                BaseKey key;
                if (i == 3 && j == 0) {
                    key = getNextKey(KeyType.DELETE);
                } else if (i == 0 && j == 3) {
                    key = getNextKey(KeyType.KEYBOARD_SWITCH);
                } else if (i == 3 && j == 1) {
                    key = getNextKey(KeyType.DONE);
                } else if (i == 2 && j == 3) {
                    key = getNextKey(KeyType.PLACEHOLDER);
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
        BaseKey.KeyParams keyParams;
        switch (type) {
            case CHARACTER:
                keyParams = new BaseKey.KeyParams(type, getNextChar(), R.drawable
                        .keyboard_white_selector, this);
                break;
            case DONE:
                keyParams = new BaseKey.KeyParams(type, params.getFinishKeyName(), R.drawable
                        .keyboard_grey_selector, this);
                break;
            case DELETE:
                keyParams = new BaseKey.KeyParams(type, R.drawable.keyboard_grey_selector,
                        "keyboard_delete", this);
                break;
            case PLACEHOLDER:
                keyParams = new BaseKey.KeyParams(type, null, R.drawable.grey, this);
                break;
            case CASE_SWITCH:
                keyParams = new BaseKey.KeyParams(type, 0, "keyboard_new_upcase_", this);
                break;
            case KEYBOARD_SWITCH:
                keyParams = new BaseKey.KeyParams(type, "abc", R.drawable
                        .keyboard_grey_selector, this);
                keyParams.go2KeyboardType = KeyboardType.ENGLISH;
                break;
            default:
                keyParams = new BaseKey.KeyParams(type, 0, null, this);
                break;
        }

        return KeyFactory.createKey(getContext(), keyParams, keyListener);
    }

    @Override
    public String getBackColor() {
        return "#c9ced7";
    }

    @Override
    public boolean isShowKeyTip() {
        return false;
    }
}
