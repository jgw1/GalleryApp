package com.example.galleryapp.CompareFilter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.GalleryAppCode;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {
    private Context mContext;
    private ThumbnailsAdapterListener listener;
    private List<ThumbnailItem> thumbnailItemList;
    private int LeftFilterIndex,RightFilterIndex;
    private int currentindex;
    private ViewHolder viewHolder;
    public ThumbnailAdapter(Context context,List<ThumbnailItem> thumbnailItemList,ThumbnailsAdapterListener listener){
        mContext = context;
        this.thumbnailItemList = thumbnailItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filterlistitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ThumbnailItem thumbnailItem = thumbnailItemList.get(position);

        holder.thumbnail.setImageBitmap(thumbnailItem.image);
        holder.thumbnail.setBackgroundColor(Color.RED);
        holder.thumbnail.setOnClickListener(view -> {
            listener.onFilterSelected(thumbnailItem.filter);
            currentindex = position;
            notifyDataSetChanged();
        });

        holder.filterName.setText(thumbnailItem.filterName);

        if (position == currentindex) {

            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_selected));
        } else {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_normal));
        }
    }

    @Override
    public int getItemCount() {
        return thumbnailItemList.size();
    }

    public void Swipe(String ChangeImage,String Direction){

        currentindex =getCurrentIndex(ChangeImage);
        if(Direction == GalleryAppCode.GoLeft){
            if(currentindex-1<0){
                currentindex = getItemCount()-1;
            }else{
                currentindex -= 1;
            }
        }else if(Direction == GalleryAppCode.GoRight){
            if(currentindex+1>getItemCount()-1){
                currentindex = 0;
            }else{
                currentindex += 1;
            }
        }
        setCurrentindex(currentindex,ChangeImage);
        ThumbnailItem item =  thumbnailItemList.get(currentindex);
        listener.onFilterSelected(item.filter);
        notifyDataSetChanged();
    }

    public int getCurrentIndex(String ChangeImage){
        if(ChangeImage=="LEFT"){
            currentindex =LeftFilterIndex;
        }else if(ChangeImage=="RIGHT"){
            currentindex=RightFilterIndex;
        }
        return currentindex;
    }

    public void setCurrentindex(int index,String changeImage){
        if(changeImage=="LEFT"){
            LeftFilterIndex =index;
        }else if(changeImage=="RIGHT"){
            RightFilterIndex=index;
        }
    }
    public void setIndex(String ChangeImage){
        if(ChangeImage=="LEFT"){
             currentindex=LeftFilterIndex;
        }else if(ChangeImage=="RIGHT"){
            currentindex=RightFilterIndex;
        }
        notifyDataSetChanged();
    }






    public interface ThumbnailsAdapterListener{
        void onFilterSelected(Filter filter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView filterName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.filter_thumbnail);
            filterName = itemView.findViewById(R.id.filter_name);

        }
    }
}
