package com.example.galleryapp.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;

import java.io.File;
import java.util.ArrayList;

public class MapRecyclerViewAdapter extends RecyclerView.Adapter<MapRecyclerViewAdapter.ViewHolder> {
    private ArrayList<MapRecyclerViewModel> mDataset;
    private Context mContext;

    public MapRecyclerViewAdapter(Context context, ArrayList<MapRecyclerViewModel> allSampleData) {
        this.mContext = context;
        this.mDataset = allSampleData;
    }
    @Override
    public MapRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_item,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MapRecyclerViewAdapter.ViewHolder holder, int position) {
        String file_name = mDataset.get(position).getFilename();
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest/",file_name+".jpg");
        holder.imageView.setImageURI(Uri.fromFile(outputFile));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(mContext, OneImage.class);
                inty.putExtra("position",position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("List",mDataset);
                inty.putExtras(bundle);
                mContext.startActivity(inty);
            }
        });

        /*       final String thumbnail =  mDataset.get(position).getHeaderTitle();

        if(thumbnail != "")
        {
            thumbnail = Picture_File_Name -> Picture_Path + thumbnail = Image
            Bitmap bitmap = Thumbnail.MakeThumbnail(Picture_Path);
            holder.imageView.setImageBitmap(bitmap);
        }

        holder.imageView.setOnClickListener(view -> {
        Intent intent = new Intent(mContext, AlbumView.class);
        intent.putExtra("List", mDataset);-- 클러스터 내의 사진들만 돌려서 볼수 있게금 조성
        intent.putExtra("PicturePath", video.getPath()); -- 해당 경로 제공
        mContext.startActivity(intent);
        });
  */  }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.map_item);

        }
    }
}
