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
import com.zomato.photofilters.imageprocessors.Filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FilterDBAccess {
    private SQLiteDatabase database;
    private FilterDBOpenHelper openHelper;
    private static volatile FilterDBAccess instance;
    private FilterModel filterModel;

    private String filtername;

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
        values.put("FilterName", filterModel.getFiltername());
        values.put("SampleFilter", filterModel.getSampleFilter());
        values.put("Brightness", filterModel.getBrightness());
        values.put("Saturation", filterModel.getSaturation());
        values.put("Contrast", filterModel.getContrast());
        database.insert(GalleryDBOpenHelper.TABLE, null, values);
    }

    public void update(FilterModel filterModel) {
        ContentValues values = new ContentValues();
        values.put("FilterName", filterModel.getFiltername());
        values.put("SampleFilter", filterModel.getSampleFilter());
        values.put("Brightness", filterModel.getBrightness());
        values.put("Saturation", filterModel.getSaturation());
        values.put("Contrast", filterModel.getContrast());

        String filtername = filterModel.getFiltername();
        database.update(GalleryDBOpenHelper.TABLE, values, "name = ?", new String[]{filtername});
    }

    public void delete(FilterModel filterModel) {
        String filtername = filterModel.getFiltername();
        database.delete(GalleryDBOpenHelper.TABLE, "name = ?", new String[]{filtername});
    }


}
