package com.lipy.keyboard.library.key;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lipy.keyboard.library.R;
import com.lipy.keyboard.library.Utils;
import com.lipy.keyboard.library.keyboard.BaseKeyboard;
import com.lipy.keyboard.library.keyboard.KeyboardType;
import com.lipy.keyboard.library.keyboard.KeyboardUtil;


/**
 * 按键类，一个实例代表一个自定义键盘中的按键
 * Created by lipy on 2016/5/19.
 */
public class BaseKey extends FrameLayout implements View.OnClickListener {
    // 用户点击屏幕时的按键，解决在用户滑动手指到另外的按键时弹出提示信息问题
    private static String mClickKey;

    // 按键参数类
    protected KeyParams mParams;

    // 按键回调
    protected KeyListener mKeyListener;

    // 按键气泡
    protected MyToast toast;

    public BaseKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 构造函数，需要按键参数和回调接口
    public BaseKey(Context context, KeyParams params, KeyListener listener) {
        this(context, null, params, listener);
    }

    // 构造函数，需要按键参数和回调接口
    public BaseKey(Context context, AttributeSet attrs, KeyParams params, KeyListener listener) {
        super(context, attrs);
        mParams = params;
        mKeyListener = listener;
        if (mKeyListener == null) {
            mKeyListener = new KeyListener() {

                @Override
                public boolean isUpperCase() {
                    return false;
                }

                @Override
                public void onKeyDownListener(KeyType keyType) {

                }

                @Override
                public void onKeyUpListener(KeyType keyType) {

                }

                @Override
                public void onKeyClick(KeyType type, String s, KeyboardType keyboardType) {

                }
            };
        }

        initView(context);
        addListen();
    }

