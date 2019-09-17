package com.example.galleryapp.GalleryDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Picture {
    private Date date;
    private int favorite = 0;
    protected String HashTag1,HashTag2,HashTag3;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm aaa");
    private Double Longitude,Latitude;
    private static DateFormat date_Format = new SimpleDateFormat("MM월 dd일");

    public Picture(long time, Double longitude,Double latitude, Integer favorite,String HashTag1,String HashTag2,String HashTag3) {
        this.date = new Date(time);
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.favorite = favorite;
        this.HashTag1 = HashTag1;
        this.HashTag2 = HashTag2;
        this.HashTag3 = HashTag3;
    }

    public Picture() {
        this.date = new Date();
    }



    public void setLongitude(Double Longitude){
        this.Longitude=Longitude;
    }

    public Double getLongitude(){
        return Latitude;
    }

    public void setLatitude(Double Latitude){
        this.Latitude=Latitude;
    }

    public Double getLatitude(){
        return Longitude;
    }

    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        this.date = new Date(time);
    }

    public int getFavorite(){
        return favorite;
    }

    public void setFavorite(int secret){
        this.favorite =secret;
    }


}
