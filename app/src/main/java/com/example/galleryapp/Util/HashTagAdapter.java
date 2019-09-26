package com.example.galleryapp.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;

import java.util.ArrayList;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ViewHolder>{
    private ArrayList<String> mDataset;
    Context mContext;

    public HashTagAdapter(Context context, ArrayList<String> Dataset){
        this.mContext= context;
        this.mDataset = Dataset;
    }

    @NonNull
    @Override
    public HashTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchtag_child,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String hashTag = mDataset.get(position);
        Log.d("AGAG",""+hashTag);
        holder.hashtag.setText(hashTag);
        holder.hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mDataset.size());

            }
        });

    }

    public void addItem(String hashtag){
        mDataset.add(hashtag);
        notifyItemInserted(mDataset.size());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView hashtag;

        public ViewHolder(View itemView){
            super(itemView);
            this.hashtag = itemView.findViewById(R.id.hashtag);


        }
    }
}
