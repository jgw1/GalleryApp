package com.example.galleryapp.GalleryDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Picture {
    private Date date;
    private int favorite = 0;
    private String HashTag1,HashTag2,HashTag3,time;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm aaa");
    private Double Longitude,Latitude;
    private static DateFormat date_Format = new SimpleDateFormat("MM월 dd일");

    public Picture(String time, Double longitude,Double latitude, Integer favorite,String HashTag1,String HashTag2,String HashTag3) {
        this.time = time;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.favorite = favorite;
        this.HashTag1 = HashTag1;
        this.HashTag2 = HashTag2;
        this.HashTag3 = HashTag3;
    }

    public Picture() {
        this.time = new String();
    }


    public void setHashTag1(String Hashtag1){
        this.HashTag1=Hashtag1;
    }
    public String getHashTag1(){
        return HashTag1;
    }

    public void setHashTag2(String Hashtag2){
        this.HashTag2=Hashtag2;
    }
    public String getHashTag2(){
        return HashTag2;
    }

    public void setHashTag3(String Hashtag3){
        this.HashTag3=Hashtag3;
    }
    public String getHashTag3(){
        return HashTag3;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFavorite(){
        return favorite;
    }

    public void setFavorite(int secret){
        this.favorite =secret;
    }


}
