package com.example.galleryapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.galleryapp.CompareFilter.FilterModel;
import com.example.galleryapp.Gallery.GalleryModel;
import com.example.galleryapp.GalleryDay.DayMotherModel;
import com.example.galleryapp.GalleryDay.Picture;
import com.example.galleryapp.Map.MapRecyclerViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FilterDBAccess {
    private SQLiteDatabase database;
    private FilterDBOpenHelper openHelper;
    private static volatile FilterDBAccess instance;
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

    private FilterDBAccess(Context context) {
        this.openHelper = new FilterDBOpenHelper(context);
    }

    public static synchronized FilterDBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new FilterDBAccess(context);
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

    public void save(FilterModel filterModel) {
        ContentValues values = new ContentValues();
        //db.execSQL("CREATE TABLE picture(date INTEGER PRIMARY KEY, longitude DOUBLE, latitude DOUBLE,Favorite integer,HASHTAG1 String,HASHTAG2 String,HASHTAG3 String)");
        values.put("FilterName", filterModel.getFilterName());
        values.put("SampleFilter", filterModel.getSampleFilter());
        values.put("Brightness", filterModel.getBrightness());
        values.put("Contrast",filterModel.getContrast());
        values.put("Saturation",filterModel.getSaturation());

        database.insert(GalleryDBOpenHelper.TABLE, null, values);
    }

    public void update(FilterModel filterModel) {
        ContentValues values = new ContentValues();
        values.put("FilterName", filterModel.getFilterName());
        values.put("SampleFilter", filterModel.getSampleFilter());
        values.put("Brightness", filterModel.getBrightness());
        values.put("Contrast",filterModel.getContrast());
        values.put("Saturation",filterModel.getContrast());

        String filterName = filterModel.getFilterName();
        database.update(GalleryDBOpenHelper.TABLE, values, "filtername = ?", new String[]{filterName});
    }

    public void delete(FilterModel filterModel) {
        String filterName = filterModel.getFilterName();
        database.delete(GalleryDBOpenHelper.TABLE, "filtername = ?", new String[]{filterName});
    }

    public void CustomFilter(String FilterName,int SampleFilter, int Brightness,int Contrast,int Saturation){
        instance.open();
        FilterModel temp = new FilterModel();
        if(picture == null) {
            temp.getFilterName(FilterName);
            temp.getSampleFilter(Brightness);
            temp.getBrightness(SampleFilter);
            temp.getContrast(Contrast);
            temp.getContrast(Saturation);

          instance.save(temp);
        } else {
            // Update the memo
            temp.getFilterName(FilterName);
            temp.getSampleFilter(Brightness);
            temp.getBrightness(SampleFilter);
            temp.getContrast(Contrast);
            temp.getContrast(Saturation);
            instance.update(temp);
        }
        instance.close();
    }

    public ArrayList getAllFilter(){
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

}
