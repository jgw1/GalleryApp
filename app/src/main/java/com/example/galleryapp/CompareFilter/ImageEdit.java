package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.FileModule;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.google.android.material.tabs.TabLayout;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.galleryapp.CompareFilter.OneFilter.File_Name;
import static com.example.galleryapp.Util.Animation.slideDown;
import static com.example.galleryapp.Util.Animation.slideUp;

public class ImageEdit extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener {
    private ImageView leftImage, rightImage;
    private int  leftbrightnessFinal,rightbrightnessFinal,initbrightness = 0;
    private float leftsaturationFinal,rightsaturationFinal,initsaturation = 1.0f;
    private float leftcontrastFinal,rightcontrastFinal,initconstrast = 1.0f;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private Bitmap  originalImage,filteredImage, Left_previewImage,scaleDownImage, Right_previewImage,Left_finalImage,Right_finalImage;
    private Filter left_filter,right_filter;
    private File ImageFile;
    private FileModule.SaveImage saveImage;
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


        Log.d("LeftFilter", "LeftFilter : " + left_filter);

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

        left_filter = new Filter();
        right_filter = new Filter();

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

            public void onLongPressed(){
                TotalImage(originalImage,left_filter);
                Log.d("AAFSS","AAF");
            }
        });

        rightImage.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                CurrentChangePicture(GalleryAppCode.GoRight);
                filtersListFragment.FilterSwipe(GalleryAppCode.GoRight,GalleryAppCode.GoLeft,tv_leftFiltername);
                Log.d("FILTERCHECK","CURRENTFILTER INDEX " + filtersListFragment.getCurrentFilter());
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

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            leftbrightnessFinal = brightness;
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            rightbrightnessFinal = brightness;
        }

    }

    @Override
    public void onSaturationChanged(float saturation) {

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            leftsaturationFinal=saturation;
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            rightsaturationFinal=saturation;
        }

    }

    @Override
    public void onContrastChanged(float contrast) {
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));

        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            leftcontrastFinal = contrast;
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            rightcontrastFinal = contrast;
        }

    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
    }

    private void loadImage() {
        left_filter = new Filter();


        originalImage = BitmapFactory.decodeFile(ImageFile.getPath());
        originalImage = originalImage.copy(Bitmap.Config.ARGB_8888,true);


        scaleDownImage = BitmapUtils.resize(getApplicationContext(), Uri.fromFile(ImageFile),300);
        filteredImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);

        Left_previewImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);
        Right_previewImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);

        leftImage.setImageBitmap(Left_previewImage);
        rightImage.setImageBitmap(Right_previewImage);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControls();

//        bitmap =  Bitmap.createScaledBitmap(bitmap,300 , 300, false);
        filteredImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);
        if(Current_LeftImage){
            Left_previewImage = filter.processFilter(filteredImage);
            leftImage.setImageBitmap(Left_previewImage);
            left_filter = filter;

        }else if(Current_RightImage){
            Right_previewImage = filter.processFilter(filteredImage);
            rightImage.setImageBitmap(Right_previewImage);
            right_filter = filter;
        }

    }
    private void resetControls() {
        if (editImageFragment != null) {
            editImageFragment.resetControls();
        }

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

    private void TotalImage(Bitmap Image, Filter filter){
        new Thread(new Runnable() {

            @Override
            public void run() {
                Bitmap bitmap  = filter.processFilter(Image);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] currentData = stream.toByteArray();

                //파일로 저장
                saveImage = new FileModule.SaveImage(ImageEdit.this);
                saveImage.execute(currentData);
            }
        }).start();
        Toast.makeText(getApplicationContext(),"CHECK THE FILE",Toast.LENGTH_LONG).show();
    }




}