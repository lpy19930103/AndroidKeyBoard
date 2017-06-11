package com.lipy.keyboard.library.keyboard;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.lipy.keyboard.library.Utils;


/**
 * 键盘辅助参数类
 * Created by lipy on 16/5/19.
 */
public class KeyboardParams {
    public DisplayMetrics dm;
    private KeyboardType keyboardType = KeyboardType.ENGLISH;
    private boolean isPwdKeyboard;
    private boolean shuffle;
    private String title;
    private String titleIcon;
    private String finishKeyName;

    private KeyboardParams() {
    }

    public KeyboardParams(Activity context) {
        dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    public KeyboardParams(Activity context, KeyboardType keyboardType) {
        this(context, keyboardType, false);
    }

    public KeyboardParams(Activity context, KeyboardType keyboardType, boolean isPwdKeyboard) {
        this.keyboardType = keyboardType;
        this.isPwdKeyboard = isPwdKeyboard;
        dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    public String getFinishKeyName() {
        if (Utils.isEmpty(finishKeyName)) {
            finishKeyName = "确定";
        }

        return finishKeyName;
    }

    public KeyboardParams setFinishKeyName(String finishKeyName) {
        this.finishKeyName = finishKeyName;
        return this;
    }

    public KeyboardType getKeyboardType() {
        return keyboardType;
    }

    public KeyboardParams setKeyboardType(KeyboardType keyboardType) {
        this.keyboardType = keyboardType;
        return this;
    }

    public boolean isPwdKeyboard() {
        return isPwdKeyboard;
    }

    public KeyboardParams setIsPwdKeyboard(boolean isPwdKeyboard) {
        this.isPwdKeyboard = isPwdKeyboard;
        return this;
    }

    public boolean shuffle() {
        return shuffle;
    }

    public KeyboardParams setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        return this;
    }

    public String getTitle() {
        if (title != null) {
            return title;
        } else {
            return "安全输入键盘";
        }

    }

    public KeyboardParams setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitleIcon() {
        if (titleIcon != null) {
            return titleIcon;
        } else {
            return "keyboard_logo";     //这个地方做个公司默认logo
        }
    }

    public KeyboardParams setTitleIcon(String titleIcon) {
        this.titleIcon = titleIcon;
        return this;
    }

    @Override
    public KeyboardParams clone() {
        KeyboardParams kbp = new KeyboardParams();
        kbp.dm = this.dm;
        kbp.keyboardType = this.keyboardType;
        kbp.isPwdKeyboard = this.isPwdKeyboard;
        kbp.shuffle = this.shuffle;
        kbp.title = this.title;
        kbp.titleIcon = this.titleIcon;
        kbp.finishKeyName = this.finishKeyName;

        return kbp;
    }
}
