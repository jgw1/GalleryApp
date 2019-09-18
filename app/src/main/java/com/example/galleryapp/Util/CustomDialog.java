package com.example.galleryapp.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.galleryapp.R;

public class CustomDialog extends Dialog {
    private Button mPositiveButton;
    private Button mNegativeButton;
    private EditText hashtag1,hashtag2,hashtag3;

    private View.OnClickListener mPositiveListener,mNegativeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.custom_dialog);

        //셋팅
        mPositiveButton=(Button)findViewById(R.id.pbutton);
        mNegativeButton=(Button)findViewById(R.id.nbutton);

        hashtag1 = findViewById(R.id.hashtag1);
        hashtag2 = findViewById(R.id.hashtag2);
        hashtag3 = findViewById(R.id.hashtag3);

        hashtag1.addTextChangedListener(new TextWatcher() {
            String CurrentText = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CurrentText = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(hashtag1.getLineCount()>=2){
                    hashtag1.setText(CurrentText);
                }
            }
        });











        hashtag1.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() ==KeyEvent.ACTION_DOWN)&& keyCode == KeyEvent.KEYCODE_ENTER ){
                    hashtag2.requestFocus();
                    return true;
                }
             return false;
            }
        });

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);
    }
    public CustomDialog(@NonNull Context context,View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;

    }
}