    protected void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_key, this, true);
        if (mParams.keyBgImage > 0) {
            setBackgroundResource(mParams.keyBgImage);
        } else if (mParams.keyBgColor > 0) {
            setBackgroundColor(context.getResources().getColor(mParams.keyBgColor));
        }

        TextView textView = (TextView) findViewById(R.id.key_tv);
        ImageView imageView = (ImageView) findViewById(R.id.key_img);

        if (Utils.isNotEmpty(mParams.mCharacter)) {
            textView.setText(mParams.mCharacter);
            if (mParams.charColor > 0) {
                textView.setTextColor(getContext().getResources().getColor(mParams.charColor));
            }
            textView.setVisibility(VISIBLE);
            imageView.setVisibility(GONE);
        } else if (Utils.isNotEmpty(mParams.imageSrc)) {
            imageView.setImageBitmap(KeyboardUtil.readBitMap(context, R.drawable.class, mParams
                    .imageSrc));
            if (mParams.imageBackground > 0) {
                imageView.setBackgroundResource(mParams.imageBackground);
            }
            imageView.setVisibility(VISIBLE);
            textView.setVisibility(GONE);
        }
    }

    // 当键盘有变化时，更新按键状态接口
    public void updateView(Context context) {

    }

    // 当键盘有变化时（按键内容变化），更新按键状态接口
    public void updateView(Context context, String s) {

    }

    protected String getCharacter() {
        return mParams.mCharacter;
    }

    private void addListen() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 判断点击的是否是开始按下时的按键
                if (Utils.isNotEmpty(mParams.mCharacter)) {
                    if (Utils.isNotEmpty(mClickKey) && !mClickKey.equals(mParams.mCharacter)) {
                        return true;
                    } else {
                        mClickKey = mParams.mCharacter;
                    }
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (mParams.keyType != KeyType.PLACEHOLDER) {
                            mClickKey = null;
                            mKeyListener.onKeyUpListener(mParams.keyType);
                            if (toast != null) {
                                toast.hide();
                                toast = null;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        showDialog(mParams.mCharacter, mParams.imageSrc);
                        mKeyListener.onKeyDownListener(mParams.keyType);
                        break;
                }
                return false;
            }
        });
    }

    protected void showDialog(String text, String imageSrc) {
        if (!mParams.mKeyboard.isShowKeyTip() || mParams.keyType != KeyType.CHARACTER) {
            return;
        }
        int position[] = {-1, -1};
        getLocationOnScreen(position);

        LinearLayout view = new LinearLayout(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout alterContainer = new RelativeLayout(getContext());
        int width = getLayoutParams().width;
        int height = getLayoutParams().height;
        int keyShow = KeyboardUtil.dip2px(getContext(), 55);
        alterContainer.setBackgroundResource(R.drawable.keyboard_toast);
        alterContainer.setLayoutParams(new RelativeLayout.LayoutParams(keyShow, keyShow));
        //        alterContainer.setLayoutParams(new RelativeLayout.LayoutParams((int) (width * 2
        // .5), (int) (height * 2.5)));
        alterContainer.setGravity(Gravity.CENTER);
        view.addView(alterContainer);

        if (text != null) {
            TextView tv = new TextView(getContext());
            tv.setTextSize(26);
            tv.setTextColor(getContext().getResources().getColor(R.color.black));
            tv.setText(text);
            alterContainer.addView(tv);
            //            alterContainer.setPadding(0, 0, 0, 80);
        } else if (Utils.isNotEmpty(imageSrc)) {
            ImageView iv = new ImageView(getContext());
            iv.setImageBitmap(KeyboardUtil.readBitMap(getContext(), R.drawable.class, imageSrc));
            alterContainer.addView(iv);
        }

        toast = MyToast.makeView(getContext(), view, 2000);
        toast.setView(view);
        toast.setGravity(Gravity.LEFT | Gravity.TOP, position[0] - (keyShow - width) / 2,
                position[1] - height - KeyboardUtil.dip2px(getContext(), 42));
        toast.show();
    }

    @Override
    public void onClick(View v) {
        switch (mParams.keyType) {
            case CHARACTER:
            case DELETE:
            case HIDE:
            case DONE:
            case KEYBOARD_SWITCH:
            case SYMBOL_MORE:
            case CASE_SWITCH:
                mKeyListener.onKeyClick(mParams.keyType, getCharacter(), mParams.go2KeyboardType);
                break;
            case PLACEHOLDER:
            default:
                // do nothing;
                break;
        }
    }

    public interface KeyListener {

        boolean isUpperCase();

        void onKeyDownListener(KeyType keyType);

        void onKeyUpListener(KeyType keyType);

        void onKeyClick(KeyType type, String s, KeyboardType keyboardType);
    }

    public static class KeyParams {

        public BaseKeyboard mKeyboard;
        public KeyType keyType;
        public String mCharacter;
        public int charColor;
        public int keyBgImage;
        public int keyBgColor;
        public String imageSrc;
        public int imageBackground;
        public KeyboardType go2KeyboardType;
        public KeyParams backupKeyParams;

        private KeyParams() {

        }

        public KeyParams(KeyType type, String s, int keyBgImage, BaseKeyboard kb) {
            keyType = type;
            mCharacter = s;
            this.keyBgImage = keyBgImage;
            imageBackground = 0;
            mKeyboard = kb;
        }

        public KeyParams(KeyType type, int keyBgImage, String imageSrc, BaseKeyboard kb) {
            keyType = type;
            this.keyBgImage = keyBgImage;
            this.imageSrc = imageSrc;
            imageBackground = 0;
            mKeyboard = kb;
        }

        public KeyParams(KeyType type, int keyBgImage, String imageSrc, int
                imageBackground, BaseKeyboard kb) {
            keyType = type;
            this.keyBgImage = keyBgImage;
            this.imageSrc = imageSrc;
            this.imageBackground = imageBackground;
            mKeyboard = kb;
        }

        public void setBackupKeyParams(KeyParams params) {
            backupKeyParams = params;
        }

        @Override
        public KeyParams clone() {
            KeyParams params = new KeyParams();
            params.mKeyboard = mKeyboard;
            params.keyType = keyType;
            params.mCharacter = mCharacter;
            params.keyBgImage = keyBgImage;
            params.keyBgColor = keyBgColor;
            params.imageSrc = imageSrc;
            params.imageBackground = imageBackground;
            params.go2KeyboardType = go2KeyboardType;

            return params;
        }
    }
}
