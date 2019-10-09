package com.example.galleryapp.CompareFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.google.android.material.tabs.TabLayout;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.galleryapp.CompareFilter.OneFilter.File_Name;

public class ImageEdit extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener {
    private ImageView leftImage, rightImage;
    private int  brightnessFinal = 0;
    private float saturationFinal = 1.0f;
    private float contrastFinal = 1.0f;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private Bitmap  originalImage,filteredImage,Left_finalImage,scaleDownImage,Right_finalImage;
    private File ImageFile;
    private boolean Current_LeftImage, Current_RightImage = false;
    private TextView tv_leftFiltername,tv_rightFilterName;
    private RelativeLayout relativeLayout;
    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    static{
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        File_Name = getIntent().getStringExtra(GalleryAppCode.GoToFilterPath);
        ImageFile = new File(GalleryAppCode.Path+File_Name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filters");

        initComponents();

        loadImage();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager) {
        FilterPagerAdapter adapter = new FilterPagerAdapter(getSupportFragmentManager());

        // adding filter list fragment
        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        // adding edit image fragment
        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));
        adapter.addFragment(editImageFragment, getString(R.string.tab_edit));

        viewPager.setAdapter(adapter);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        leftImage = findViewById(R.id.image_preview);
        rightImage = findViewById(R.id.RightImage);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.nonviewpager);


        leftImage.bringToFront();
        rightImage.bringToFront();

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        relativeLayout = findViewById(R.id.filterlayout);
        relativeLayout.setVisibility(View.INVISIBLE);

        tv_leftFiltername=findViewById(R.id.leftFilterName);
        tv_rightFilterName=findViewById(R.id.rightFilterName);
        leftImage.setOnTouchListener(new OnSwipeTouchListener(this) {

            //왼쪽방향 스와이프 - 필터변경
            public void onSwipeLeft() {
                CurrentChangePicture(GalleryAppCode.GoLeft);
                filtersListFragment.FilterSwipe(GalleryAppCode.GoLeft,GalleryAppCode.GoLeft,tv_leftFiltername);
            }

            //오른쪽방향 스와이프 - 필터변경
            public void onSwipeRight() {
                CurrentChangePicture(GalleryAppCode.GoLeft);
                filtersListFragment.FilterSwipe(GalleryAppCode.GoLeft,GalleryAppCode.GoRight,tv_leftFiltername);
            }

            //변경할 이미지 선택
            public void onDoubleTouch(){
                CurrentChangePicture(GalleryAppCode.GoLeft);
                resetControls();
                filtersListFragment.FilterPositionChange(GalleryAppCode.GoLeft);
                Log.d("Boolean check","LEFTIMAGE : " + Current_LeftImage);
                Log.d("Boolean check","RIGHTIMAGE : " + Current_RightImage);

            }
            public void onSwipeTop() {
                CurrentChangePicture(GalleryAppCode.GoLeft);
                slideUp(relativeLayout);
            }
            public void onSwipeBottom(){
                CurrentChangePicture(GalleryAppCode.GoLeft);
                slideDown(relativeLayout);

            }
        });
        rightImage.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                CurrentChangePicture(GalleryAppCode.GoRight);
                filtersListFragment.FilterSwipe(GalleryAppCode.GoRight,GalleryAppCode.GoLeft,tv_leftFiltername);
            }

            //왼쪽방향 스와이프 - 필터변경
            public void onSwipeRight() {
                CurrentChangePicture(GalleryAppCode.GoRight);
                filtersListFragment.FilterSwipe(GalleryAppCode.GoRight,GalleryAppCode.GoRight,tv_leftFiltername);
            }
            //오른쪽방향 스와이프 - 필터변경
            public void onDoubleTouch(){
                CurrentChangePicture(GalleryAppCode.GoRight);
                resetControls();
                filtersListFragment.FilterPositionChange(GalleryAppCode.GoRight);
                Log.d("Boolean check","LEFTIMAGE : " + Current_LeftImage);
                Log.d("Boolean check","RIGHTIMAGE : " + Current_RightImage);

            }
            public void onSwipeTop() {
                CurrentChangePicture(GalleryAppCode.GoRight);
                slideUp(relativeLayout);
            }
            public void onSwipeBottom(){
                CurrentChangePicture(GalleryAppCode.GoRight);
                slideDown(relativeLayout);
            }
        });

    }

    public void CurrentChangePicture(String Direction){
        if(Direction == GalleryAppCode.GoLeft){
            Current_LeftImage = true;
            Current_RightImage = false;
//           rightImage.setAlpha(0.1f);
//           leftImage.setAlpha(1f);

        }else if(Direction == GalleryAppCode.GoRight){
            Current_LeftImage = false;
            Current_RightImage = true;
//           leftImage.setAlpha(0.1f);
//           rightImage.setAlpha(1f);
        }

    }
    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }

        Log.d("Filter", "Brightness : " + brightnessFinal);
        Log.d("Filter", "saturation : " + saturationFinal);
        Log.d("Filter", "contrast : " + contrastFinal);
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.d("Filter", "Brightness : " + brightnessFinal);
        Log.d("Filter", "saturation : " + saturationFinal);
        Log.d("Filter", "contrast : " + contrastFinal);
    }

    @Override
    public void onContrastChanged(float contrast) {
        contrastFinal = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));

        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.d("Filter", "Brightness : " + brightnessFinal);
        Log.d("Filter", "saturation : " + saturationFinal);
        Log.d("Filter", "contrast : " + contrastFinal);
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        final Bitmap bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(contrastFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        scaleDownImage = myFilter.processFilter(bitmap);
    }
    private void loadImage() {
        originalImage = BitmapFactory.decodeFile(ImageFile.getPath());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;
        originalImage = BitmapFactory.decodeFile(ImageFile.getPath());
        scaleDownImage  =  BitmapFactory.decodeFile(ImageFile.getPath(),options);


//        originalImage = BitmapUtils.getBitmapFromGallery(this,Uri.fromFile(ImageFile),300,300);

        filteredImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);

        Left_finalImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);
        Right_finalImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);

        leftImage.setImageBitmap(scaleDownImage);
        rightImage.setImageBitmap(scaleDownImage);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControls();

//        bitmap =  Bitmap.createScaledBitmap(bitmap,300 , 300, false);
        filteredImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);
        if(Current_LeftImage){
            leftImage.setImageBitmap(filter.processFilter(filteredImage));
            Left_finalImage = filter.processFilter(filteredImage);
        }else if(Current_RightImage){
            rightImage.setImageBitmap(filter.processFilter(filteredImage));
            Right_finalImage = filter.processFilter(filteredImage);
        }





    }
    private void resetControls() {
        if (editImageFragment != null) {
            editImageFragment.resetControls();
        }
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        contrastFinal = 1.0f;
    }
    class FilterPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public FilterPagerAdapter(FragmentManager manager) {
            super(manager);
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
    public void slideUp(RelativeLayout relativeLayout){
        relativeLayout.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                relativeLayout.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        relativeLayout.startAnimation(animate);
        relativeLayout.bringToFront();

    }
    public void slideDown(RelativeLayout relativeLayout){
        relativeLayout.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                relativeLayout.getHeight());                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        relativeLayout.startAnimation(animate);
    }
}