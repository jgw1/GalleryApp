package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.GalleryTotal.GalleryTotalAdapter;
import com.example.galleryapp.R;

import java.util.ArrayList;

public class AlbumTotal extends Fragment {
    private RecyclerView RV_GalleryTotalView;
    private ArrayList<GalleryModel> List_GalleryTotal;
    private Activity activity;
    private DatabaseAccess databaseAccess;
    private GalleryTotalAdapter galleryTotalAdapter;
    public AlbumTotal()
    {

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context  instanceof Activity){
            activity = (Activity) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List_GalleryTotal = new ArrayList<>();
        this.databaseAccess = DatabaseAccess.getInstance(activity);
        databaseAccess.open();
        List_GalleryTotal = databaseAccess.getDataForMap();
        databaseAccess.close();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_albumtotal,

                container, false);

        RV_GalleryTotalView = layout.findViewById(R.id.GalleryTotalView);

        galleryTotalAdapter = new GalleryTotalAdapter(getContext(),List_GalleryTotal);
        RV_GalleryTotalView.setHasFixedSize(true);
        RV_GalleryTotalView.setAdapter(galleryTotalAdapter);
        RV_GalleryTotalView.setLayoutManager(new GridLayoutManager(getContext(),3));
        return layout;
    }

    @Override
    public void onViewCreated(final View view,Bundle savedInstanceState){
        galleryTotalAdapter.notifyDataSetChanged();
        RV_GalleryTotalView.setAdapter(galleryTotalAdapter);
    }
}
