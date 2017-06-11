package com.lipy.keyboard.library.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lipy.keyboard.library.key.BaseKey;


/**
 * 自定义键盘基类
 * Created by lipy on 2016/5/18.
 */
public abstract class BaseKeyboard extends LinearLayout {

    protected KeyboardParams params;
    protected String[] keyCharList;
    private int keyCharIndex;
    protected BaseKey.KeyListener keyListener;

    public BaseKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     * @param context      上下文
     * @param params       键盘参数
     * @param charList     键盘中字符按键对应的字符串数组
     * @param keyListener  键盘回调接口
     */
    public BaseKeyboard(Context context, KeyboardParams params, String[] charList, BaseKey
            .KeyListener keyListener) {
        super(context);

        // 初始化键盘中的字符列表
        this.params = params;
        keyCharList = charList;
        keyCharIndex = 0;
        this.keyListener = keyListener;
        if (params.shuffle()) {
            KeyboardUtil.shuffle(keyCharList);
        }

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        addTitle();
        addView(initKeyBoard(params.dm));
    }

    /**
     * 在初始化过程中，需要遍历按键字符串列表，当第二次再获取字符串时需要先调用go2FirstChar，在使用getNextChar
     */
    protected void go2FirstChar() {
        keyCharIndex = 0;
        if (params.shuffle()) {
            KeyboardUtil.shuffle(keyCharList);
        }
    }

    /**
     * 遍历按键字符串列表
     * @return 下一个按键字符串
     */
    protected String getNextChar() {
        if (keyCharIndex < keyCharList.length) {
            return keyCharList[keyCharIndex++];
        }

        return "";
    }

    /**
     * 初始化键盘，由于每一种键盘的按键位置和显示都不相同，需要具体键盘实现初始化过程
     * @param dm 屏幕尺寸
     * @return 键盘view
     */
    protected abstract View initKeyBoard(DisplayMetrics dm);

    /**
     * 在键盘增加title view
     */
    protected void addTitle() {
        //        LinearLayout titleContainer = new LinearLayout(context);
        //        titleContainer.setLayoutParams(
        //                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
        // .WRAP_CONTENT));
        //        titleContainer.setOrientation(LinearLayout.HORIZONTAL);
        //        titleContainer.setGravity(Gravity.CENTER);
        //        titleContainer.setPadding(0, 10, 0, 10);
        //        TextView title = new TextView(context);
        //        title.setText(keyboardParams.getTitle());
        //        title.setTextColor(context.getResources().getColor(R.color.black));
        //        title.setTextSize(18);
        //        title.setGravity(Gravity.CENTER_VERTICAL);
        //        title.setHeight(DensityUtil.dip2px(context, 30));
        //        titleContainer.addView(title);
        //        view.addView(titleContainer);
    }

    /**
     * 重新刷新键盘，当键盘在需要重新排列或者改变状态时会调用此接口
     */
    public void refresh() {

    }

    /**
     * 切换大小写
     */
    public void turnCase() {

    }

    /**
     * 是否大写状态
     */
    public boolean isUpperCase() {
        return false;
    }

    /**
     * 键盘背景色
     */
    public String getBackColor() {
        return "#cccccc";
    }

    /**
     * 是否显示按下提示气泡
     */
    public boolean isShowKeyTip() {
        return false;
    }

    /**
     * 获取键盘参数
     */
    public KeyboardParams getParams() {
        return params;
    }
}
