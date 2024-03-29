package com.example.galleryapp.Gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.galleryapp.CompareFilter.FilterModel;
import com.example.galleryapp.DB.FilterDBAccess;
import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.galleryapp.CompareFilter.OneFilter.File_Name;

public class GalleryFragment extends AppCompatActivity implements AlbumTotal.AlbumTotalFragmentInteractionListener,AlbumDay.AlbumDayFragmentInteractionListener{

    ViewPager pager;
    private AlbumTotal albumTotal;
    private AlbumDay albumDay;
    private AlbumMap albumMap;
    private ArrayList<GalleryModel> List_GalleryTotal;
    private GalleryDBAccess galleryDBAccess;
    private Context context;
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
        context = getApplicationContext();
        List_GalleryTotal = new ArrayList<>();
        this.galleryDBAccess = GalleryDBAccess.getInstance(this);
        galleryDBAccess.open();
        List_GalleryTotal = galleryDBAccess.getDataForMap();
        galleryDBAccess.close();

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(3);
        View.OnClickListener movePageListener = view -> {
            int tag = (int) view.getTag();
            pager.setCurrentItem(tag);
        };

        albumDay = new AlbumDay(context,List_GalleryTotal);
        albumTotal = new AlbumTotal(context,List_GalleryTotal);
        albumMap = new AlbumMap(context,List_GalleryTotal);

        NewAlbumDay.setOnClickListener(movePageListener);
        NewAlbumDay.setTag(0);

        NewAlbumTotal.setOnClickListener(movePageListener);
        NewAlbumTotal.setTag(1);

        NewAlbumMap.setOnClickListener(movePageListener);
        NewAlbumMap.setTag(2);

    }

    @Override
    public void onAlbumtotalFragmentInteraction(ArrayList<GalleryModel> arrayList) {
        albumDay.setData(arrayList);
    }

    @Override
    public void onAlbumdayFragmentInteraction(ArrayList<GalleryModel> arrayList) {
        albumTotal.setData(arrayList);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
      public pagerAdapter(FragmentManager fm){
            super(fm);

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
