package com.example.galleryapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.galleryapp.GalleryDay.DayChildModel;
import com.example.galleryapp.GalleryDay.DayMotherModel;
import com.example.galleryapp.GalleryDay.Picture;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private DatabaseOpenHelper openHelper;
    private static volatile DatabaseAccess instance;
    private static DateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    ArrayList<DayMotherModel> allSampleData = new ArrayList<>();
    String file_name;
    DayMotherModel MD = new DayMotherModel();
    ArrayList<DayChildModel> childDataModels = new ArrayList<>();
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
        values.put("date", picture.getTime());
        values.put("longitude", picture.getLongitude());
        values.put("latitude", picture.getLatitude());
        values.put("favorite",picture.getFavorite());
        database.insert(DatabaseOpenHelper.TABLE, null, values);
    }

    public void update(Picture picture) {
        ContentValues values = new ContentValues();
        values.put("date", picture.getTime());
        values.put("longitude", picture.getLongitude());
        values.put("latitude", picture.getLatitude());
        values.put("favorite",picture.getFavorite());
        String date = Long.toString(picture.getTime());
        database.update(DatabaseOpenHelper.TABLE, values, "date = ?", new String[]{date});
    }

    public void delete(Picture picture) {
        String date = Long.toString(picture.getTime());
        database.delete(DatabaseOpenHelper.TABLE, "date = ?", new String[]{date});
    }

    public void FavoriteChange(String PictureName,Integer Favorite){
        ContentValues values = new ContentValues();
        values.put("favorite",Favorite);
        database.update(DatabaseOpenHelper.TABLE, values, "date = ?", new String[]{PictureName});
    }

    public String FileName(String longitude, String latitude){
        Cursor cursor = database.rawQuery("SELECT * From picture ORDER BY date DESC", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            double Long = cursor.getDouble(1);
            String Longitude = String.valueOf(Long);

            double Latit = cursor.getDouble(2);
            String Latitude = String.valueOf(Latit);

            if((longitude.equals(Longitude)) & (latitude.equals(Latitude))){
                Long fn = cursor.getLong(0);
                file_name = String.valueOf(fn);
                //                Log.d("HAHAHA","Success");
                cursor.close();
                return file_name;
            }
            else{
                cursor.moveToNext();
            }
        }
        cursor.close();
        return file_name;
    }


    public ArrayList getAllMemos() {
        allSampleData = new ArrayList<>();
        MD = new DayMotherModel();
        childDataModels = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * From picture ORDER BY date DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            long time = cursor.getLong(0);
//            Date a = new Date(time);
            String currentTitle = dateFormat.format(new Date(time));
            String title = dateFormat.format(new Date(prevtime));

            if (!currentTitle.equals(title)){
                prevtime = time;
                if(MD.getHeaderTitle() == null)
                {
                    MD.setHeaderTitle(currentTitle);
                    childDataModels.add(new DayChildModel(cursor.getInt(3)));
                }

                else{
                    if(cursor.isLast()){
                        if(currentTitle.equals(MD.getHeaderTitle())){
                            childDataModels.add(new DayChildModel(cursor.getInt(3)));
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                        }

                        else{
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                            MD = new DayMotherModel();
                            MD.setHeaderTitle(currentTitle);
                            childDataModels = new ArrayList<>();
                            childDataModels.add(new DayChildModel(cursor.getInt(3)));
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                        }
                    }

                    else{
                        if(currentTitle.equals(MD.getHeaderTitle())){
                            childDataModels.add(new DayChildModel(cursor.getInt(3)));
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                        }

                        else{
                            MD.setAllItemsInSection(childDataModels);
                            allSampleData.add(MD);
                            MD = new DayMotherModel();
                            MD.setHeaderTitle(currentTitle);
                            childDataModels = new ArrayList<>();
                            childDataModels.add(new DayChildModel(cursor.getInt(3)));
                        }
                    }
                }

            }else{
                if(cursor.isLast()){
                    if(currentTitle.equals(MD.getHeaderTitle())){
                        childDataModels.add(new DayChildModel(cursor.getInt(3)));
                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);
                    }
                    else{

                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);

                        MD = new DayMotherModel();
                        MD.setHeaderTitle(currentTitle);
                        childDataModels = new ArrayList<>();
                        childDataModels.add(new DayChildModel(cursor.getInt(3)));
                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);

                    }
                }else{
                    if(currentTitle.equals(MD.getHeaderTitle())){
                        childDataModels.add(new DayChildModel(cursor.getInt(3)));
                    }
                    else{
                        MD.setAllItemsInSection(childDataModels);
                        allSampleData.add(MD);
                        MD = new DayMotherModel();
                        MD.setHeaderTitle(currentTitle);
                        childDataModels = new ArrayList<>();
                        childDataModels.add(new DayChildModel(cursor.getInt(3)));
                    }
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        return allSampleData;
    }

}
