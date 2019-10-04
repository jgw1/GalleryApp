package com.example.galleryapp.Util;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.GalleryDay.DayMotherModel;

import java.util.ArrayList;

public class GetDataFromDB extends AsyncTask<Void, Void, ArrayList<DayMotherModel>> {
    
    private GalleryDBAccess galleryDBAccess;
    private ArrayList<DayMotherModel> DataForAlbumDay;
    private ArrayList<GalleryModel> DataForAlbumTotal;

    public GetDataFromDB(Activity activity){
        this.galleryDBAccess = GalleryDBAccess.getInstance(activity);
    }

    @Override
    protected  void onPreExecute(){

    }

    @Override
    protected ArrayList<DayMotherModel> doInBackground(Void... voids) {
        galleryDBAccess.open();
        DataForAlbumDay = galleryDBAccess.getDataForGallery();
        for(int i =0 ;i<DataForAlbumDay.size() ;i++){
            if(DataForAlbumDay.get(i).getHeaderTitle() == null){
                DataForAlbumDay.remove(i);
            }
        }

        galleryDBAccess.close();
        return DataForAlbumDay;
    }

    @Override
    protected void onPostExecute(ArrayList<DayMotherModel> Result){
        super.onPostExecute(Result);
    }


}