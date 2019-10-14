package com.example.galleryapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

public class HashtagCustomDialog extends Dialog {
    private Activity activity;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private EditText hashtag1,hashtag2,hashtag3;
    private GalleryDBAccess galleryDBAccess;
    private HashtagCustomDialog hashtagCustomDialog;
    private Context mContext;
    private View.OnClickListener mPositiveListener,mNegativeListener;
    private ArrayList<GalleryModel> arrayList;
    public HashtagCustomDialog(@NonNull Activity activity) {
        super(activity);
        this.activity=activity;

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
        setContentView(R.layout.hashtag_custom_dialog);




        //셋팅
        mPositiveButton=(Button)findViewById(R.id.pbutton);
        mNegativeButton=(Button)findViewById(R.id.nbutton);

        hashtag1 = findViewById(R.id.hashtag1);
        hashtag2 = findViewById(R.id.hashtag2);
        hashtag3 = findViewById(R.id.hashtag3);

        hashtag1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        hashtag2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        hashtag3.setImeOptions(EditorInfo.IME_ACTION_DONE);

//        hashtag3.setOnEditorActionListener((textView, actionId, keyEvent) -> {
//            if(actionId == EditorInfo.IME_ACTION_DONE)
//            {
//                EditText hashtag1 = findViewById(R.id.hashtag1);
//                EditText hashtag2 = findViewById(R.id.hashtag2);
//                EditText hashtag3 = findViewById(R.id.hashtag3);
//
//                String Hashtag1 = hashtag1.getText().toString();
//                String Hashtag2 = hashtag2.getText().toString();
//                String Hashtag3 = hashtag3.getText().toString();
//
//                ArrayList<Double> LatLng = Location.GetCurrentLocation(activity.getApplicationContext());
//                galleryDBAccess.InsertData(LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
//
//                return true;
//            }
//            return false;
//        });

        mPositiveButton.setOnClickListener(mpositiveListener);
        mNegativeButton.setOnClickListener(negativeListener);
    }


    private View.OnClickListener mpositiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            galleryDBAccess.open();

            String Hashtag1 = hashtag1.getText().toString();
            String Hashtag2 = hashtag2.getText().toString();
            String Hashtag3 = hashtag3.getText().toString();
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getContext());
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest";
            File file = new File(String.valueOf(FileModule.latestFileModified(path)));
            galleryDBAccess.InsertData(file.getName(),LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
            galleryDBAccess.close();
            dismiss();

            hashtag1.setText(null);
            hashtag2.setText(null);
            hashtag3.setText(null);
        }
    };

    private View.OnClickListener negativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest";
            File file = new File(String.valueOf(FileModule.latestFileModified(path)));
            String file_name = file.getName();
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getContext());
            galleryDBAccess.open();
            galleryDBAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),"","","");
            galleryDBAccess.close();
            dismiss();

            hashtag1.setText(null);
            hashtag2.setText(null);
            hashtag3.setText(null);
        }
    };




}
