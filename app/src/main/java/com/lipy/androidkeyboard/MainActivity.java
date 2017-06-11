package com.lipy.androidkeyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


import com.lipy.keyboard.library.SecureKeyBoard;
import com.lipy.keyboard.library.Utils;
import com.lipy.keyboard.library.keyboard.KeyboardParams;
import com.lipy.keyboard.library.keyboard.KeyboardType;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private SecureKeyBoard keyBoard;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_text);
        Utils.disableShowSoftInput(editText);
        editText.setOnTouchListener(this);
        findViewById(R.id.text1).setOnClickListener(this);
        findViewById(R.id.text2).setOnClickListener(this);
        findViewById(R.id.text3).setOnClickListener(this);
        findViewById(R.id.text4).setOnClickListener(this);
        findViewById(R.id.text5).setOnClickListener(this);
        initKeyBoard(KeyboardType.DECIMAL);
    }

    public void closeKeyBoard() {
        if (keyBoard.isShowing()) {
            keyBoard.dismissKeyBoard();
        }
    }

    private void initKeyBoard(KeyboardType keyboardType) {
        keyBoard = new SecureKeyBoard(this,
                new KeyboardParams(this, keyboardType), editText, 16);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int i = view.getId();
        if (i == R.id.edit_text) {
            if (!keyBoard.isShowing()) {
                keyBoard.showAtLocation(MainActivity.this.findViewById(R.id.edit_text),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        closeKeyBoard();
        KeyboardType keyboardType = KeyboardType.DECIMAL;
        switch (view.getId()) {
            case R.id.text1:
                keyboardType = KeyboardType.ENGLISH;
                break;
            case R.id.text2:
                keyboardType = KeyboardType.NUM_FULL;
                break;
            case R.id.text3:
                keyboardType = KeyboardType.NUM_PWD;
                break;
            case R.id.text4:
                keyboardType = KeyboardType.INTEGER;
                break;
            case R.id.text5:
                keyboardType = KeyboardType.DECIMAL;
                break;
        }
        initKeyBoard(keyboardType);
    }
}
