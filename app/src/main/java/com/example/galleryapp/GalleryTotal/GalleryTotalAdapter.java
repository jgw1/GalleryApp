package com.example.galleryapp.GalleryTotal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.GalleryDay.DayChildAdapter;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class GalleryTotalAdapter  extends RecyclerView.Adapter<GalleryTotalAdapter.ViewHolder>{
    private ArrayList<GalleryModel> mDataset;
    private Context context;
    private boolean isSelectable = false;

    public GalleryTotalAdapter(Context context, ArrayList<GalleryModel> list_galleryTotal) {
        this.context = context;
        this.mDataset = list_galleryTotal;
    }


    @Override
    public GalleryTotalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallerytotalitem,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final GalleryModel galleryModel = mDataset.get(position);
       String FileName =  galleryModel.getFilename();
       File outputFile = new File(GalleryAppCode.Path,FileName);
        Bitmap bitmap  = BitmapUtils.resize(context, Uri.fromFile(outputFile),100);


        holder.checkBox.setVisibility(isSelectable?View.VISIBLE:View.INVISIBLE);
        if( isSelectable ){
            holder.checkBox.setImageResource(galleryModel.getChecked()?R.mipmap.checked:R.mipmap.uncheck);
        }else{
            galleryModel.setChecked(false);
        }
       holder.imageView.setImageBitmap(bitmap);
       holder.imageView.setOnClickListener(view -> {
           if(getSelectable()){
                setCheckedToggle(position);
           }else{
               

               Intent intent = new Intent(context, OneImage.class);
               intent.putExtra(GalleryAppCode.Position,position);
               Bundle bundle = new Bundle();
               bundle.putSerializable(GalleryAppCode.GalleryList,mDataset);
               intent.putExtras(bundle);
               context.startActivity(intent);
           }
       });


    }
    public void setSelectable(boolean isSelectable){
        this.isSelectable = isSelectable;
        notifyDataSetChanged();
    }
    public boolean getSelectable(){
        return isSelectable;
    }

    public void setCheckedToggle(int i) {
        if( isSelectable ) {
            mDataset.get(i).setChecked(!mDataset.get(i).getChecked());
            notifyDataSetChanged();
            
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected ImageView checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.GalleryTotalItem);
            this.checkBox = itemView.findViewById(R.id.check_total);

            imageView.setOnLongClickListener(view -> {
                setSelectable(true);
                return false;
            });
        }
    }
}
