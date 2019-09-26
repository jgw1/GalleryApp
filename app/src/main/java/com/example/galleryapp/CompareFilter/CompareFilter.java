package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.CompareFilterAdapter;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;

public class CompareFilter extends AppCompatActivity implements  FiltersListFragment.FiltersListFragmentListener{
    private RecyclerView RV_FilterList;
    private Bitmap originalImage, filteredImage,finalImage;
    private ImageView IV_LeftImage;
    private ViewPager viewPager;
    private String ImagePath;
    private FiltersListFragment filtersListFragment;
    static{
        System.loadLibrary("NativeImageProcessor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_compare_filter);
        ImagePath = getIntent().getStringExtra(GalleryAppCode.GoToFilterPath);
        setupViewPager(viewPager);
        initComponents();

    }

    private void setupViewPager(ViewPager viewPager) {
        CompareFilterAdapter adapter = new CompareFilterAdapter(getSupportFragmentManager());

        // adding filter list fragment
        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        // adding edit image fragment

        adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));

        viewPager.setAdapter(adapter);
    }



    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        IV_LeftImage = findViewById(R.id.leftImage);
        File ImageFIle = new File(ImagePath);
        IV_LeftImage.setImageURI(Uri.fromFile(ImageFIle));

        viewPager = findViewById(R.id.viewpager);

        IV_LeftImage.setOnTouchListener(new OnSwipeTouchListener(this) {

            //오른쪽방향 스와이프 - 필터변경
            public void onSwipeRight() {
            }

            //왼쪽방향 스와이프 - 필터변경
            public void onSwipeLeft() {
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        // reset image controls


        // applying the selected filter
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        IV_LeftImage.setImageBitmap(filter.processFilter(filteredImage));

        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
    }
}
