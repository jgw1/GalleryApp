package com.example.galleryapp.CompareFilter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.HashtagCustomDialog;
import com.example.galleryapp.Util.FileModule;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;
//import com.kakao.kakaolink.KakaoLink;
//import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
//import com.kakao.util.KakaoParameterException;
import com.google.android.material.tabs.TabLayout;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.galleryapp.Util.Animation.slideDown;
import static com.example.galleryapp.Util.Animation.slideUp;

public class OneFilter extends AppCompatActivity implements  FiltersListFragment.FiltersListFragmentListener,EditImageFragment.EditImageFragmentListener{

    private Bitmap originalImage,scaleDownImage, filteredImage, PreviewImage;
    private ImageView IV_LeftImage;
    private ViewPager viewPager;
    private String ImagePath,Hashtag1,Hashtag2,Hashtag3;
    private File ImageFile;
    private Filter ImageFilter;
    private HashtagCustomDialog hashtagCustomDialog;
    private GalleryDBAccess galleryDBAccess;
    private ImageButton IB_SaveFilterImage;
    private TextView TV_FilterName;
    private TabLayout tabLayout;
    private FileModule.SaveImage saveImage;
    private RelativeLayout onefilterlayout;
    ArrayList<GalleryModel> ImageList;
    private int InitPosition;
    private FiltersListFragment filtersListFragment;
    private ArrayList<FilterModel> oneimage_DBfilter;
    private EditImageFragment editImageFragment;
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
        Toolbar toolbar = findViewById(R.id.onefiltertoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filters");

        viewPager = findViewById(R.id.onefilterviewpager);
        setupViewPager(viewPager);
        initComponents();
        loadImage();


        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        CompareFilterAdapter adapter = new CompareFilterAdapter(getSupportFragmentManager());

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

    private void resetControls() {
        if (editImageFragment != null) {
            editImageFragment.resetControls();
        }
    }

    @Override
    public void onBrightnessChanged(int brightness) {

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        ImageFilter.addSubFilter(new BrightnessSubFilter(brightness));
        oneimage_DBfilter.get(0).setBrightness(brightness);
        IV_LeftImage.setImageBitmap(myFilter.processFilter(PreviewImage.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        oneimage_DBfilter.get(0).setSaturation(saturation);
        IV_LeftImage.setImageBitmap(myFilter.processFilter(PreviewImage.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onContrastChanged(float contrast) {
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        oneimage_DBfilter.get(0).setContrast(contrast);
        IV_LeftImage.setImageBitmap(myFilter.processFilter(PreviewImage.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {

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

//        IV_LeftImage.setImageURI(Uri.fromFile(ImageFIle));
        InitPosition = getIntent().getIntExtra(GalleryAppCode.Position, InitPosition);
        ImageList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);
        GalleryModel galleryModel = ImageList.get(InitPosition);
        tabLayout = findViewById(R.id.onefiltertabs);
        onefilterlayout = findViewById(R.id.onefilterlayout);
        ImageFilter = new Filter();
        oneimage_DBfilter = FilterModel.initfilterModel();
        onefilterlayout.bringToFront();
        TV_FilterName = findViewById(R.id.selectfilterName);
        Hashtag1 = galleryModel.getHashtag1();
        Hashtag2 = galleryModel.getHashtag2();
        Hashtag3 = galleryModel.getHashtag3();


        hashtagCustomDialog = new HashtagCustomDialog(this);




        IV_LeftImage.setOnTouchListener(new OnSwipeTouchListener(this) {

            //왼쪽방향 스와이프 - 필터변경
            public void onSwipeLeft() {
//                filtersListFragment.FilterChange(GalleryAppCode.GoLeft,TV_FilterName);
                filtersListFragment.FilterSwipe(GalleryAppCode.OneImage,GalleryAppCode.GoLeft,TV_FilterName);

            }

            //오른쪽방향 스와이프 - 필터변경
            public void onSwipeRight() {
//                filtersListFragment.FilterChange(GalleryAppCode.GoRight,TV_FilterName);
                filtersListFragment.FilterSwipe(GalleryAppCode.OneImage,GalleryAppCode.GoRight,TV_FilterName);
            }
            public void onSwipeTop() {
                slideUp(onefilterlayout);

            }
            public void onSwipeBottom(){
                slideDown(onefilterlayout);

            }
        });

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
            case R.id.action_kakaotalk:

                return true;
            case R.id.action_save:
                TotalImage(originalImage,ImageFilter);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        // reset image controls
        resetControls();
//        scaleDownImage = BitmapUtils.getBitmapFromAssets(this, File_Name, 300, 300);
        // applying the selected filter
        filteredImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);
        // preview filtered image
        PreviewImage = filter.processFilter(filteredImage);
        IV_LeftImage.setImageBitmap(PreviewImage);
        ImageFilter = filter;
        oneimage_DBfilter.get(0).setSampleFilter(filtersListFragment.getSelectedFilter());


    }

    private void loadImage(){
        originalImage = BitmapFactory.decodeFile(ImageFile.getPath());
        originalImage = originalImage.copy(Bitmap.Config.ARGB_8888,true);

        scaleDownImage = BitmapUtils.resize(getApplicationContext(), Uri.fromFile(ImageFile),300);
        scaleDownImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);

        PreviewImage = scaleDownImage.copy(Bitmap.Config.ARGB_8888,true);
        IV_LeftImage.setImageBitmap(PreviewImage);
    }


    private void TotalImage(Bitmap Image, Filter filter){
        new Thread(() -> {
            Bitmap bitmap  = filter.processFilter(Image);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] currentData = stream.toByteArray();

            //파일로 저장
            saveImage = new FileModule.SaveImage(OneFilter.this);
            saveImage.execute(currentData);
        }).start();
        Toast.makeText(getApplicationContext(),"CHECK THE FILE",Toast.LENGTH_LONG).show();
    }
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
