package com.example.galleryapp.CompareFilter;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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

public class OneFilter extends AppCompatActivity implements  FiltersListFragment.FiltersListFragmentListener,View.OnClickListener{

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
    static{
        System.loadLibrary("NativeImageProcessor");
    }
    public static String File_Name;

    public OneFilter() {
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
        filtersListFragment.setListener(this);

        // adding edit image fragment

        adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
            switch(view.getId()){
                case R.id.SaveImage:
                    customDialog.show();
                    EditText hashtag1 = customDialog.findViewById(R.id.hashtag1);
                    hashtag1.setText(Hashtag1);

                    EditText hashtag2 = customDialog.findViewById(R.id.hashtag2);
                    hashtag2.setText(Hashtag2);

                    EditText hashtag3 = customDialog.findViewById(R.id.hashtag3);
                    hashtag3.setText(Hashtag3);

                    break;

        }
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
        this.galleryDBAccess = GalleryDBAccess.getInstance(this);
        IV_LeftImage = findViewById(R.id.SelectImage);
        File ImageFIle = new File(GalleryAppCode.Path+ImagePath);
//        IV_LeftImage.setImageURI(Uri.fromFile(ImageFIle));

        InitPosition = getIntent().getIntExtra(GalleryAppCode.Position, InitPosition);
        ImageList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);
        GalleryModel galleryModel = ImageList.get(InitPosition);
        IB_SaveFilterImage = findViewById(R.id.SaveImage);
        IB_SaveFilterImage.setOnClickListener(this::onClick);
        IB_SaveFilterImage.bringToFront();


        TV_FilterName = findViewById(R.id.selectfilterName);
        Hashtag1 = galleryModel.getHashtag1();
        Hashtag2 = galleryModel.getHashtag2();
        Hashtag3 = galleryModel.getHashtag3();


        viewPager = findViewById(R.id.filterViewPager);
        customDialog = new CustomDialog(this,positiveListener,negativeListener);
        viewPager.setVisibility(View.INVISIBLE);


        IV_LeftImage.setOnTouchListener(new OnSwipeTouchListener(this) {

            //오른쪽방향 스와이프 - 필터변경
            public void onSwipeLeft() {
                filtersListFragment.FilterChange(GalleryAppCode.GoLeft,TV_FilterName);
            }

            //왼쪽방향 스와이프 - 필터변경
            public void onSwipeRight() {
                filtersListFragment.FilterChange(GalleryAppCode.GoRight,TV_FilterName);
            }
            public void onSwipeTop() {
                slideUp(viewPager);
            }
            public void onSwipeBottom(){
                slideDown(viewPager);
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

    private View.OnClickListener positiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText hashtag1 = customDialog.findViewById(R.id.hashtag1);
            EditText hashtag2 = customDialog.findViewById(R.id.hashtag2);
            EditText hashtag3 = customDialog.findViewById(R.id.hashtag3);

            String Hashtag1 = "#" + hashtag1.getText().toString();
            String Hashtag2 = "#" + hashtag2.getText().toString();
            String Hashtag3 = "#" + hashtag3.getText().toString();

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest";
            File file = new File(String.valueOf(Thumbnail.latestFileModified(path)));
            String file_name = file.getName();
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getApplicationContext());
            galleryDBAccess.open();
            galleryDBAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
            galleryDBAccess.close();
            customDialog.dismiss();
        }
    };
    private View.OnClickListener negativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest";
            File file = new File(String.valueOf(Thumbnail.latestFileModified(path)));
            String file_name = file.getName();
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getApplicationContext());
            galleryDBAccess.open();
            galleryDBAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),"","","");
            galleryDBAccess.close();
            customDialog.dismiss();
        }
    };
    // slide the view from below itself to the current position
    public void slideUp(ViewPager view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(ViewPager view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                view.getHeight());                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

}