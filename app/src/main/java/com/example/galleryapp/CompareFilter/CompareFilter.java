package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompareFilter extends AppCompatActivity implements  FiltersListFragment.FiltersListFragmentListener{
    private RecyclerView RV_FilterList;
    private Bitmap originalImage, filteredImage,finalImage;
    private ImageView IV_LeftImage;
    private ViewPager viewPager;
    private String ImagePath;
    private File ImageFile;
    private ArrayList<Filter> filterArrayList;
    private FiltersListFragment filtersListFragment;
    static{
        System.loadLibrary("NativeImageProcessor");
    }
    public static String File_Name;

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
        IV_LeftImage = findViewById(R.id.leftImage);
        File ImageFIle = new File(GalleryAppCode.Path+ImagePath);
//        IV_LeftImage.setImageURI(Uri.fromFile(ImageFIle));

        viewPager = findViewById(R.id.filterViewPager);

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

        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
    }

    private void loadImage(){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageFile.getPath());
//        originalImage = BitmapUtils.getBitmapFromGallery(this,Uri.fromFile(ImageFile),300,300);
        filteredImage = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalImage = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        IV_LeftImage.setImageBitmap(bitmap);
    }


}
