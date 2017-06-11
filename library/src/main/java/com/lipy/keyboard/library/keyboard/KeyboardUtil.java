package com.lipy.keyboard.library.keyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * 键盘util类
 * Created by lipy on 16/5/19.
 */
public class KeyboardUtil {

    // 乱序
    public static String[] shuffle(String[] list) {
        int len = list.length;
        Random rd = new Random();
        for (int i = 0; i < len; i++) {
            int j = rd.nextInt(len);//生成随机数
            String temp = list[i];//交换
            list[i] = list[j];
            list[j] = temp;
        }

        return list;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context
                .getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context
                .getResources().getDisplayMetrics());
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap readBitMap(Context context, Class RDrawableClz, String imageName) {
        Field field = null;
        try {
            field = RDrawableClz.getField(imageName);
            int imageId = field.getInt(field.getName());

            return readBitMap(context, imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 禁止Edittext弹出软键盘，光标依然正常显示。
     */
    public static void disableShowSoftInput(EditText editText)
    {
        editText.setTextIsSelectable(false);
        if (android.os.Build.VERSION.SDK_INT <= 10)
        {
            editText.setInputType(InputType.TYPE_NULL);
        }
        else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus",boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            }catch (Exception e) {
                e.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus",boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
