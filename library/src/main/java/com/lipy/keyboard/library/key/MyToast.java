package com.lipy.keyboard.library.key;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 按键按下后的气泡
 * Created by lipy on 2014/7/6.
 */
public class MyToast {

    private final Handler handler = new Handler();
    private final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private int duration;
    private int gravity = Gravity.CENTER;
    private int mX, mY;
    private float horizontalMargin;
    private float verticalMargin;
    private View view;
    private View nextView;
    private boolean isShow;
    private WindowManager windowManager;
    private final Runnable mShow = new Runnable() {
        public void run() {
            handleShow();
        }
    };
    private final Runnable mHide = new Runnable() {
        public void run() {
            handleHide();
        }
    };


    public MyToast(Context context) {
        init(context);
    }

    public static MyToast makeView(Context context, View view, int duration) {
        MyToast result = new MyToast(context);
        result.setView(view);
        result.setDuration(duration);
        return result;

    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        LinearLayout view = new LinearLayout(context);
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        view.setBackgroundColor(Color.parseColor("#636f7e"));
        int w = context.getResources().getDisplayMetrics().widthPixels / 2;
        int h = context.getResources().getDisplayMetrics().widthPixels / 10;
        view.addView(tv, w, h);
        return makeView(context, view, duration);
    }

    public View getView() {
        return nextView;
    }

    public void setView(View view) {
        nextView = view;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        this.horizontalMargin = horizontalMargin;
        this.verticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return horizontalMargin;
    }

    public float getVerticalMargin() {
        return verticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        this.gravity = gravity;
        mX = xOffset;
        mY = yOffset;
    }

    public int getGravity() {
        return gravity;
    }

    public int getXOffset() {
        return mX;
    }

    public int getYOffset() {
        return mY;
    }

    public void show() {
        handler.post(mShow);
        if (duration > 0) {
            handler.postDelayed(mHide, duration);
        }
    }

    public void hide() {
        handler.post(mHide);
    }

    private void init(Context context) {
        final WindowManager.LayoutParams params = this.params;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
//        params.windowAnimations = android.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");

        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }


    private void handleShow() {
        try {
            if (view != nextView) {
                handleHide();
                view = nextView;
                final int gravity = this.gravity;
                params.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    params.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    params.verticalWeight = 1.0f;
                }
                params.x = mX;
                params.y = mY;
                params.verticalMargin = verticalMargin;
                params.horizontalMargin = horizontalMargin;
                if (view.getParent() != null) {
                    windowManager.removeView(view);
                }
                windowManager.addView(view, params);
                isShow = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleHide() {
        try {
            if (view != null) {
                if (view.getParent() != null) {
                    windowManager.removeView(view);
                }
                view = null;
                isShow = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShow() {
        return isShow;
    }
}
