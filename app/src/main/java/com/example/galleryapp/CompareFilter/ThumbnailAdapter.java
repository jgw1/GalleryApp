package com.example.galleryapp.CompareFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {
    private Context mContext;
    private ThumbnailsAdapterListener listener;
    private List<ThumbnailItem> thumbnailItemList;
    private int selectedIndex = 0;
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
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFilterSelected(thumbnailItem.filter);
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });

        holder.filterName.setText(thumbnailItem.filterName);

        if (selectedIndex == position) {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_selected));
        } else {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_normal));
        }

    }

    @Override
    public int getItemCount() {
        return thumbnailItemList.size();
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
