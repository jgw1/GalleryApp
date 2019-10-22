package com.example.galleryapp.GalleryDay;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.File;
import java.util.ArrayList;

public class GalleryDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private ArrayList<ItemInterface> mUsersAndSectionList;
    private Context context;

    public GalleryDayAdapter(ArrayList<ItemInterface> mUsersAndSectionList, Context context) {
        this.mUsersAndSectionList = mUsersAndSectionList;
        this.context = context;
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            AlbumDayTitleModel sectionItem = ((AlbumDayTitleModel) mUsersAndSectionList.get(position));
            ((HeaderViewHolder) holder).headerTitle.setText(sectionItem.title);
        }else if(holder instanceof ItemViewHolder){
            GalleryModel galleryModel = ((GalleryModel) mUsersAndSectionList.get(position));
            String FileName =  galleryModel.getFilename();
            File outputFile = new File(GalleryAppCode.Path,FileName);
            Bitmap bitmap  = BitmapUtils.resize(context, Uri.fromFile(outputFile),100);
            ((ItemViewHolder) holder).itemContent.setImageBitmap(bitmap);
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headerTitle;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView)itemView.findViewById(R.id.header_id);
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemContent;
        public ItemViewHolder(View itemView) {
            super(itemView);
            itemContent = (ImageView)itemView.findViewById(R.id.item_content);
        }
    }
}