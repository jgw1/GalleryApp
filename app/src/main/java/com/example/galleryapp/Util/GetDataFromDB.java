package com.example.galleryapp.Util;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.GalleryDay.DayMotherModel;

import java.util.ArrayList;

public class GetDataFromDB extends AsyncTask<String, Void, ArrayList<DayMotherModel>> {
    private DatabaseAccess databaseAccess;
    private ArrayList<DayMotherModel> DataForAlbumDay;
    private ArrayList<GalleryModel> DataForAlbumTotal;

    public GetDataFromDB(Activity activity){
        this.databaseAccess = DatabaseAccess.getInstance(activity);
    }
    @Override

    protected  void onPreExecute(){

    }

    @Override
    protected ArrayList<DayMotherModel> doInBackground(String... strings) {
        databaseAccess.open();
        DataForAlbumDay = databaseAccess.getDataForGallery();
        for(int i =0 ;i<DataForAlbumDay.size() ;i++){
            if(DataForAlbumDay.get(i).getHeaderTitle() == null){
                DataForAlbumDay.remove(i);
            }
        }

        databaseAccess.close();
        return DataForAlbumDay;
    }

    @Override
    protected void onPostExecute(ArrayList<DayMotherModel> Result){

    }


}