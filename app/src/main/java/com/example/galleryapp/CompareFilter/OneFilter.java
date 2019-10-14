package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.HashtagCustomDialog;
import com.example.galleryapp.Util.FileModule;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
//import com.kakao.kakaolink.KakaoLink;
//import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
//import com.kakao.util.KakaoParameterException;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.galleryapp.Util.Animation.slideDown;
import static com.example.galleryapp.Util.Animation.slideUp;
import static com.example.galleryapp.Util.Animation.topslidedown;
import static com.example.galleryapp.Util.Animation.topslideup;

public class OneFilter extends AppCompatActivity implements  FiltersListFragment.FiltersListFragmentListener,View.OnClickListener{

    private Bitmap originalImage, filteredImage,finalImage;
    private ImageView IV_LeftImage;
    private ViewPager viewPager;
    private String ImagePath,Hashtag1,Hashtag2,Hashtag3;
    private File ImageFile;
    private Filter Selectedfilter;
    private HashtagCustomDialog hashtagCustomDialog;
    private GalleryDBAccess galleryDBAccess;
    private ImageButton IB_SaveFilterImage;
    private TextView TV_FilterName;
    private LinearLayout top_navigation;
    private FileModule.SaveImage saveImage;

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

        setContentView(R.layout.activity_one_filter);
        this.galleryDBAccess = GalleryDBAccess.getInstance(getApplicationContext());
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
                    hashtagCustomDialog.show();
                    EditText hashtag1 = hashtagCustomDialog.findViewById(R.id.hashtag1);
                    hashtag1.setText(Hashtag1);

                    EditText hashtag2 = hashtagCustomDialog.findViewById(R.id.hashtag2);
                    hashtag2.setText(Hashtag2);

                    EditText hashtag3 = hashtagCustomDialog.findViewById(R.id.hashtag3);
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
        top_navigation=findViewById(R.id.Filter_Topnavigation);
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
        hashtagCustomDialog = new HashtagCustomDialog(this);
        viewPager.setVisibility(View.INVISIBLE);
        top_navigation.setVisibility(View.INVISIBLE);


        IV_LeftImage.setOnTouchListener(new OnSwipeTouchListener(this) {

            //오른쪽방향 스와이프 - 필터변경
            public void onSwipeLeft() {
//                filtersListFragment.FilterChange(GalleryAppCode.GoLeft,TV_FilterName);
            }

            //왼쪽방향 스와이프 - 필터변경
            public void onSwipeRight() {
//                filtersListFragment.FilterChange(GalleryAppCode.GoRight,TV_FilterName);
            }
            public void onSwipeTop() {
                slideUp(viewPager);
                topslideup(top_navigation);
            }
            public void onSwipeBottom(){
                slideDown(viewPager);
                topslidedown(top_navigation);
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

//        originalImage = BitmapUtils.getBitmapFromAssets(this, File_Name, 300, 300);
        // applying the selected filter
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        finalImage = filter.processFilter(filteredImage);
        IV_LeftImage.setImageBitmap(filter.processFilter(filteredImage));


    }

    private void loadImage(){
        originalImage = BitmapUtils.resize(getApplicationContext(), Uri.fromFile(ImageFile),300);
//        originalImage = BitmapUtils.getBitmapFromGallery(this,Uri.fromFile(ImageFile),300,300);
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888,true);
        finalImage = originalImage.copy(Bitmap.Config.ARGB_8888,true);
        IV_LeftImage.setImageBitmap(filteredImage);
    }

    private View.OnClickListener positiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //bitmap을 byte array로 변환
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            finalImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            //파일로 저장

            saveImage = new FileModule.SaveImage(OneFilter.this);
            saveImage.execute(currentData);

            EditText hashtag1 = hashtagCustomDialog.findViewById(R.id.hashtag1);
            EditText hashtag2 = hashtagCustomDialog.findViewById(R.id.hashtag2);
            EditText hashtag3 = hashtagCustomDialog.findViewById(R.id.hashtag3);

            String Hashtag1 = "#" + hashtag1.getText().toString();
            String Hashtag2 = "#" + hashtag2.getText().toString();
            String Hashtag3 = "#" + hashtag3.getText().toString();

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest";
            File file = new File(String.valueOf(FileModule.latestFileModified(path)));
            String file_name = file.getName();
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getApplicationContext());
            galleryDBAccess.open();
            galleryDBAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),Hashtag1,Hashtag2,Hashtag3);
            galleryDBAccess.close();



            hashtagCustomDialog.dismiss();
        }
    };
    private View.OnClickListener negativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            finalImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            //파일로 저장
            saveImage = new FileModule.SaveImage(OneFilter.this);
            saveImage.execute(currentData);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest";
            File file = new File(String.valueOf(FileModule.latestFileModified(path)));
            String file_name = file.getName();
            ArrayList<Double> LatLng = Location.GetCurrentLocation(getApplicationContext());
            galleryDBAccess.open();
            galleryDBAccess.InsertData(file_name,LatLng.get(0),LatLng.get(1),"","","");
            galleryDBAccess.close();



            hashtagCustomDialog.dismiss();
        }
    };
    // slide the view from below itself to the current position


//    public void shareKAKAO(){
//        try{
//            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
//            final KakaoTalkLinkMessageBuilder kakaobuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//
//            kakaobuilder.addText("카카오링크 테스트");
//            String url = "http://upload2.inven.co.kr/upload/2015/09/27/bbs/i12820605286.jpg";
//            kakaobuilder.addImage(url,1080,1920);
//
//            kakaoLink.sendMessage(kakaobuilder, this);
//
//        }
//        catch (KakaoParameterException e) {
//            e.printStackTrace();
//        }
//    }

}
