package com.lipy.keyboard.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lipy.keyboard.library.key.BaseKey;
import com.lipy.keyboard.library.key.KeyType;
import com.lipy.keyboard.library.keyboard.BaseKeyboard;
import com.lipy.keyboard.library.keyboard.KeyboardFactory;
import com.lipy.keyboard.library.keyboard.KeyboardParams;
import com.lipy.keyboard.library.keyboard.KeyboardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义键盘类，一个EditText对应一个SecureKeyBoard类实例
 * Created by lipy on 2016/5/19.
 */
public class SecureKeyBoard extends PopupWindow implements BaseKey.KeyListener {

    private Context mContext;
    private LinearLayout rootView;
    // 当前键盘引用
    private BaseKeyboard currKeyboard;
    // 记录创建时设置的第一个键盘类型和参数，在隐藏后再次显示时需要恢复到第一个键盘
    private KeyboardParams firstKbParams;
    // 键盘集合，在切换键盘过程中，把创建的键盘都放到map中管理
    private Map<KeyboardType, BaseKeyboard> mKeyboardMap;
    private DisplayMetrics dm;
    // 当需要输入密码时，输出到EditText中的是*而不是真实的按键键值，result保存真实输入
    private List<String> result;
    private KeyBoardListener callBack;
    private EditText editText;
    private int maxLength;
    private int height_;

    private boolean mustScroll_;
    private View parent_;

    /**
     * 自定义键盘构造函数
     *
     * @param context        上下文
     * @param keyboardParams 键盘参数，包含键盘类型、是否密码输入等信息，具体请参考KeyboardParams类
     * @param editText       对应自定义键盘的EditText实例
     * @param maxLength      最大输入长度
     */
    public SecureKeyBoard(Activity context, KeyboardParams keyboardParams, EditText editText, int
            maxLength) {
        this(context, keyboardParams, editText, maxLength, null);
    }

    /**
     * 自定义键盘构造函数
     *
     * @param context        上下文
     * @param keyboardParams 键盘参数，包含键盘类型、是否密码输入等信息，具体请参考KeyboardParams类
     * @param editText       对应自定义键盘的EditText实例
     * @param maxLength      最大输入长度
     * @param callBack       键盘回调接口，包含加密算法、按下抬起处理、输入结束通知等
     */
    public SecureKeyBoard(Activity context, KeyboardParams keyboardParams, EditText editText, int
            maxLength, KeyBoardListener callBack) {
        super(context);
        mContext = context;
        if (keyboardParams == null) {
            keyboardParams = new KeyboardParams(context);
        }

        if (callBack == null) {
            callBack = new KeyBoardListener();
        }
        firstKbParams = keyboardParams;
        this.callBack = callBack;
        this.editText = editText;
        this.maxLength = maxLength;
        result = new ArrayList<>();

        dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        BaseKeyboard keyboard = KeyboardFactory.createKeyboard(context, keyboardParams, this);
        mKeyboardMap = new HashMap<>();
        mKeyboardMap.put(keyboardParams.getKeyboardType(), keyboard);
        keyboard.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        height_ = keyboard.getMeasuredHeight();

        rootView = new LinearLayout(context);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        rootView.setOrientation(LinearLayout.VERTICAL);
        currKeyboard = keyboard;
        rootView.addView(currKeyboard);

        initView(rootView, keyboard.getBackColor());
    }

    @Override
    public void dismiss() {

    }

    @Override
    public boolean isUpperCase() {
        return currKeyboard != null && currKeyboard.isUpperCase();
    }

    @Override
    public void onKeyDownListener(KeyType keyType) {
        callBack.onKeyDownListener(keyType);
    }

    @Override
    public void onKeyUpListener(KeyType keyType) {
        callBack.onKeyUpListener(keyType);
    }

    @Override
    public void onKeyClick(KeyType type, String s, KeyboardType keyboardType) {
        switch (type) {
            case CHARACTER:
                addChar(s);
                break;
            case DELETE:
                deleteChar();
                break;
            case HIDE:
            case DONE:
                dismissKeyBoard();
                break;
            case KEYBOARD_SWITCH:
            case SYMBOL_MORE:
                switchKeyboard(keyboardType);
                break;
            case CASE_SWITCH:
                if (currKeyboard != null) {
                    currKeyboard.turnCase();
                }
                break;
            case PLACEHOLDER:
            default:
                // do nothing;
                break;
        }
    }

    /**
     * 清除自定义键盘中记录的用户输入，主要用于密码输入过程
     */
    public void clear() {
        result.clear();
    }

