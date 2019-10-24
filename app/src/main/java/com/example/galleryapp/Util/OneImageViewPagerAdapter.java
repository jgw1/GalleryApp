package com.example.galleryapp.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.CompareFilter.ImageEdit;
import com.example.galleryapp.CompareFilter.OneFilter;
import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.R;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.galleryapp.Util.GalleryAppCode.GoToFilterPath;

public class OneImageViewPagerAdapter extends PagerAdapter implements View.OnClickListener{
    private ArrayList<GalleryModel> itemModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private ImageButton IB_GalleryBack;
    private ImageView imageView,IV_Filter,IV_KAKAOTALK,IV_Information;
    private LinearLayout ll_TopGalleryLayout,ll_BottomGalleryLayout;
    private ToggleButton TB_SetFavorite;
    private ViewPager viewPager;
    private ViewGroup viewGroup;
    private GalleryDBAccess galleryDBAccess;
    private TextView TV_GalleryHashtag;
    private String hashtag1,hashtag2,hashtag3,total_hashtag;

    // 해당 context가 자신의 context 객체와 똑같이 되도록 생성자를 만듬
    public OneImageViewPagerAdapter(Context context, ViewPager ViewPager, ArrayList<GalleryModel> itemModelArrayList){
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        this.viewPager= ViewPager;

    }
    @Override
    public int getCount() {
        return itemModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // object를 LinearLayout 형태로 형변환했을 때 view와 같은지 여부를 반환

        return view == ((View)object);
    }

    // 각각의 item을 인스턴스 화

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //초기화
        viewGroup = container;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.one_image_viewpager, container, false);
        imageView = v.findViewById(R.id.Gallery_Image);
        final GalleryModel galleryModel = itemModelArrayList.get(position);

        String FileName = galleryModel.getFilename();
        File outputFile = new File(GalleryAppCode.Path,FileName);
        Bitmap Image = BitmapUtils.resize(context, Uri.fromFile(outputFile),800);
        imageView.setImageBitmap(Image);

        ll_TopGalleryLayout = v.findViewById(R.id.OneImage_Topnavigation);
        ll_BottomGalleryLayout = v.findViewById(R.id.OneImage_BottomNavigation);

        ll_TopGalleryLayout.setVisibility(View.INVISIBLE);
        ll_BottomGalleryLayout.setVisibility(View.INVISIBLE);

        IV_Filter = v.findViewById(R.id.CompareButton);
        IV_Filter.setOnClickListener(this);

        IV_Information = v.findViewById(R.id.PictureInfo);
        IV_Information.setOnClickListener(this);

        IV_KAKAOTALK = v.findViewById(R.id.KAKAOTALK);
        IV_KAKAOTALK.setOnClickListener(this);

        TV_GalleryHashtag = v.findViewById(R.id.TotalHashtag);
        TB_SetFavorite =v.findViewById(R.id.SetFavorite);
        if(galleryModel.getFavorite() == 1){
            TB_SetFavorite.setChecked(true);
        }else{
            TB_SetFavorite.setChecked(false);
        }
        TB_SetFavorite.setOnCheckedChangeListener(checkedChangeListener);


        hashtag1 = galleryModel.getHashtag1();
        hashtag2 = galleryModel.getHashtag2();
        hashtag3 = galleryModel.getHashtag3();

        total_hashtag = "#"+hashtag1+"#"+hashtag2+"#"+hashtag3;
        TV_GalleryHashtag.setText(total_hashtag);

//        viewPager.setOnTouchListener((view, motionEvent) -> {
//            switch(motionEvent.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                   // 터치 이벤트
//                    Log.d("TOUCHEVENT","TOUCHEVENT");
//                    CountDownTimer CDT = new CountDownTimer(2 * 1000, 5000) {
//                        public void onTick(long millisUntilFinished) {
//                            ll_TopGalleryLayout.setVisibility(View.VISIBLE);
//                            ll_TopGalleryLayout.bringToFront();
//
//                            ll_BottomGalleryLayout.setVisibility(View.VISIBLE);
//                            ll_BottomGalleryLayout.bringToFront();
//                        }
//                        public void onFinish() {
//                            ll_TopGalleryLayout.setVisibility(View.INVISIBLE);
//                            ll_BottomGalleryLayout.setVisibility(View.INVISIBLE);
//                        }
//                    };
//                    CDT.start(); //CountDownTimer 실행
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    view.performClick();
//                    break;
//            }
//            return false;
//        });

        container.addView(v);
        return v;
    }

    //할당을 해제
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
//        super.destroyItem(container, position, object);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.CompareButton:
                SelectHowtoEdit();
                break;
            case R.id.KAKAOTALK:
