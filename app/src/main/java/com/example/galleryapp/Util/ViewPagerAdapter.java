package com.example.galleryapp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.viewpager.widget.PagerAdapter;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.R;

import java.io.File;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<GalleryModel> itemModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private ImageButton IB_GalleryBack;
    private ImageView imageView;
    private LinearLayout ll_GalleryLayout;
    private ToggleButton TB_SetFavorite;
    private TextView TV_GalleryHashtag1,TV_GalleryHashtag2,TV_GalleryHashtag3;

    // 해당 context가 자신의 context 객체와 똑같이 되도록 생성자를 만듬
    public ViewPagerAdapter(Context context,ArrayList<GalleryModel> itemModelArrayList){
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
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
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.temporary, container, false);
        imageView = v.findViewById(R.id.Gallery_Image);
        final GalleryModel galleryModel = itemModelArrayList.get(position);

        String FileName = galleryModel.getFilename();
        File outputFile = new File(GalleryAppCode.Path,FileName);
        Bitmap Image = BitmapUtils.resize(context, Uri.fromFile(outputFile),800);


        imageView.setImageBitmap(Image);

        container.addView(v);
        return v;
    }

    //할당을 해제
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
//        super.destroyItem(container, position, object);
    }


}
