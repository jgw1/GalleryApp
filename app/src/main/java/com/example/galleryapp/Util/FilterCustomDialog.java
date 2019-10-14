package com.example.galleryapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.R;

import java.io.File;
import java.util.ArrayList;

public class FilterCustomDialog extends Dialog {
    private Activity activity;
    private Button mOkButton;
    private EditText hashtag1,hashtag2,hashtag3;
    private GalleryDBAccess galleryDBAccess;
    private FilterCustomDialog hashtagCustomDialog;
    private Context mContext;
    private View.OnClickListener okclicklistener;
    private ArrayList<GalleryModel> arrayList;
    public FilterCustomDialog(@NonNull Activity activity,View.OnClickListener okclicklistener) {
        super(activity);
        this.activity=activity;
        this.okclicklistener = okclicklistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryDBAccess = GalleryDBAccess.getInstance(activity);
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        mContext = activity.getApplicationContext();
        setContentView(R.layout.filter_custom_dialog);

        //셋팅
        mOkButton=(Button)findViewById(R.id.okbutton);
        mOkButton.setOnClickListener(okclicklistener);
    }

}
