package com.lipy.keyboard.library.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lipy.keyboard.library.R;
import com.lipy.keyboard.library.key.BaseKey;
import com.lipy.keyboard.library.key.CharacterKey;
import com.lipy.keyboard.library.key.KeyFactory;
import com.lipy.keyboard.library.key.KeyType;


/**
 * 数字密码键盘类
 * Created by lipy on 16/5/18.
 */
public class NumPwdKeyboard extends BaseKeyboard {

    public NumPwdKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumPwdKeyboard(Context context, KeyboardParams params, BaseKey.KeyListener
            keyListener) {
        super(context, params, new String[]{"1", "4", "7", "2", "5", "8", "0", "3", "6", "9"},
                keyListener);
    }

    @Override
    protected View initKeyBoard(DisplayMetrics dm) {
        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOrientation(LinearLayout.VERTICAL);
        setPadding(0, 0, 0, 0);

        int itemHeight = KeyboardUtil.dip2px(getContext(), 54);
        int avg = dm.widthPixels / 3;

        for (int i = 0; i < 4; i++) {
            LinearLayout columnView = new LinearLayout(getContext());
            columnView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            columnView.setOrientation(LinearLayout.HORIZONTAL);
            view.addView(columnView);

            for (int j = 0; j < 3; j++) {
                LayoutParams lp = new LayoutParams(avg, itemHeight, 1);
                lp.setMargins(2, 0, 0, 2);

                BaseKey key;
                if (i == 3 && j == 2) {
                    key = getNextKey(KeyType.DELETE);
                } else if (i == 3 && j == 0) {
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
        BaseKey.KeyParams keyParams = null;
        switch (type) {
            case CHARACTER:
                keyParams = new BaseKey.KeyParams(type, getNextChar(), R.drawable
                        .keyboard_number_white_selector, this);
                break;
            case DELETE:
                keyParams = new BaseKey.KeyParams(type, 0, "keyboard_delete", this);
                keyParams.keyBgColor = R.color.keyboard_integer;
                break;
            case PLACEHOLDER:
                keyParams = new BaseKey.KeyParams(type, null, 0, this);
                keyParams.keyBgColor = R.color.keyboard_integer;
                break;
            default:
                break;
        }

        return KeyFactory.createKey(getContext(), keyParams, keyListener);
    }

    @Override
    public void refresh() {
        super.refresh();

        go2FirstChar();
        for (int i = 0; i < this.getChildCount(); i++) {
            ViewGroup vg = (ViewGroup) getChildAt(i);
            for (int j = 0; j < vg.getChildCount(); j++) {
                ViewGroup vg1 = (ViewGroup) vg.getChildAt(j);
                for (int m = 0; m < vg1.getChildCount(); m++) {
                    View key = vg1.getChildAt(m);
                    if (key instanceof CharacterKey) {
                        ((BaseKey) key).updateView(getContext(), getNextChar());
                    }
                }
            }

        }
    }
}
