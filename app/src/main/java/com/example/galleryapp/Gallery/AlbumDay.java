package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Picture;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.galleryapp.Util.GetDataFromDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlbumDay extends Fragment {
    private RecyclerView mRecyclerView;
    SQLiteDatabase database;
    private GetDataFromDB getDataFromDB;
    private DatabaseAccess databaseAccess;
    private Picture picture;
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

        allSampleData = new ArrayList<>();
//        this.databaseAccess = DatabaseAccess.getInstance(activity);
//        databaseAccess.open();
//        allSampleData = databaseAccess.getDataForGallery();
//        for(int i =0 ;i<allSampleData.size() ;i++){
//            if(allSampleData.get(i).getHeaderTitle() == null){
//                allSampleData.remove(i);
//            }
//        }
//
//        databaseAccess.close();
        getDataFromDB = new GetDataFromDB(activity);
        try {
            allSampleData = getDataFromDB.execute().get();;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("GWGW","SIZE : " + allSampleData.size());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardviewalbum);
        mRecyclerView.setHasFixedSize(true);
        DayMotherAdapter adapter = new DayMotherAdapter(getContext(),allSampleData);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

}
