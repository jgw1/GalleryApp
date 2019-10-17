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

import com.example.galleryapp.CompareFilter.FilterModel;
import com.example.galleryapp.CompareFilter.ImageEdit;
import com.example.galleryapp.DB.FilterDBAccess;
import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.R;

import java.io.File;
import java.util.ArrayList;

public class FilterCustomDialog extends Dialog {
    private Activity activity;
    private Button mOkButton;
    private FilterDBAccess filterDBAccess;
    private Context mContext;
    private ArrayList<FilterModel> filterModel;
    public FilterCustomDialog(@NonNull Activity activity,ArrayList<FilterModel> filterModel) {
        super(activity);
        this.activity=activity;
        this.filterModel=filterModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterDBAccess = FilterDBAccess.getInstance(activity);
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        mContext = activity.getApplicationContext();
        setContentView(R.layout.filter_custom_dialog);

        //셋팅
        mOkButton= findViewById(R.id.okbutton);
        OKclickListener oKclickListener = new OKclickListener(filterModel);
        mOkButton.setOnClickListener(oKclickListener);
    }
    public class OKclickListener implements View.OnClickListener {

        private ArrayList<FilterModel> Filter;

        public OKclickListener(ArrayList<FilterModel> filter) {

            this.Filter = filter;

        }
        @Override
        public void onClick(View view) {
            EditText editText = findViewById(R.id.customfiltername);
            Filter.get(0).setFiltername(editText.getText().toString());
            filterDBAccess.open();
            filterDBAccess.InsertData(Filter);
            filterDBAccess.close();
            dismiss();
            HashtagCustomDialog hashtagCustomDialog = new HashtagCustomDialog(activity);
            hashtagCustomDialog.show();
        }
    }

}
