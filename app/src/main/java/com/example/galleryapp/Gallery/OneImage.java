package com.example.galleryapp.Gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.CompareFilter.ImageEdit;
import com.example.galleryapp.CompareFilter.OneFilter;
import com.example.galleryapp.CompareFilter.practiceseekbar;
import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.ViewPagerAdapter;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.galleryapp.Util.GalleryAppCode.GoToFilterPath;

public class OneImage extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout ll_TopGalleryLayout,ll_BottomGalleryLayout;
    private ToggleButton TB_SetFavorite;
    private TextView TV_GalleryHashtag;
    private ImageView IV_Filter,IV_KAKAOTALK,IV_Information;
    private int InitPosition, CurrentPosition;
    private String hashtag1, hashtag2,hashtag3,total_hashtag;
    private GalleryDBAccess galleryDBAccess;
    private GalleryModel galleryModel;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    ArrayList<GalleryModel> ImageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image);
        this.galleryDBAccess = GalleryDBAccess.getInstance(getApplicationContext());
        InitPosition = getIntent().getIntExtra(GalleryAppCode.Position, InitPosition);
        ImageList = (ArrayList<GalleryModel>) getIntent().getSerializableExtra(GalleryAppCode.GalleryList);
        galleryModel = ImageList.get(InitPosition);

        InitComponents();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                GalleryModel GModel = ImageList.get(position);
                Log.d("OneImage","Position : "  + position);
                Log.d("OneImage","Favorite : "  + GModel.getFavorite());
                hashtag1 = GModel.getHashtag1();
                hashtag2 = GModel.getHashtag2();
                hashtag3 = GModel.getHashtag3();
                Log.d("GWGWGW","PageChange" + GModel.getFavorite());
                total_hashtag = hashtag1+","+hashtag2+","+hashtag3;
                if(GModel.getFavorite() == 1){
                    TB_SetFavorite.setChecked(true);

                }else{
                    TB_SetFavorite.setChecked(false);
                }
                TV_GalleryHashtag.setText(total_hashtag);
                TB_SetFavorite.setOnCheckedChangeListener(checkedChangeListener);
                Log.d("GWGWGW","Position" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitComponents(){
        viewPager = findViewById(R.id.view);
        ll_TopGalleryLayout = findViewById(R.id.OneImage_Topnavigation);
        ll_BottomGalleryLayout = findViewById(R.id.OneImage_BottomNavigation);

        ll_TopGalleryLayout.setVisibility(View.INVISIBLE);
        ll_BottomGalleryLayout.setVisibility(View.INVISIBLE);
        IV_Filter = findViewById(R.id.CompareButton);
        IV_Filter.setOnClickListener(this);

        IV_Information = findViewById(R.id.PictureInfo);
        IV_Information.setOnClickListener(this);

        IV_KAKAOTALK = findViewById(R.id.KAKAOTALK);
        IV_KAKAOTALK.setOnClickListener(this);

        TV_GalleryHashtag = findViewById(R.id.TotalHashtag);
        TB_SetFavorite = findViewById(R.id.SetFavorite);
        if(galleryModel.getFavorite() == 1){
            TB_SetFavorite.setChecked(true);
        }else{
            TB_SetFavorite.setChecked(false);
        }
        TB_SetFavorite.setOnCheckedChangeListener(checkedChangeListener);
        viewPagerAdapter = new ViewPagerAdapter(this, ImageList);
        viewPager.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                   // 터치 이벤트
                    CountDownTimer CDT = new CountDownTimer(2 * 1000, 5000) {
                        public void onTick(long millisUntilFinished) {
                            ll_TopGalleryLayout.setVisibility(View.VISIBLE);
                            ll_TopGalleryLayout.bringToFront();

                            ll_BottomGalleryLayout.setVisibility(View.VISIBLE);
                            ll_BottomGalleryLayout.bringToFront();
                        }
                        public void onFinish() {
                            ll_TopGalleryLayout.setVisibility(View.INVISIBLE);
                            ll_BottomGalleryLayout.setVisibility(View.INVISIBLE);
                        }
                    };
                    CDT.start(); //CountDownTimer 실행

                    break;
            }
            return false;
        });

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(InitPosition);

        GalleryModel galleryModel = ImageList.get(InitPosition);
        hashtag1 = galleryModel.getHashtag1();
        hashtag2 = galleryModel.getHashtag2();
        hashtag3 = galleryModel.getHashtag3();

        total_hashtag = hashtag1+","+hashtag2+","+hashtag3;
        TV_GalleryHashtag.setText(total_hashtag);

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.CompareButton:
                Log.d("GWGWGW","Success");
                CurrentPosition = viewPager.getCurrentItem();
                String CurrentFile = ImageList.get(CurrentPosition).getFilename();
                Intent intent = new Intent(this, ImageEdit.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable(GalleryAppCode.GalleryList,ImageList);
                intent.putExtras(bundle);

                intent.putExtra(GoToFilterPath,CurrentFile);
                intent.putExtra(GalleryAppCode.Position,CurrentPosition);
                startActivity(intent);
                break;
            case R.id.KAKAOTALK:
//                shareKAKAO();
                SelectHowtoEdit();
                break;
            case R.id.PictureInfo:
                GalleryModel galleryModel = ImageList.get(viewPager.getCurrentItem());
                ImageInformation(galleryModel);
        }
    }

    private ToggleButton.OnCheckedChangeListener checkedChangeListener = (compoundButton, isfavorite) -> {
        if(isfavorite){
            GalleryModel GalleryModel = ImageList.get(viewPager.getCurrentItem());
            TB_SetFavorite.setChecked(true);
            GalleryModel.setFavorite(1);
            galleryDBAccess.open();
            galleryDBAccess.FavoriteChange(GalleryModel,1);
            galleryDBAccess.close();
            Log.d("GWGWGW","Favorite" + GalleryModel.getFavorite());

        }else{
            GalleryModel GalleryModel = ImageList.get(viewPager.getCurrentItem());
            TB_SetFavorite.setChecked(false);
            GalleryModel.setFavorite(0);
            galleryDBAccess.open();
            galleryDBAccess.FavoriteChange(GalleryModel,0);
            galleryDBAccess.close();
            Log.d("GWGWGW","Favorite" + GalleryModel.getFavorite());
        }

    };
    public void shareKAKAO(){
        try{
            Log.d("KAKAO","KAKAO");
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaobuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaobuilder.addText("카카오링크 테스트");
            String url = "http://upload2.inven.co.kr/upload/2015/09/27/bbs/i12820605286.jpg";
            kakaobuilder.addImage(url,1080,1920);

            kakaoLink.sendMessage(kakaobuilder, this);

        }
        catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    private void SelectHowtoEdit(){
//         선택 Dialog 형성
    String[] listItems = {"이미지 1개로 단순 필터 적용", "이미지 2개로 비교하며 필터 적용"};

    LayoutInflater inflater = (LayoutInflater)(OneImage.this).getSystemService(LAYOUT_INFLATER_SERVICE);
    View Layout = inflater.inflate(R.layout.customdialogpractice,(ViewGroup) findViewById(R.id.layout_root));
    TextView firstText = Layout.findViewById(R.id.firsttext);
    TextView secondText = Layout.findViewById(R.id.secondtext);
    ImageView firstImage = Layout.findViewById(R.id.firsttextimage);
    ImageView secondImage = Layout.findViewById(R.id.secondtextimage);

    firstText.setText(listItems[0]);
    secondText.setText(listItems[1]);

    firstImage.setImageResource(R.mipmap.oneimagefilter);
    secondImage.setImageResource(R.mipmap.twoimagefilter);

    AlertDialog.Builder builder = new AlertDialog.Builder(OneImage.this);

          builder.setItems(listItems, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  //해당 아이템 선택시 발생
                  if(i == 0){
                      Log.d("GWGWGW","Success");
                      GotoImageEdit(viewPager,ImageList,"ONE");
                  }else if(i ==1){
                      Log.d("GWGWGW","Success");
                      GotoImageEdit(viewPager,ImageList,"TWO");
                  }
              }
          });

    AlertDialog dialog = builder.create();
           dialog.show();
    }


    private void GotoImageEdit(ViewPager viewPager,ArrayList<GalleryModel> galleryModels,String ImageEdit){
        Intent intent = new Intent();
        CurrentPosition = viewPager.getCurrentItem();
        String CurrentFile = galleryModels.get(CurrentPosition).getFilename();
        if(ImageEdit == "ONE"){
            intent = new Intent(OneImage.this, OneImage.class);
        }else if(ImageEdit == "TWO"){
            intent = new Intent(OneImage.this, ImageEdit.class);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(GalleryAppCode.GalleryList,ImageList);
        intent.putExtras(bundle);

        intent.putExtra(GoToFilterPath,CurrentFile);
        intent.putExtra(GalleryAppCode.Position,CurrentPosition);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    private void ImageInformation(GalleryModel galleryModel){

        String FileName = galleryModel.getFilename();
        Double Longitude = galleryModel.getLongitude();
        Double Latitude = galleryModel.getLatitude();
        String Hashtag1 = galleryModel.getHashtag1();
        String Hashtag2 = galleryModel.getHashtag2();
        String Hashtag3 = galleryModel.getHashtag3();
        String Filtername = galleryModel.getFiltername();
        int Favorite = galleryModel.getFavorite();

        FileName = FileName.replaceAll(".jpg","");
        long filename = Long.parseLong(FileName);
        Date date=new Date(filename);
        SimpleDateFormat df2 = new SimpleDateFormat("MM월 dd일");
        FileName = df2.format(date);

        LayoutInflater inflater = (LayoutInflater)(OneImage.this).getSystemService(LAYOUT_INFLATER_SERVICE);
        View Layout = inflater.inflate(R.layout.informationdialog,(ViewGroup) findViewById(R.id.informationdialoglayout));

        AlertDialog.Builder builder = new AlertDialog.Builder(OneImage.this);

        TextView name = Layout.findViewById(R.id.ImageDate);
        TextView location = Layout.findViewById(R.id.ImageLocation);
        TextView hashtag1 = Layout.findViewById(R.id.ImageHashtag1);
        TextView hashtag2 = Layout.findViewById(R.id.ImageHashtag2);
        TextView hashtag3 = Layout.findViewById(R.id.ImageHashtag3);
        TextView Like = Layout.findViewById(R.id.ImageFavorite);
        TextView filtername = Layout.findViewById(R.id.ImageFilter);

        Geocoder mGeoCoder = new Geocoder(OneImage.this);
        //섬네일 리스트 상단에 선택 클러스터 주소 표시
        try {
            List<Address> mResultList = mGeoCoder.getFromLocation(Longitude,Latitude,1);
            String ImageLocation  = String.valueOf(mResultList.get(0).getAddressLine(0));
            String[] data = ImageLocation.split(" ");
            ImageLocation = data[1] + " "  +data[2];
            location.setText(ImageLocation);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        name.setText(FileName);
        hashtag1.setText(Hashtag1);
        hashtag2.setText(Hashtag2);
        hashtag3.setText(Hashtag3);
        filtername.setText(Filtername);
        if(Favorite == 0){
            Like.setText("아직 설정 안 했어요");
        }else{
            Like.setText("좋아요");
        }

        builder.setView(Layout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}


