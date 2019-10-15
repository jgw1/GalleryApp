package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
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

import com.example.galleryapp.DB.FilterDBAccess;
import com.example.galleryapp.GalleryTotal.GalleryTotalAdapter;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.FileModule;
import com.example.galleryapp.Util.FilterCustomDialog;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.HashtagCustomDialog;
import com.example.galleryapp.Util.ImageCustomDialog;
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
    private FilterDBAccess filterDBAccess;
    private int  leftbrightnessFinal,rightbrightnessFinal,initbrightness = 0;
    private float leftsaturationFinal,rightsaturationFinal,initsaturation = 1.0f;
    private float leftcontrastFinal,rightcontrastFinal,initconstrast = 1.0f;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private Bitmap  originalImage,filteredImage, Left_previewImage,scaleDownImage, Right_previewImage,Left_finalImage,Right_finalImage;
    private Filter leftimage_filter, rightimage_filter;
    private File ImageFile;
    private FileModule.SaveImage saveImage;
    private boolean Current_LeftImage = true;
    private boolean Current_RightImage = false;

    private TextView tv_leftFiltername,tv_rightFilterName;
    private RelativeLayout relativeLayout;
    private ImageCustomDialog imageCustomDialog;
    private FilterCustomDialog filterCustomDialog;
    private ArrayList<FilterModel> leftimage_DBfilter, rightimage_DBfilter;
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
        viewPager = findViewById(R.id.nonviewpager);
        setupViewPager(viewPager);


        initComponents();

        loadImage();

        tabLayout.setupWithViewPager(viewPager);


        Log.d("LeftFilter", "LeftFilter : " + leftimage_filter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

//        leftImage.bringToFront();
//        rightImage.bringToFront();
        filterDBAccess = FilterDBAccess.getInstance(getApplicationContext());

        leftimage_filter = new Filter();
        rightimage_filter = new Filter();

        leftimage_DBfilter =  FilterModel.initfilterModel();
        rightimage_DBfilter = FilterModel.initfilterModel();

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        relativeLayout = findViewById(R.id.filterlayout);
        relativeLayout.setVisibility(View.VISIBLE);

        tv_leftFiltername=findViewById(R.id.leftFilterName);
        tv_rightFilterName=findViewById(R.id.rightFilterName);

        SwipeListener Left_swipeListener = new SwipeListener(getApplicationContext(),GalleryAppCode.GoLeft,tv_leftFiltername,filtersListFragment,relativeLayout,leftimage_DBfilter,leftimage_filter);
        SwipeListener Right_swipeListener = new SwipeListener(getApplicationContext(),GalleryAppCode.GoRight,tv_rightFilterName,filtersListFragment,relativeLayout,rightimage_DBfilter,rightimage_filter);

        leftImage.setOnTouchListener(Left_swipeListener);
        rightImage.setOnTouchListener(Right_swipeListener);


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

            leftimage_DBfilter.get(0).setBrightness(brightness);
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            rightimage_DBfilter.get(0).setBrightness(brightness);

        }

    }

    @Override
    public void onSaturationChanged(float saturation) {

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_previewImage.copy(Bitmap.Config.ARGB_8888, true)));

            leftimage_DBfilter.get(0).setSaturation(saturation);
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_previewImage.copy(Bitmap.Config.ARGB_8888, true)));

            rightimage_DBfilter.get(0).setSaturation(saturation);
        }

    }

    @Override
    public void onContrastChanged(float contrast) {
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));

        if(Current_LeftImage){
            leftImage.setImageBitmap(myFilter.processFilter(Left_previewImage.copy(Bitmap.Config.ARGB_8888, true)));

            leftimage_DBfilter.get(0).setContrast(contrast);
        }else if(Current_RightImage){
            rightImage.setImageBitmap(myFilter.processFilter(Right_previewImage.copy(Bitmap.Config.ARGB_8888, true)));
            rightimage_DBfilter.get(0).setContrast(contrast);
        }

    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
    }

    private void loadImage() {
        leftimage_filter = new Filter();


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
            leftimage_filter = filter;
            Log.d("CUrrentINdex","CUrrentINdex : " + filtersListFragment.getSelectedFilter());
            leftimage_DBfilter.get(0).setSampleFilter(filtersListFragment.getSelectedFilter());

        }else if(Current_RightImage){
            Right_previewImage = filter.processFilter(filteredImage);
            rightImage.setImageBitmap(Right_previewImage);
            rightimage_DBfilter.get(0).setSampleFilter(filtersListFragment.getSelectedFilter());
            rightimage_filter = filter;
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
        new Thread(() -> {
            Bitmap bitmap  = filter.processFilter(Image);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            //파일로 저장
            saveImage = new FileModule.SaveImage(ImageEdit.this);
            saveImage.execute(currentData);
        }).start();
        Toast.makeText(getApplicationContext(),"CHECK THE FILE",Toast.LENGTH_LONG).show();
    }


   public class SwipeListener extends OnSwipeTouchListener{
        private String Direction;
        private TextView textView;
        private FiltersListFragment filtersFragment;
        private Context context;
        private View view;
        private ArrayList<FilterModel> FilterModel;
        private Filter filter;

       public SwipeListener(Context ctx, String Direction, TextView textView, FiltersListFragment filtersListFragment,View view,ArrayList<FilterModel> filterModel,Filter filter) {
           super(ctx);
           this.context = ctx;
           this.Direction = Direction;
           this.textView = textView;
           this.filtersFragment = filtersListFragment;
           this.view = view;
           this.FilterModel = filterModel;
           this.filter = filter;

       }
       public void onSwipeLeft() {
           CurrentChangePicture(Direction);
           filtersFragment.FilterSwipe(Direction,GalleryAppCode.GoLeft,textView);

       }

       //왼쪽방향 스와이프 - 필터변경
       public void onSwipeRight() {
           CurrentChangePicture(Direction);
           filtersFragment.FilterSwipe(Direction,GalleryAppCode.GoRight,textView);
       }
       //오른쪽방향 스와이프 - 필터변경
       public void onDoubleTouch(){
           CurrentChangePicture(Direction);
           resetControls();
           filtersFragment.FilterPositionChange(Direction);
           Log.d("Boolean check","LEFTIMAGE : " + Current_LeftImage);
           Log.d("Boolean check","RIGHTIMAGE : " + Current_RightImage);

       }
       public void onSwipeTop() {
           CurrentChangePicture(Direction);
           slideUp(view);
       }
       public void onSwipeBottom(){
           CurrentChangePicture(Direction);
           slideDown(view);
       }
       public void onLongPressed(){
           int bright = FilterModel.get(0).getBrightness();
           float contrast = FilterModel.get(0).getContrast();
           float saturation = FilterModel.get(0).getSaturation();

           if(Direction == GalleryAppCode.GoLeft){
               filter = leftimage_filter;
           }else if(Direction == GalleryAppCode.GoRight){
               filter = rightimage_filter;
           }

           filter.addSubFilter(new BrightnessSubFilter(bright));
           filter.addSubFilter(new ContrastSubFilter(contrast));
           filter.addSubFilter(new SaturationSubfilter(saturation));
           TotalImage(originalImage,filter);


           filterCustomDialog = new FilterCustomDialog(ImageEdit.this,FilterModel);
           filterCustomDialog.show();
           Log.d("AAFSS","AAF");
       }
   }
}