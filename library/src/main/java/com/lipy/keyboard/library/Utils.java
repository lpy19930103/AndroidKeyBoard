package com.lipy.keyboard.library;

import android.text.InputType;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * 工具类
 * Created by lipy on 2016/9/28.
 */

public class Utils {
    public static boolean isEmpty(String src) {
        return src == null || src.trim().length() == 0;
    }

    public static boolean isNotEmpty(String src) {
        return src != null && src.trim().length() > 0;
    }

    /**
     * 禁止Edittext弹出软键盘，光标依然正常显示。
     */
    public static void disableShowSoftInput(EditText editText) {
        editText.setTextIsSelectable(false);
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
        }
    }
}
