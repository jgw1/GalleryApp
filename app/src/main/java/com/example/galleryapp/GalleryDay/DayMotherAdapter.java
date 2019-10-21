package com.example.galleryapp.GalleryDay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.OnSwipeTouchListener;

import java.util.ArrayList;

public class DayMotherAdapter extends RecyclerView.Adapter<DayMotherAdapter.ItemRowHolder> {
    private ArrayList<DayMotherModel> mDataset;
    private Context mContext;
    private boolean isSelectable = false;
    public DayMotherAdapter(Context context, ArrayList<DayMotherModel> allSampleData) {
        this.mContext = context;
        this.mDataset = allSampleData;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daymother,null);
        ItemRowHolder vh = new ItemRowHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        final String sectionName = mDataset.get(position).getHeaderTitle();
        final String shotaddress = mDataset.get(position).getShotaddress();

        ArrayList singleSectionItems = mDataset.get(position).getAllItemsInSection();

        holder.itemTitle.setText(sectionName);
        holder.shotaddress.setText(shotaddress);


        DayChildAdapter itemListDataAdapter = new DayChildAdapter(mContext, singleSectionItems);

        holder.recycler_view_list.setHasFixedSize(true);
        holder.recycler_view_list.setAdapter(itemListDataAdapter);
        holder.recycler_view_list.setLayoutManager(new GridLayoutManager(mContext,6));

        holder.recycler_view_list.addOnItemTouchListener(new RecyclerViewClickListener(mContext, holder.recycler_view_list, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemposition) {
//                if(isSelectable){
//                    CheckBox checkBox = view.findViewById(R.id.checkBox_day);
//                    checkBox.setChecked(true);
//                }

                itemListDataAdapter.toggleChecked(itemposition);
                itemListDataAdapter.notifyItemChanged(itemposition);
//                view.setBackgroundColor(Color.RED);
                Log.d("ClickListener","ItemPosition : "+ itemposition);
                Log.d("ClickListener","DatasetPosition : "+ position);
                Log.d("ClickListener","getSelectable : "+ isSelectable);
                //                mDataset.get(position).getAllItemsInSection().get(itemposition).setChecked(true);
//                DayChildAdapter dayChildAdapter = (DayChildAdapter) holder.recycler_view_list.getAdapter();
//
//                Log.d("POSITION","POSITION : "   + itemposition);
//                Log.d("POSITION","Dataset Position : "   + position);
            }

            @Override
            public void onLongItemClick(View view, int itemposition) {
                setSelectable(true);
                CheckBox checkBox = view.findViewById(R.id.checkBox_day);
                checkBox.setVisibility(View.VISIBLE);
            }

        }));

        for(int i = 0;i<mDataset.size();i++){
            if(mDataset.get(i).getChecked() == true){
                DayChildAdapter dayChildAdapter = (DayChildAdapter) holder.recycler_view_list.getAdapter();
                dayChildAdapter.setSelectable(true);
            }
        }

        Log.d("DATASETSIZE","DatasetSize " + mDataset.size());


    }
    public void setSelectable(boolean isSelectable){
        this.isSelectable = isSelectable;

        Log.d("sdfasdfsdfsdf","asdfasdfasdf");
    }
    public boolean getSelectable(){
        return isSelectable;
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder{
        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected TextView shotaddress;



        public ItemRowHolder(View itemView) {
            super(itemView);
            this.itemTitle = itemView.findViewById(R.id.itemTitle);
            this.recycler_view_list = itemView.findViewById(R.id.recycler_view_list);
            this.shotaddress = itemView.findViewById(R.id.Album_Address);


        }
    }
}
