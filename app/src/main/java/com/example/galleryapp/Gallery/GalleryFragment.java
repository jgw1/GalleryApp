package com.example.galleryapp.Gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.galleryapp.R;

public class GalleryFragment extends AppCompatActivity {
    ViewPager pager;

    ImageButton NewAlbumDay;
    ImageButton NewAlbumTotal;
    ImageButton NewAlbumMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_fragment);
        pager = findViewById(R.id.pager);

        NewAlbumDay = findViewById(R.id.NewAlbumDay);
        NewAlbumTotal = findViewById(R.id.NewAlbumTotal);
        NewAlbumMap = findViewById(R.id.NewAlbumMap);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(3);
        View.OnClickListener movePageListener = view -> {
            int tag = (int) view.getTag();
            pager.setCurrentItem(tag);
        };

        NewAlbumDay.setOnClickListener(movePageListener);
        NewAlbumDay.setTag(0);
        NewAlbumTotal.setOnClickListener(movePageListener);
        NewAlbumTotal.setTag(1);
        NewAlbumMap.setOnClickListener(movePageListener);
        NewAlbumMap.setTag(2);

    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        private AlbumTotal albumTotal;
        private AlbumDay albumDay;
        private AlbumMap albumMap;
        public pagerAdapter(FragmentManager fm){
            super(fm);
            albumTotal = new AlbumTotal();
            albumDay = new AlbumDay();
            albumMap = new AlbumMap();
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return albumTotal;
                case 1:
                    return albumDay;
                case 2:
                    return albumMap;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
