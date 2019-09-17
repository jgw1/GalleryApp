package com.example.galleryapp.GalleryDay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;

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
        if(childDataModel.getThumbnail() == 0) {
            holder.thumbnail.setImageResource(R.drawable.ic_launcher_background);
        }
        Log.d("GWGWGWGWGW","itemModelArrayList.size " + itemModelArrayList.size());
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
