package com.example.galleryapp.GalleryDay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;

import java.util.ArrayList;

public class DayMotherAdapter extends RecyclerView.Adapter<DayMotherAdapter.ItemRowHolder> {
    private ArrayList<DayMotherModel> mDataset;
    private Context mContext;

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
        holder.itemTitle.setOnClickListener(view -> {
            itemListDataAdapter.setCheckedToggleAll();
            itemListDataAdapter.notifyDataSetChanged();
            Log.d("AABB#E","Singlesec");
        });
        Log.d("AABB#E","Singlesec" + singleSectionItems);



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
