package com.example.galleryapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GalleryDBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "Pictures.db";
    public static final String TABLE = "picture";
    public static final int VERSION = 1;

    public GalleryDBOpenHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE picture(date String PRIMARY KEY, longitude DOUBLE, latitude DOUBLE,Favorite integer,HASHTAG1 String,HASHTAG2 String,HASHTAG3 String,FilterName String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
