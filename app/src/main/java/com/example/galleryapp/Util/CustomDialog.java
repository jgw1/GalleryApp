package com.example.galleryapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.R;

import java.util.ArrayList;

public class CustomDialog extends Dialog {
    private Activity activity;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private EditText hashtag1,hashtag2,hashtag3;
    private DatabaseAccess databaseAccess;
    private CustomDialog customDialog;
    private Context mContext;
    private View.OnClickListener mPositiveListener,mNegativeListener;

    public CustomDialog(@NonNull Activity activity) {
        super(activity);
        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        customDialog = new CustomDialog(activity);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        mContext = activity.getApplicationContext();
        setContentView(R.layout.custom_dialog);

        //셋팅
        mPositiveButton=(Button)findViewById(R.id.pbutton);
        mNegativeButton=(Button)findViewById(R.id.nbutton);

        hashtag1 = findViewById(R.id.hashtag1);
        hashtag2 = findViewById(R.id.hashtag2);
        hashtag3 = findViewById(R.id.hashtag3);

        hashtag1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        hashtag2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        hashtag3.setImeOptions(EditorInfo.IME_ACTION_DONE);

        hashtag3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    EditText hashtag1 = findViewById(R.id.hashtag1);
                    EditText hashtag2 = findViewById(R.id.hashtag2);
                    EditText hashtag3 = findViewById(R.id.hashtag3);

                    String Hashtag1 = hashtag1.getText().toString();
                    String Hashtag2 = hashtag2.getText().toString();
                    String Hashtag3 = hashtag3.getText().toString();

                    ArrayList<Double> LatLng = Location.GetCurrentLocation(activity.getApplicationContext());
                    databaseAccess.InsertData(LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
                    customDialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);
    }
    private View.OnClickListener positiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText hashtag1 = customDialog.findViewById(R.id.hashtag1);
            EditText hashtag2 = customDialog.findViewById(R.id.hashtag2);
            EditText hashtag3 = customDialog.findViewById(R.id.hashtag3);

            String Hashtag1 = hashtag1.getText().toString();
            String Hashtag2 = hashtag2.getText().toString();
            String Hashtag3 = hashtag3.getText().toString();

            ArrayList<Double> LatLng = Location.GetCurrentLocation(getContext());
            databaseAccess.InsertData(LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
            customDialog.dismiss();
        }
    };
    private View.OnClickListener negativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getContext());
            databaseAccess.InsertData(LatLng.get(0),LatLng.get(1),"","","");
            customDialog.dismiss();
        }
    };


}
