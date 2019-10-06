package com.example.galleryapp.CompareFilter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.CustomDialog;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.example.galleryapp.Util.Thumbnail;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TwoFilter extends AppCompatActivity {
    private Bitmap originalImage, filteredImage,finalImage;
    private ImageView IV_LeftImage;
    private ViewPager viewPager;
    private String ImagePath,Hashtag1,Hashtag2,Hashtag3;
    private File ImageFile;
    private Filter Selectedfilter;
    private CustomDialog customDialog;
    private GalleryDBAccess galleryDBAccess;
    private ImageButton IB_SaveFilterImage;
    private TextView TV_FilterName;

    ArrayList<GalleryModel> ImageList;
    private int InitPosition;
    private FiltersListFragment filtersListFragment;
    private EditImageFragment editImageFragment;
    static{
        System.loadLibrary("NativeImageProcessor");
    }
    public static String File_Name;

    public TwoFilter() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_compare_filter);
        File_Name = getIntent().getStringExtra(GalleryAppCode.GoToFilterPath);
        ImageFile = new File(GalleryAppCode.Path+File_Name);
        initComponents();
        loadImage();
        setupViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        CompareFilterAdapter adapter = new CompareFilterAdapter(getSupportFragmentManager());

        // adding filter list fragment
        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this::onFilterSelected);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);
        // adding edit image fragment

        adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));
        adapter.addFragment(editImageFragment, getString(R.string.tab_edit));
        viewPager.setAdapter(adapter);
    }

    class CompareFilterAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public CompareFilterAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        viewPager.setVisibility(View.INVISIBLE);



    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        // reset image controls
        Bitmap bitmap = BitmapFactory.decodeFile(ImageFile.getPath());
//        originalImage = BitmapUtils.getBitmapFromAssets(this, File_Name, 300, 300);
        // applying the selected filter
        filteredImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        IV_LeftImage.setImageBitmap(filter.processFilter(filteredImage));


    }

    private void loadImage(){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageFile.getPath());
//        originalImage = BitmapUtils.getBitmapFromGallery(this,Uri.fromFile(ImageFile),300,300);
        filteredImage = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalImage = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        IV_LeftImage.setImageBitmap(bitmap);
    }

}
