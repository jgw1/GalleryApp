package com.example.galleryapp.Util;

import android.os.Environment;

public class GalleryAppCode {
    public static final String Position = "Position";
    public static final String GalleryList = "GalleryList";
    public static final String Path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest/";
    // UI THREAD 해결 참고 - https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
    // Image Filter 관련 : https://www.androidhive.info/2017/11/android-building-image-filters-like-instagram/


//    Image Filter 예시
//    Bitmap.Config config = bitmap.getConfig();
//    Bitmap newBitmap = bitmap.copy(config,true);
//    Filter newFilter = FilterPack.getNightWhisperFilter(context);
//    //                myFilter.addSubFilter(new BrightnessSubFilter(30));
////                myFilter.addSubFilter(new ContrastSubFilter(1.1f));
//    Bitmap outputImage = newFilter.processFilter(newBitmap);
//                imageView.setImageBitmap(outputImage);

}
