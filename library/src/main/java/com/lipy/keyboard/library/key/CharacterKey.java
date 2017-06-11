package com.lipy.keyboard.library.key;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lipy.keyboard.library.R;
import com.lipy.keyboard.library.Utils;


/**
 * 字符key
 * Created by lipy on 16/5/24.
 */
public class CharacterKey extends BaseKey {
    public CharacterKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterKey(Context context, KeyParams params, KeyListener listener) {
        super(context, params, listener);
    }

    public CharacterKey(Context context, AttributeSet attrs, KeyParams params, KeyListener
            listener) {
        super(context, attrs, params, listener);
    }

    @Override
    public void updateView(Context context) {
        super.updateView(context);
        TextView textView = (TextView) findViewById(R.id.key_tv);
        if (isAChar() && mKeyListener.isUpperCase()) {
            textView.setText(mParams.mCharacter.toUpperCase());
        } else {
            textView.setText(mParams.mCharacter);
        }
    }

    @Override
    public void updateView(Context context, String s) {
        super.updateView(context);
        mParams.mCharacter = s;
        TextView textView = (TextView) findViewById(R.id.key_tv);
        textView.setText(mParams.mCharacter);
    }

    @Override
    protected void showDialog(String text, String imageSrc) {
        if (isAChar() && mKeyListener.isUpperCase()) {
            text = text.toUpperCase();
        }

        super.showDialog(text, imageSrc);
    }

    @Override
    protected String getCharacter() {
        if (isAChar() && mKeyListener.isUpperCase()) {
            return mParams.mCharacter.toUpperCase();
        }
        return mParams.mCharacter;
    }

    private boolean isAChar() {
        return Utils.isNotEmpty(mParams.mCharacter) && mParams.mCharacter.length() == 1 &&
                "abcdefghijklmnopqrstuvwxyz".contains(mParams.mCharacter);
    }
}
