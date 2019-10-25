package com.example.galleryapp.GalleryDay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GalleryDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public boolean isSelectable =false;

    private ArrayList<ItemInterface> mUsersAndSectionList;
    private ArrayList<Integer> headerPositionList;
    private ArrayList<Integer> GalleryList;
    private Context context;

    public GalleryDayAdapter(ArrayList<ItemInterface> mUsersAndSectionList, Context context,ArrayList<Integer> arrayList) {
        this.mUsersAndSectionList = mUsersAndSectionList;
        this.context = context;
        this.headerPositionList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.albumday_title, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.albumday_content, parent, false);
            return new ItemViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + ". Make sure you are using view types correctly!");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            AlbumDayTitleModel sectionItem = ((AlbumDayTitleModel) mUsersAndSectionList.get(position));
            ((HeaderViewHolder) holder).headerTitle.setText(sectionItem.title);

            ((HeaderViewHolder) holder).headerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentposition  = headerPositionList.indexOf(position);
                    for(int i = currentposition;i<headerPositionList.get(currentposition+1);i++){
                        GalleryModel galleryModel = ((GalleryModel) mUsersAndSectionList.get(i+1));
                        galleryModel.setChecked(true);
                    }
                    notifyItemRangeChanged(currentposition,headerPositionList.get(currentposition+1));

                }
            });

        }
        else if(holder instanceof ItemViewHolder){
            GalleryModel galleryModel = ((GalleryModel) mUsersAndSectionList.get(position));
            String FileName =  galleryModel.getFilename();
            File outputFile = new File(GalleryAppCode.Path,FileName);
            Bitmap bitmap  = BitmapUtils.resize(context, Uri.fromFile(outputFile),100);
            ((ItemViewHolder) holder).itemContent.setImageBitmap(bitmap);
            ((ItemViewHolder) holder).checkBox.setVisibility(isSelectable?View.VISIBLE:View.INVISIBLE);
            if(galleryModel.getChecked()){
                ((ItemViewHolder) holder).checkBox.setChecked(true);
            }
            ((ItemViewHolder) holder).itemContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setSelectable(true);
                    return false;
                }
            });
            ((ItemViewHolder) holder).itemContent.setOnClickListener(view -> {
                if(isSelectable){
                    ((ItemViewHolder) holder).checkBox.setChecked(!galleryModel.getChecked());
                    galleryModel.setChecked(!galleryModel.getChecked());


                }else{
                    ArrayList<GalleryModel> ImageList = new ArrayList<>();
                    ArrayList<Integer> CurrentPositionList = new ArrayList<>();
                    GalleryList = GetImageList(headerPositionList,position);
                    for(int i = GalleryList.get(0);i <GalleryList.get(1)-1;i++){
                        GalleryModel Image = ((GalleryModel) mUsersAndSectionList.get(i+1));
                        ImageList.add(Image);
                        CurrentPositionList.add(i+1);
                    }

                    Ascending ascending = new Ascending();
                    Collections.sort(CurrentPositionList, ascending);


                    Intent intent = new Intent(context, OneImage.class);
                    int CurrentPosition = CurrentPositionList.indexOf(position);
                    intent.putExtra(GalleryAppCode.Position,CurrentPosition);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GalleryAppCode.GalleryList,ImageList); // mDataset자리에 ArrayList<GalleryModel> 형태의 넣어줘야된다.
                    intent.putExtras(bundle);
                    GalleryList = new ArrayList<>();
                    context.startActivity(intent);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mUsersAndSectionList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (mUsersAndSectionList.get(position).isSection())
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    public void setSelectable(boolean isSelectable){
        this.isSelectable = isSelectable;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> GetImageList(ArrayList<Integer> arrayList,int position){
        ArrayList<Integer> NumberList = new ArrayList<>();
        ArrayList<Integer> HeaderList = new ArrayList<>();

        arrayList.add(position);
        Ascending ascending = new Ascending();
        Collections.sort(arrayList, ascending);
        int a = arrayList.indexOf(position);
        NumberList.add(arrayList.get(a-1));
        NumberList.add(arrayList.get(a+1));
        arrayList.remove(a);
        return NumberList;
    }
    class Ascending implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headerTitle;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView)itemView.findViewById(R.id.header_id);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemContent;
        public CheckBox checkBox;
        public ItemViewHolder(View itemView) {
            super(itemView);
            itemContent = (ImageView)itemView.findViewById(R.id.item_content);
            checkBox = itemView.findViewById(R.id.albumday_checkbox);


        }
    }
}