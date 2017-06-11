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
 * 英文键盘类
 * Created by lipy on 16/5/18.
 */
public class EnglishKeyboard extends BaseKeyboard {
    private boolean isUpperCase = false;

    public EnglishKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnglishKeyboard(Context context, KeyboardParams params, BaseKey.KeyListener
            keyListener) {
        super(context, params, new String[]{"q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
                "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b",
                "n", "m"},
                keyListener);
    }

    @Override
    protected View initKeyBoard(DisplayMetrics dm) {
        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOrientation(LinearLayout.VERTICAL);
        view.setPadding(0, 10, 0, 5);

        int offsetX = (int) (2 * dm.density);
        int itemHeight = KeyboardUtil.dip2px(getContext(), 43);
        int avg = (dm.widthPixels - 11 * offsetX) / 10;
        int[] columnLen = new int[]{10, 9, 9, 3};

        for (int i = 0; i < 4; i++) {
            LinearLayout rowView = new LinearLayout(getContext());
            rowView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            rowView.setOrientation(LinearLayout.HORIZONTAL);
            view.addView(rowView);

            LayoutParams lp;
            int column = columnLen[i];
            for (int j = 0; j < column; j++) {
                switch (i) {
                    case 0:
                        lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                        break;
                    case 1:
                        rowView.setPadding(avg / 2, 0, avg / 2, 0);
                        lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                        break;
                    case 2:
                        if (j == 0 || j == 8) {
                            lp = new LinearLayout.LayoutParams((int) (avg * 1.5), itemHeight, 1);
                        } else {
                            lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                        }
                        break;
                    case 3:
                        if (j == 0 || j == 2) {
                            lp = new LinearLayout.LayoutParams(avg * 2, itemHeight);
                        } else {
                            lp = new LinearLayout.LayoutParams(((int) (avg * 4.5) + offsetX * 5),
                                    itemHeight, 1);
                        }
                        break;
                    default:
                        lp = new LinearLayout.LayoutParams(avg, itemHeight, 1);
                        break;
                }

                lp.setMargins(offsetX, offsetX * 4, offsetX, offsetX);

                BaseKey key;
                if (i == 2 && j == 0) {
                    key = getNextKey(KeyType.CASE_SWITCH);
                } else if (i == 2 && j == 8) {
                    key = getNextKey(KeyType.DELETE);
                } else if (i == 3 && j == 0) {
                    key = getNextKey(KeyType.KEYBOARD_SWITCH);
                } else if (i == 3 && j == 1) {
                    key = getNextKey(KeyType.PLACEHOLDER);
                } else if (i == 3 && j == 2) {
                    key = getNextKey(KeyType.DONE);
                } else {
                    key = getNextKey(KeyType.CHARACTER);
                }

                key.setLayoutParams(lp);
                key.setOnClickListener(key);
                rowView.addView(key);
            }
        }

        return view;
    }

    protected BaseKey getNextKey(KeyType type) {
        BaseKey.KeyParams keyParams;
        switch (type) {
            case CHARACTER:
                keyParams = new BaseKey.KeyParams(type, getNextChar(), R.drawable
                        .keyboard_letter_white_selector, this);
                break;
            case DONE:
                keyParams = new BaseKey.KeyParams(type, params.getFinishKeyName(), R.drawable
                        .keyboard_grey_selector, this);
                break;
            case DELETE:
                keyParams = new BaseKey.KeyParams(type, R.drawable.keyboard_full_delete_selector,
                        null, this);
                break;
            case PLACEHOLDER:
                keyParams = new BaseKey.KeyParams(type, null, R.drawable.white, this);
                break;
            case CASE_SWITCH:
                keyParams = new BaseKey.KeyParams(type, R.drawable.keyboard_new_upcase_, null, this);
                BaseKey.KeyParams params = keyParams.clone();
                params.keyBgImage = R.drawable.keyboard_new_lowercase_;
                keyParams.setBackupKeyParams(params);
                break;
            case KEYBOARD_SWITCH:
                keyParams = new BaseKey.KeyParams(type, "123", R.drawable
                        .keyboard_grey_selector, this);
                keyParams.go2KeyboardType = KeyboardType.NUM_FULL;
                break;
            default:
                keyParams = new BaseKey.KeyParams(type, 0, null, this);
                break;
        }

        return KeyFactory.createKey(getContext(), keyParams, keyListener);
    }

    @Override
    public void turnCase() {
        isUpperCase = !isUpperCase;
        for (int i = 0; i < this.getChildCount(); i++) {
            ViewGroup vg = (ViewGroup)getChildAt(i);
            for (int j = 0; j < vg.getChildCount(); j++) {
                ViewGroup vg1 = (ViewGroup)vg.getChildAt(j);
                for (int m = 0; m < vg1.getChildCount(); m++) {
                    View key = vg1.getChildAt(m);
                    if (key instanceof BaseKey) {
                        ((BaseKey)key).updateView(getContext());
                    }
                }
            }
        }
    }

    @Override
    public void refresh() {
        if (isUpperCase) {
            turnCase();
        }
    }

    @Override
    public boolean isUpperCase() {
        return isUpperCase;
    }

    @Override
    public String getBackColor() {
        return "#c9ced7";
    }

    @Override
    public boolean isShowKeyTip() {
        return true;
    }
}
