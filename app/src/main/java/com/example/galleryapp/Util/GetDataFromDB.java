package com.example.galleryapp.Util;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;

import java.util.ArrayList;

public class GetDataFromDB extends AsyncTask<Void, Void, ArrayList<GalleryModel>> {
    
    private GalleryDBAccess galleryDBAccess;
    private ArrayList<GalleryModel> DataForAlbumTotal;

    public GetDataFromDB(Activity activity){
        this.galleryDBAccess = GalleryDBAccess.getInstance(activity);
    }

    @Override
    protected  void onPreExecute(){

    }

    @Override
    protected ArrayList<GalleryModel> doInBackground(Void... voids) {
        galleryDBAccess.open();
        DataForAlbumTotal = galleryDBAccess.getDataForMap();
        galleryDBAccess.close();
        return DataForAlbumTotal;
    }

    @Override
    protected void onPostExecute(ArrayList<GalleryModel> Result){
        super.onPostExecute(Result);
    }


}