    /**
     * 显示自定义键盘
     *
     * @param parent  键盘显示的父窗口
     * @param gravity 键盘显示位置，正常显示时使用：Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
     * @param x       显示时对应的x坐标，默认填写0
     * @param y       显示时对应的y坐标，默认填写0
     */
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        showAtLocation(parent, gravity, x, y, null);
    }

    /**
     * 显示自定义键盘
     *
     * @param parent    键盘显示的父窗口
     * @param gravity   键盘显示位置，正常显示时使用：Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
     * @param x         显示时对应的x坐标，默认填写0
     * @param y         显示时对应的y坐标，默认填写0
     * @param aboveView 显示在键盘上面的view，当设置此参数时，会根据此view的位置调整界面显示，向上滚动
     */
    public void showAtLocation(View parent, int gravity, int x, int y, View aboveView) {
        if (mKeyboardMap.size() > 1) {
            switchKeyboard(firstKbParams.getKeyboardType());
        }
        for (Map.Entry<KeyboardType, BaseKeyboard> entry : mKeyboardMap.entrySet()) {
            entry.getValue().refresh();
        }
        super.showAtLocation(parent, gravity, x, y);

        if (aboveView != null) {
            int[] screenLocation = new int[2];
            aboveView.getLocationInWindow(screenLocation);
            int tempH_ = dm.heightPixels - screenLocation[1] - aboveView.getHeight();
            if (height_ > tempH_) {
                mustScroll_ = true;
                parent.getRootView().scrollTo(0, Math.abs(height_ - tempH_));
                parent_ = parent;
            }
        }
    }

    /**
     * 隐藏自定义键盘
     */
    public void dismissKeyBoard() {
        super.dismiss();

        //self code
        if (mustScroll_) {
            if (parent_ != null) {
                mustScroll_ = false;
                parent_.getRootView().scrollTo(0, 0);
                parent_ = null;
            }
        }
    }

    /**
     * 获取自定义键盘高度
     */
    public int getHeight_() {
        return height_;
    }

    // 初始化键盘界面
    private void initView(View view, String color) {
        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true); //使pop外的区域点击生效
        this.setFocusable(false);
        this.setAnimationStyle(R.style.AnimBottom);   //滑出滑入动画
        ColorDrawable dw = new ColorDrawable(Color.parseColor(color));
        this.setBackgroundDrawable(dw); //不设置背景setOutsideTouchable方法不会生效
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                callBack.finish(result);
            }
        });

    }

    // 增加一个字符，非密码输入直接把字符增加到EditText中，如果是密码输入，在EditText中输入*，同时在result中保存用户的实际输入
    private void addChar(String s) {
        Editable eda = editText.getText();
        if (eda.length() == 0) {
            result.clear();
        }
        int editEnd = editText.getSelectionEnd();
        if (currKeyboard.getParams().isPwdKeyboard()) {
            eda.insert(editEnd, "*");
        } else {
            eda.insert(editEnd, s);
        }
        if (currKeyboard.getParams().isPwdKeyboard()) {
            if (result.size() < editText.getText().length()) {
                // 这个地方加密
                result.add(editEnd, callBack.encryption(s));
            }
        }
        if (maxLength == 6 && result.size() == maxLength) {
            dismissKeyBoard();
        }
    }

    // 删除一个字符，非密码输入直接删除EditText中的字符，如果是密码输入，同时在result中删除对应输入
    private void deleteChar() {
        Editable eda = editText.getText();
        if (currKeyboard.getParams().isPwdKeyboard() && eda.length() != result.size()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.size(); i++) {
                sb.append("*");
            }
            editText.setText(sb.toString());
        } else {
            int deleteEnd = editText.getSelectionEnd();
            editText.setSelection(deleteEnd);
            if (eda.toString().length() > 0 && deleteEnd > 0) {
                eda.delete(deleteEnd - 1, deleteEnd);
                if (currKeyboard.getParams().isPwdKeyboard()) {
                    result.remove(deleteEnd - 1);
                }
            }
        }
    }

    // 切换键盘
    private void switchKeyboard(KeyboardType type) {
        if (type == null) {
            return;
        }

        // 判断要跳转的是不是和当前的type一致，如果一致，直接返回
        if (currKeyboard != null && currKeyboard.getParams().getKeyboardType() == type) {
            return;
        }

        BaseKeyboard keyboard;
        if (mKeyboardMap.containsKey(type)) {
            keyboard = mKeyboardMap.get(type);
        } else {
            KeyboardParams kbp = currKeyboard.getParams().clone();
            kbp.setKeyboardType(type);
            keyboard = KeyboardFactory.createKeyboard(mContext, kbp, this);
            mKeyboardMap.put(type, keyboard);
        }

        ViewGroup.LayoutParams lp = rootView.getLayoutParams();
        lp.height = height_;
        rootView.setLayoutParams(lp);
        rootView.removeAllViews();
        currKeyboard = keyboard;
        rootView.addView(currKeyboard);
    }
}
