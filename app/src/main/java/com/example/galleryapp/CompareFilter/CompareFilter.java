package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.Gallery.GalleryFragment;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.CustomDialog;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.example.galleryapp.Util.Thumbnail;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompareFilter extends AppCompatActivity implements  FiltersListFragment.FiltersListFragmentListener,View.OnClickListener{

    private Bitmap originalImage, filteredImage,finalImage;
    private ImageView IV_LeftImage;
    private ViewPager viewPager;
    private String ImagePath,Hashtag1,Hashtag2,Hashtag3;
    private File ImageFile;
    private Filter Selectedfilter;
    private CustomDialog customDialog;
    private DatabaseAccess databaseAccess;
    private ImageButton IB_SaveFilterImage;
    ArrayList<GalleryModel> ImageList;
    private int InitPosition;
    private FiltersListFragment filtersListFragment;
    static{
        System.loadLibrary("NativeImageProcessor");
    }
    public static String File_Name;

    public CompareFilter() {
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
                case R.id.SaveFilterImage:
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
        this.databaseAccess = DatabaseAccess.getInstance(this);
        IV_LeftImage = findViewById(R.id.leftImage);
        File ImageFIle = new File(GalleryAppCode.Path+ImagePath);
//        IV_LeftImage.setImageURI(Uri.fromFile(ImageFIle));

        InitPosition = getIntent().getIntExtra(GalleryAppCode.Position, InitPosition);
        ImageList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);
        GalleryModel galleryModel = ImageList.get(InitPosition);
        IB_SaveFilterImage = findViewById(R.id.SaveFilterImage);
        IB_SaveFilterImage.setOnClickListener(this::onClick);
        IB_SaveFilterImage.bringToFront();


        Hashtag1 = galleryModel.getHashtag1();
        Hashtag2 = galleryModel.getHashtag2();
        Hashtag3 = galleryModel.getHashtag3();


        viewPager = findViewById(R.id.filterViewPager);
        customDialog = new CustomDialog(this,positiveListener,negativeListener);



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
            databaseAccess.open();
            databaseAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
            databaseAccess.close();
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
            databaseAccess.open();
            databaseAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),"","","");
            databaseAccess.close();
            customDialog.dismiss();
        }
    };

}
