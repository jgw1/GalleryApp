package com.example.galleryapp.Gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OneImageViewPagerAdapter;

import java.util.ArrayList;

public class OneImage extends AppCompatActivity {

    private LinearLayout ll_TopGalleryLayout, ll_BottomGalleryLayout;
    private int InitPosition;
    private GalleryModel galleryModel;
    OneImageViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ArrayList<GalleryModel> ImageList;

    public OneImage() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image);

        InitPosition = getIntent().getIntExtra(GalleryAppCode.Position, InitPosition);
        ImageList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);
        galleryModel = ImageList.get(InitPosition);
        InitComponents();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitComponents() {
        viewPager = findViewById(R.id.view);
        viewPagerAdapter = new OneImageViewPagerAdapter(this, viewPager, ImageList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(InitPosition);
        viewPager.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:

                    // 터치 이벤트
                    //millsinFuture - 단위 : ms, countDownInterval - 단위 : ms
                    //millsinFuture 동안 countDownInterval 마다 실행하겠다라는 뜻이다.
                    CountDownTimer CDT = new CountDownTimer(5 * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            ll_TopGalleryLayout = view.findViewById(R.id.OneImage_Topnavigation);
                            ll_TopGalleryLayout.setVisibility(View.VISIBLE);
                            ll_TopGalleryLayout.bringToFront();

                            ll_BottomGalleryLayout = view.findViewById(R.id.OneImage_BottomNavigation);
                            ll_BottomGalleryLayout.setVisibility(View.VISIBLE);
                            ll_BottomGalleryLayout.bringToFront();
                        }
                        public void onFinish() {
                            ll_TopGalleryLayout.setVisibility(View.INVISIBLE);
                            ll_BottomGalleryLayout.setVisibility(View.INVISIBLE);
                        }
                    };
                    CDT.start(); //CountDownTimer 실행
                    break;
            }
            return false;
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}