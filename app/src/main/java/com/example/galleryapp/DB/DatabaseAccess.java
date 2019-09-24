package com.example.galleryapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.GalleryDay.DayChildModel;
import com.example.galleryapp.GalleryDay.DayMotherModel;
import com.example.galleryapp.GalleryDay.Picture;
import com.example.galleryapp.Map.MapRecyclerViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private DatabaseOpenHelper openHelper;
    private static volatile DatabaseAccess instance;
    private Picture picture;
    private static DateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    private String hashtag1,hashtag2,hashtag3;
    private Double Longit,latit;
    private int favorite;
    ArrayList<DayMotherModel> allSampleData = new ArrayList<>();
    String file_name;
    DayMotherModel MD = new DayMotherModel();
    ArrayList<GalleryModel> childDataModels = new ArrayList<>();
    ArrayList<MapRecyclerViewModel> mapRecyclerViewModels = new ArrayList<>();
    Long prevtime = Long.valueOf(0);

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
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

    public void save(Picture picture) {
        ContentValues values = new ContentValues();
        //db.execSQL("CREATE TABLE picture(date INTEGER PRIMARY KEY, longitude DOUBLE, latitude DOUBLE,Favorite integer,HASHTAG1 String,HASHTAG2 String,HASHTAG3 String)");
        values.put("date", picture.getTime());
        values.put("longitude", picture.getLongitude());
        values.put("latitude", picture.getLatitude());
        values.put("HASHTAG1",picture.getHashTag1());
        values.put("HASHTAG2",picture.getHashTag2());
        values.put("HASHTAG3",picture.getHashTag3());
        values.put("favorite",picture.getFavorite());
        database.insert(DatabaseOpenHelper.TABLE, null, values);
    }

    public void update(Picture picture) {
        ContentValues values = new ContentValues();
        values.put("date", picture.getTime());
        values.put("longitude", picture.getLongitude());
        values.put("latitude", picture.getLatitude());
        values.put("HASHTAG1",picture.getHashTag1());
        values.put("HASHTAG2",picture.getHashTag2());
        values.put("HASHTAG3",picture.getHashTag3());
        values.put("favorite",picture.getFavorite());

        String date = picture.getTime();
        database.update(DatabaseOpenHelper.TABLE, values, "date = ?", new String[]{date});
    }

    public void delete(Picture picture) {
        String date = picture.getTime();
        database.delete(DatabaseOpenHelper.TABLE, "date = ?", new String[]{date});
    }

    public void FavoriteChange(String PictureName,Integer Favorite){
        ContentValues values = new ContentValues();
        values.put("favorite",Favorite);
        database.update(DatabaseOpenHelper.TABLE, values, "date = ?", new String[]{PictureName});
    }
    public void InsertData(String file_name,Double longitude, Double latitude,String hashtag1,String hashtag2,String hashtag3){
        instance.open();
        Picture temp = new Picture();
        if(picture == null) {
            temp.setTime(file_name);
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);
            temp.setHashTag1(hashtag1);
            temp.setHashTag2(hashtag2);
            temp.setHashTag3(hashtag3);
          instance.save(temp);
        } else {
            // Update the memo
            temp.setTime(file_name);
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);
            temp.setHashTag1(hashtag1);
            temp.setHashTag2(hashtag2);
            temp.setHashTag3(hashtag3);
            instance.update(picture);
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
            Longit = cursor.getDouble(1);
            latit = cursor.getDouble(2);
            favorite = cursor.getInt(3);
            hashtag1 = cursor.getString(4);
            hashtag2 = cursor.getString(5);
            hashtag3 = cursor.getString(6);
            DataFromDatabase.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
            cursor.moveToNext();
        }
        cursor.close();
        return DataFromDatabase;
    }

    public ArrayList getDataForGallery() {
        allSampleData = new ArrayList<>();

        MD = new DayMotherModel();
        childDataModels = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * From picture ORDER BY date DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Long time = cursor.getLong(0);

            file_name = cursor.getString(0);
            Longit = cursor.getDouble(1);
            latit = cursor.getDouble(2);
            favorite = cursor.getInt(3);
            hashtag1 = cursor.getString(4);
            hashtag2 = cursor.getString(5);
            hashtag3 = cursor.getString(6);


            Log.d("GWGWGWGWGWGW","time " + time);
//            Date a = new Date(time);
            String currentTitle = dateFormat.format(new Date(time));
            String title = dateFormat.format(new Date(prevtime));


            Log.d("GWGWGWGWGWGW","file_name " + file_name);

            if (!currentTitle.equals(title)){
                prevtime = time;
                if(MD.getHeaderTitle() == null)
                {
                    MD.setHeaderTitle(currentTitle);
                    childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                }

                else{
                    if(cursor.isLast()){
                        if(currentTitle.equals(MD.getHeaderTitle())){
                            childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                        }

                        else{
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                            MD = new DayMotherModel();
                            MD.setHeaderTitle(currentTitle);
                            childDataModels = new ArrayList<>();
                            childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                        }
                    }

                    else{
                        if(currentTitle.equals(MD.getHeaderTitle())){
                            childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                        }

                        else{
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                            MD = new DayMotherModel();
                            MD.setHeaderTitle(currentTitle);
                            childDataModels = new ArrayList<>();
                            childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                        }
                    }
                }

            }else{
                if(cursor.isLast()){
                    if(currentTitle.equals(MD.getHeaderTitle())){
                        childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);
                    }
                    else{

                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);

                        MD = new DayMotherModel();
                        MD.setHeaderTitle(currentTitle);
                        childDataModels = new ArrayList<>();
                        childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);
                    }

                }else{
                    if(currentTitle.equals(MD.getHeaderTitle())){
                        childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                    }
                    else{
                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);
                        MD = new DayMotherModel();
                        MD.setHeaderTitle(currentTitle);
                        childDataModels = new ArrayList<>();
                        childDataModels.add(new GalleryModel(file_name,Longit,latit,hashtag1,hashtag2,hashtag3,favorite));
                    }
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        return allSampleData;
    }

}
