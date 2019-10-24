package com.example.galleryapp.Util;

import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.galleryapp.CompareFilter.ImageEdit;

public class GalleryAppCode {
    public static final String Position = "Position";
    public static final String GalleryList = "GalleryList";
    public static final String Path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest/";
    public static final String GoToFilterPath = "GoToFilterPath";
    public static final String GoLeft = "LEFT";
    public static final String GoRight = "RIGHT";
    public static final String OneImage = "ONE";
    public static final String TwoImage = "TWO";
    public static final String EditOneImage = "이미지 1개로 단순 필터 적용";
    public static final String EditTwoImage = "이미지 2개로 비교하며 필터 적용";
    // Camera 관련 참고 : https://yeolco.tistory.com/45?category=757621
    // UI THREAD 해결 참고 - https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
    // ImageView - URI VS Bitmap - https://stackoverflow.com/questions/41430796/difference-between-uri-and-bitmap-image


     // 선택 Dialog 형성
//    String[] listItems = {"one", "two", "three", "four", "five"};
//
//    AlertDialog.Builder builder = new AlertDialog.Builder("Activity 이름".this);
//
//           builder.setItems(listItems, (dialog, position) -> Toast.makeText("Activity 이름".this, "Position: " + position + " Value: " + listItems[position], Toast.LENGTH_LONG).show());
//
//    AlertDialog dialog = builder.create();
//           dialog.show();
}
