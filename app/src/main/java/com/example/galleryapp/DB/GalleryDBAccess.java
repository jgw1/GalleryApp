package com.example.galleryapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.GalleryDay.Picture;
import com.example.galleryapp.Map.MapRecyclerViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GalleryDBAccess {
    private SQLiteDatabase database;
    private GalleryDBOpenHelper openHelper;
    private static volatile GalleryDBAccess instance;
    private Picture picture;
    private static DateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    private String hashtag1,hashtag2,hashtag3,filtername;
    private Double Longit,latit;
    private int favorite;

    String file_name;
    ArrayList<GalleryModel> childDataModels = new ArrayList<>();
    ArrayList<MapRecyclerViewModel> mapRecyclerViewModels = new ArrayList<>();
    Long prevtime = Long.valueOf(0);

    private GalleryDBAccess(Context context) {
        this.openHelper = new GalleryDBOpenHelper(context);
    }

    public static synchronized GalleryDBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new GalleryDBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void save(GalleryModel galleryModel) {
        ContentValues values = new ContentValues();
        //db.execSQL("CREATE TABLE picture(date INTEGER PRIMARY KEY, longitude DOUBLE, latitude DOUBLE,Favorite integer,HASHTAG1 String,HASHTAG2 String,HASHTAG3 String)");
        values.put("date", galleryModel.getFilename());
        values.put("longitude", galleryModel.getLongitude());
        values.put("latitude", galleryModel.getLatitude());
        values.put("HASHTAG1",galleryModel.getHashtag1());
        values.put("HASHTAG2",galleryModel.getHashtag2());
        values.put("HASHTAG3",galleryModel.getHashtag3());
        values.put("favorite",galleryModel.getFavorite());
        database.insert(GalleryDBOpenHelper.TABLE, null, values);
    }

    public void update(GalleryModel galleryModel) {
        ContentValues values = new ContentValues();
        values.put("date", galleryModel.getFilename());
        values.put("longitude", galleryModel.getLongitude());
        values.put("latitude", galleryModel.getLatitude());
        values.put("HASHTAG1",galleryModel.getHashtag1());
        values.put("HASHTAG2",galleryModel.getHashtag2());
        values.put("HASHTAG3",galleryModel.getHashtag3());
        values.put("favorite",galleryModel.getFavorite());

        String date = galleryModel.getFilename();
        database.update(GalleryDBOpenHelper.TABLE, values, "date = ?", new String[]{date});
    }

    public void delete(GalleryModel galleryModel) {
        String date = galleryModel.getFilename();
        database.delete(GalleryDBOpenHelper.TABLE, "date = ?", new String[]{date});
    }

    public void FavoriteChange(GalleryModel galleryModel,int Favorite){
        ContentValues values = new ContentValues();
        values.put("favorite",Favorite);
        String date = galleryModel.getFilename();
        database.update(GalleryDBOpenHelper.TABLE, values, "date = ?", new String[]{date});
    }
    public void InsertData(String file_name,Double latitude, Double longitude,String hashtag1,String hashtag2,String hashtag3){
        instance.open();
        GalleryModel temp = new GalleryModel();
        if(picture == null) {
            temp.setFilename(file_name);
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);
            temp.setHashtag1(hashtag1);
            temp.setHashtag2(hashtag2);
            temp.setHashtag3(hashtag3);
          instance.save(temp);
        } else {
            // Update the memo
            temp.setFilename(file_name);
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);
            temp.setHashtag1(hashtag1);
            temp.setHashtag2(hashtag2);
            temp.setHashtag3(hashtag3);
            instance.update(temp);
        }
        instance.close();
    }
    public ArrayList getDataForMap(){
        ArrayList<GalleryModel> DataFromDatabase = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * From picture ORDER BY date DESC", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            file_name = cursor.getString(0);
            latit = cursor.getDouble(1);
            Longit = cursor.getDouble(2);
            favorite = cursor.getInt(3);
            hashtag1 = cursor.getString(4);
            hashtag2 = cursor.getString(5);
            hashtag3 = cursor.getString(6);
            DataFromDatabase.add(new GalleryModel(file_name,latit,Longit,hashtag1,hashtag2,hashtag3,favorite,"Normal"));
            cursor.moveToNext();
        }
        cursor.close();
        return DataFromDatabase;
    }
}
