package com.example.galleryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;

import java.util.ArrayList;
import java.util.List;

public class BoardItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private List<ItemObject> itemObjectList;


    public BoardItem(List<ItemObject> itemObjectList) {
        this.itemObjectList = itemObjectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_header_item, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
            return new ItemViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + ". Make sure you are using view types correctly!");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemObject mObject = itemObjectList.get(position);
        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).headerTitle.setText(mObject.getContents());
        }else if(holder instanceof ItemViewHolder){
            ((ItemViewHolder) holder).itemContent.setText(mObject.getContents());
        }
    }

    private ItemObject getItem(int position) {
        return itemObjectList.get(position);
    }
    @Override
    public int getItemCount() {
        return itemObjectList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return (position == 6) || (position == 0);
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headerTitle;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView)itemView.findViewById(R.id.header_id);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView itemContent;
        public ItemViewHolder(View itemView) {
            super(itemView);
            itemContent = (TextView)itemView.findViewById(R.id.item_content);
        }
    }
}