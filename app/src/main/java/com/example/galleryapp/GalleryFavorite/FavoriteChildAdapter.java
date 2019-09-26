package com.example.galleryapp.GalleryFavorite;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.File;
import java.util.ArrayList;

public class FavoriteChildAdapter extends RecyclerView.Adapter<FavoriteChildAdapter.ViewHolder> {
    private ArrayList<GalleryModel> itemModelArrayList;
    Context mContext;


    public FavoriteChildAdapter(Context context, ArrayList<GalleryModel> itemModelArrayList){
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
        holder.thumbnail.setImageURI(Uri.fromFile(outputFile));
        Log.d("GWGWGWGWGWGWGW", "OUTPUTFILE" + Uri.fromFile(outputFile));
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OneImage.class);
                intent.putExtra(GalleryAppCode.Position,position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GalleryAppCode.GalleryList,itemModelArrayList);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
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
