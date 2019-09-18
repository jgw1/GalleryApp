package com.example.galleryapp.Gallery;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Picture;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    DatabaseOpenHelper openHelper;
    ImageButton NewAlbumDay,NewAlbumTotal,NewAlbumMap,NewAlbumFavorite;
    List<Address> mResultList;
    ArrayList<DayMotherModel> allSampleData;
    DayMotherModel MD = new DayMotherModel();
    ArrayList<DayChildModel> childDataModels = new ArrayList<>();
    Long prevtime = null;
    public AlbumDay()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_albumday,

                container, false);
        return layout;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        NewAlbumDay = view.findViewById(R.id.NewAlbumDay);
        NewAlbumTotal = view.findViewById(R.id.NewAlbumTotal);
        NewAlbumMap = view.findViewById(R.id.NewAlbumMap);
        NewAlbumFavorite = view.findViewById(R.id.NewAlbumFavorite);
        this.databaseAccess = DatabaseAccess.getInstance(getContext());
        allSampleData = new ArrayList<>();
        databaseAccess.open();
        allSampleData = databaseAccess.getAllPictures();
        databaseAccess.close();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardviewalbum);
        mRecyclerView.setHasFixedSize(true);
        DayMotherAdapter adapter = new DayMotherAdapter(getContext(),allSampleData);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

}
