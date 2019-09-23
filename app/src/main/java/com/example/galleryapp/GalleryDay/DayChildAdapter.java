package com.example.galleryapp.GalleryDay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryFragment;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.Thumbnail;

import java.io.File;
import java.util.ArrayList;

public class DayChildAdapter extends RecyclerView.Adapter<DayChildAdapter.ViewHolder> {
    private ArrayList<DayChildModel> itemModelArrayList;
    Context mContext;


    public DayChildAdapter(Context context, ArrayList<DayChildModel> itemModelArrayList){
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
        DayChildModel childDataModel = itemModelArrayList.get(position);
        String FileName = childDataModel.getFilename();
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest/",FileName+".jpg");
        holder.thumbnail.setImageURI(Uri.fromFile(outputFile));

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(mContext, OneImage.class);
                inty.putExtra("position",position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("List",itemModelArrayList);
                inty.putExtras(bundle);
                mContext.startActivity(inty);
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}
