package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Picture;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.DB.DatabaseOpenHelper;
import com.example.galleryapp.GalleryDay.DayChildModel;
import com.example.galleryapp.GalleryDay.DayMotherAdapter;
import com.example.galleryapp.GalleryDay.DayMotherModel;
import com.example.galleryapp.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumDay extends Fragment {
    private RecyclerView mRecyclerView;
    SQLiteDatabase database;
    private DatabaseAccess databaseAccess;
    private Picture picture;
    private DayMotherAdapter adapter;
    DatabaseOpenHelper openHelper;
    ImageButton NewAlbumDay,NewAlbumTotal,NewAlbumMap,NewAlbumFavorite;
    List<Address> mResultList;
    ArrayList<DayMotherModel> allSampleData;
    DayMotherModel MD = new DayMotherModel();
    ArrayList<DayChildModel> childDataModels = new ArrayList<>();
    Activity activity;

    @Override
    public void onDestroy() {
        super.onDestroy();
        allSampleData = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context  instanceof Activity){
            activity = (Activity) context;
        }

    }
    public AlbumDay()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.databaseAccess = DatabaseAccess.getInstance(activity);
        allSampleData = new ArrayList<>();

        databaseAccess.open();
        allSampleData = databaseAccess.getDataForGallery();
        for(int i =0 ;i<allSampleData.size() ;i++){
            if(allSampleData.get(i).getHeaderTitle() == null){
                allSampleData.remove(i);
            }
        }
        databaseAccess.close();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        long beforeWork = System.currentTimeMillis();
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_albumday,

                container, false);

        NewAlbumDay = layout.findViewById(R.id.NewAlbumDay);
        NewAlbumTotal = layout.findViewById(R.id.NewAlbumTotal);
        NewAlbumMap = layout.findViewById(R.id.NewAlbumMap);
        NewAlbumFavorite = layout.findViewById(R.id.NewAlbumFavorite);

        Log.d("GWGW","SIZE : " + allSampleData.size());
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.cardviewalbum);
        adapter = new DayMotherAdapter(getContext(),allSampleData);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        long afterWork = System.currentTimeMillis();
        Log.d("WorkTime","WorkTime = " + (afterWork-beforeWork));
        return layout;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }


}
