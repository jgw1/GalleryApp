package com.example.galleryapp.Gallery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.ViewPagerAdapter;

import java.util.ArrayList;

public class OneImage extends AppCompatActivity {
    private ImageButton IB_GalleryBack;
    private ImageView IV_GalleryImage;
    private LinearLayout ll_GalleryLayout;
    private ToggleButton TB_SetFavorite;
    private TextView TV_GalleryHashtag1,TV_GalleryHashtag2,TV_GalleryHashtag3;
    private int position;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ArrayList<GalleryModel> videoArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image);

        viewPager = (ViewPager)findViewById(R.id.view);
        position = getIntent().getIntExtra(GalleryAppCode.Position,position);
        videoArrayList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);

        viewPagerAdapter = new ViewPagerAdapter(this,videoArrayList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);


    }

}
