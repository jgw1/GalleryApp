package com.example.galleryapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.R;

import java.util.ArrayList;

public class ImageCustomDialog extends Dialog {
    private Activity activity;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private ImageCustomDialog imageCustomDialog;
    private Context mContext;
    private View.OnClickListener mPositiveListener,mNegativeListener;
    private ArrayList<String> arrayList;
    public ImageCustomDialog(@NonNull Activity activity) {
        super(activity);
        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        mContext = activity.getApplicationContext();
        setContentView(R.layout.image_custom_dialog);
        imageCustomDialog = new ImageCustomDialog(activity);

        //셋팅
        mPositiveButton=(Button)findViewById(R.id.yesbutton);
        mNegativeButton=(Button)findViewById(R.id.nobutton);

        mPositiveButton.setOnClickListener(mpositiveListener);
        mNegativeButton.setOnClickListener(negativeListener);
    }
    private View.OnClickListener mpositiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            dismiss();
        }
    };
    private View.OnClickListener negativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imageCustomDialog.cancel();
        }
    };





}