//                shareKAKAO();
                break;
            case R.id.PictureInfo:
                GalleryModel galleryModel = itemModelArrayList.get(viewPager.getCurrentItem());
                ImageInformation(galleryModel);
        }
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

        LayoutInflater inflater = (LayoutInflater)(context).getSystemService(LAYOUT_INFLATER_SERVICE);
        View Layout = inflater.inflate(R.layout.informationdialog, viewGroup.findViewById(R.id.informationdialoglayout));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView name = Layout.findViewById(R.id.ImageDate);
        TextView location = Layout.findViewById(R.id.ImageLocation);
        TextView hashtag1 = Layout.findViewById(R.id.ImageHashtag1);
        TextView hashtag2 = Layout.findViewById(R.id.ImageHashtag2);
        TextView hashtag3 = Layout.findViewById(R.id.ImageHashtag3);
        TextView Like = Layout.findViewById(R.id.ImageFavorite);
        TextView filtername = Layout.findViewById(R.id.ImageFilter);

        Geocoder mGeoCoder = new Geocoder(context);
        //섬네일 리스트 상단에 선택 클러스터 주소 표시
        try {
            List<Address> mResultList = mGeoCoder.getFromLocation(Latitude,Longitude,1);
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
    private void SelectHowtoEdit(){
//         선택 Dialog 형성
        String[] listItems = {GalleryAppCode.EditOneImage, GalleryAppCode.EditTwoImage};

        LayoutInflater inflater = (LayoutInflater)(context).getSystemService(LAYOUT_INFLATER_SERVICE);
        View Layout = inflater.inflate(R.layout.customdialogpractice, viewGroup.findViewById(R.id.layout_root));

        TextView firstText = Layout.findViewById(R.id.firsttext);
        TextView secondText = Layout.findViewById(R.id.secondtext);

        firstText.setText(listItems[0]);
        secondText.setText(listItems[1]);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(listItems, (dialogInterface, i) -> {
            //해당 아이템 선택시 발생
            if(i == 0){
                GotoImageEdit(viewPager,itemModelArrayList,GalleryAppCode.OneImage);
            }else if(i ==1){
                GotoImageEdit(viewPager,itemModelArrayList,GalleryAppCode.TwoImage);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void GotoImageEdit(ViewPager viewPager,ArrayList<GalleryModel> galleryModels,String ImageEdit){
        Intent intent = new Intent();
        int CurrentPosition = viewPager.getCurrentItem();
        String CurrentFile = galleryModels.get(CurrentPosition).getFilename();
        if(ImageEdit == "ONE"){
            intent = new Intent(context, OneFilter.class);
        }else if(ImageEdit == "TWO"){
            intent = new Intent(context, ImageEdit.class);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(GalleryAppCode.GalleryList,itemModelArrayList);
        intent.putExtras(bundle);

        intent.putExtra(GoToFilterPath,CurrentFile);
        intent.putExtra(GalleryAppCode.Position,CurrentPosition);
        context.startActivity(intent);
    }
    private ToggleButton.OnCheckedChangeListener checkedChangeListener = (compoundButton, isfavorite) -> {
        this.galleryDBAccess = GalleryDBAccess.getInstance(context);
        if(isfavorite){
            GalleryModel GalleryModel = itemModelArrayList.get(viewPager.getCurrentItem());
            TB_SetFavorite.setChecked(true);
            GalleryModel.setFavorite(1);
            galleryDBAccess.open();
            galleryDBAccess.FavoriteChange(GalleryModel,1);
            galleryDBAccess.close();
            Log.d("GWGWGW","Favorite" + GalleryModel.getFavorite());

        }else{
            GalleryModel GalleryModel = itemModelArrayList.get(viewPager.getCurrentItem());
            TB_SetFavorite.setChecked(false);
            GalleryModel.setFavorite(0);
            galleryDBAccess.open();
            galleryDBAccess.FavoriteChange(GalleryModel,0);
            galleryDBAccess.close();
            Log.d("GWGWGW","Favorite" + GalleryModel.getFavorite());
        }
    };
    public void shareKAKAO() {
        try {
            Log.d("KAKAO", "KAKAO");
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
            final KakaoTalkLinkMessageBuilder kakaobuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaobuilder.addText("카카오링크 테스트");
            String url = "http://upload2.inven.co.kr/upload/2015/09/27/bbs/i12820605286.jpg";
            kakaobuilder.addImage(url, 1080, 1920);

            kakaoLink.sendMessage(kakaobuilder, context);

        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
}
