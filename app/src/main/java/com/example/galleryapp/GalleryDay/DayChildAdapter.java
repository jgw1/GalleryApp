package com.example.galleryapp.GalleryDay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryFragment;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DayChildAdapter extends RecyclerView.Adapter<DayChildAdapter.ViewHolder> {
    private ArrayList<GalleryModel> itemModelArrayList;
    Context mContext;
    private boolean isSelectable,isAllChecked= false;


    public DayChildAdapter(Context context, ArrayList<GalleryModel> itemModelArrayList){
        this.itemModelArrayList = itemModelArrayList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daychild,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GalleryModel galleryModel = itemModelArrayList.get(position);
        String FileName = galleryModel.getFilename();
        File outputFile = new File(GalleryAppCode.Path,FileName);
//        holder.thumbnail.setImageURI(Uri.fromFile(outputFile));
        Bitmap bitmap  = BitmapUtils.resize(mContext, Uri.fromFile(outputFile),100);

        holder.thumbnail.setImageBitmap(bitmap);

        holder.itemView.setBackgroundColor(galleryModel.getChecked()?Color.RED: Color.TRANSPARENT);
//        holder.thumbnail.setImageResource(R.drawable.bubble_mask);
//        Log.d("GWGWGWGWGWGWGW", "OUTPUTFILE" + Uri.fromFile(outputFile));

//        holder.checkBox.setVisibility(isSelectable?View.VISIBLE:View.INVISIBLE);
//        if(getCheckedToggleAll()){
//            holder.checkBox.setChecked(true);
//        }
////        holder.checkBox.setChecked(galleryModel.getChecked()?true:false);
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isSelectable){
//                    galleryModel.setChecked(!galleryModel.getChecked());
//                    holder.checkBox.setChecked(!galleryModel.getChecked());
//                }else{
//                    Intent intent = new Intent(mContext, OneImage.class);
//                    intent.putExtra(GalleryAppCode.Position,position);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(GalleryAppCode.GalleryList,itemModelArrayList);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                }
//
//            }
//        });
//        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//               setSelectable(true);
//                return false;
//            }
//        });
    }


    public void setSelectable(boolean isSelectable){

        this.isSelectable = isSelectable;
        notifyDataSetChanged();
        Log.d("sdfasdfsdfsdf","asdfasdfasdf");
    }

    public boolean getCheckedToggleAll(){
        return isAllChecked;
    }
    public boolean getSelectable(){
        return isSelectable;
    }
    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }
    public void toggleChecked(int position){
        getItem(position).setChecked(!getItem(position).getChecked());
    }
    public GalleryModel getItem(int position){
        return itemModelArrayList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView thumbnail;
        protected CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.checkBox = itemView.findViewById(R.id.checkBox_day);
            checkBox.setVisibility(View.INVISIBLE);

        }
    }
}
