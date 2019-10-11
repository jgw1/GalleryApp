package com.example.galleryapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FilterDBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "Filters.db";
    public static final String TABLE = "Filter";
    public static final int VERSION = 1;

    public FilterDBOpenHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Filter(FilterName String PRIMARY KEY,SampleFilter int, Brightness int,Contrast float,Saturation float)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
