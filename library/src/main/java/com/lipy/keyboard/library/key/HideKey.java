package com.lipy.keyboard.library.key;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipy.keyboard.library.R;
import com.lipy.keyboard.library.Utils;
import com.lipy.keyboard.library.keyboard.KeyboardUtil;

/**
 * 按键类，一个实例代表一个自定义键盘中的按键
 * Created by lipy on 2016/5/19.
 */
public class HideKey extends BaseKey implements View.OnClickListener {

    Bitmap bitmap;

    public HideKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 构造函数，需要按键参数和回调接口
    public HideKey(Context context, KeyParams params, KeyListener listener) {
        this(context, null, params, listener);
    }

    // 构造函数，需要按键参数和回调接口
    public HideKey(Context context, AttributeSet attrs, KeyParams params, KeyListener listener) {
        super(context, attrs, params, listener);
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
            bitmap = KeyboardUtil.readBitMap(context, R.drawable.class, mParams.imageSrc);
            imageView.setImageBitmap(bitmap);
            if (mParams.imageBackground > 0) {
                setBackgroundResource(mParams.imageBackground);
            }
            imageView.setVisibility(VISIBLE);
            textView.setVisibility(GONE);
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        if (Utils.isEmpty(mParams.mCharacter) && Utils.isNotEmpty(mParams.imageSrc) && bitmap != null) {
            View v = findViewById(R.id.key_img);
            ViewGroup.LayoutParams lp = v.getLayoutParams();

            lp.width = (int)(params.width / 2.6);
            lp.height = (int)(lp.width * (bitmap.getHeight() / (bitmap.getWidth() * 1.0)));
            v.setLayoutParams(lp);
        }
    }
}
