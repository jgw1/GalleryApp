package com.example.galleryapp.Util;

import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class GalleryAppCode {
    public static final String Position = "Position";
    public static final String GalleryList = "GalleryList";
    public static final String Path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest/";
    public static final String GoToFilterPath = "GoToFilterPath";
    public static final String GoLeft = "LEFT";
    public static final String GoRight = "RIGHT";
    // Camera 관련 참고 : https://yeolco.tistory.com/45?category=757621
    // UI THREAD 해결 참고 - https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
    // ImageView - URI VS Bitmap - https://stackoverflow.com/questions/41430796/difference-between-uri-and-bitmap-image
}
