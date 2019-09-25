package com.example.galleryapp.Gallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.example.galleryapp.Util.ViewPagerAdapter;

import java.util.ArrayList;

public class OneImage extends AppCompatActivity {
    private ImageButton IB_GalleryBack;
    private ImageView IV_GalleryImage;
    private LinearLayout ll_TopGalleryLayout,ll_BottomGalleryLayout;
    private ToggleButton TB_SetFavorite;
    private TextView TV_GalleryHashtag1,TV_GalleryHashtag2,TV_GalleryHashtag3;
    private int InitPosition;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ArrayList<GalleryModel> ImageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image);

        InitPosition = getIntent().getIntExtra(GalleryAppCode.Position, InitPosition);
        ImageList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);

        InitComponents();



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GalleryModel galleryModel = ImageList.get(position);
                TV_GalleryHashtag1.setText(galleryModel.getHashtag1());
                TV_GalleryHashtag2.setText(galleryModel.getHashtag2());
                TV_GalleryHashtag3.setText(galleryModel.getHashtag3());
                Log.d("GWGWGW","Position" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void InitComponents(){
        viewPager = findViewById(R.id.view);
        ll_TopGalleryLayout = findViewById(R.id.OneImage_Topnavigation);
        ll_BottomGalleryLayout = findViewById(R.id.OneImage_BottomNavigation);

        ll_TopGalleryLayout.setVisibility(View.INVISIBLE);
        ll_BottomGalleryLayout.setVisibility(View.INVISIBLE);


        TV_GalleryHashtag1 = findViewById(R.id.Gallery_Hashtag1);
        TV_GalleryHashtag2 = findViewById(R.id.Gallery_Hashtag2);
        TV_GalleryHashtag3 = findViewById(R.id.Gallery_Hashtag3);
        TB_SetFavorite = findViewById(R.id.SetFavorite);

        viewPagerAdapter = new ViewPagerAdapter(this, ImageList);
        viewPager.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                   // 터치 이벤트
                    CountDownTimer CDT = new CountDownTimer(2 * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            ll_TopGalleryLayout.setVisibility(View.VISIBLE);
                            ll_TopGalleryLayout.bringToFront();

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
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(InitPosition);

        GalleryModel galleryModel = ImageList.get(InitPosition);
        TV_GalleryHashtag1.setText(galleryModel.getHashtag1());
        TV_GalleryHashtag2.setText(galleryModel.getHashtag2());
        TV_GalleryHashtag3.setText(galleryModel.getHashtag3());

    }



}
