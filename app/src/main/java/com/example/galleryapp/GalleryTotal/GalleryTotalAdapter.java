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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.GalleryDay.DayChildAdapter;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class GalleryTotalAdapter  extends RecyclerView.Adapter<GalleryTotalAdapter.ViewHolder>{
    private ArrayList<GalleryModel> mDataset;
    private Context context;

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
    public void onBindViewHolder(ViewHolder holder, int position) {

       String FileName =  mDataset.get(position).getFilename();
       File outputFile = new File(GalleryAppCode.Path,FileName);
       Bitmap bitmap = BitmapFactory.decodeFile(outputFile.getPath());
       bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
       holder.imageView.setImageBitmap(bitmap);
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               Intent intent = new Intent(context, OneImage.class);
               intent.putExtra(GalleryAppCode.Position,position);
               Bundle bundle = new Bundle();
               bundle.putSerializable(GalleryAppCode.GalleryList,mDataset);
               intent.putExtras(bundle);
               context.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.GalleryTotalItem);
        }
    }
}
