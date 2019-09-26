package com.example.galleryapp.GalleryFavorite;

import java.io.Serializable;

public class FavoriteChildModel implements Serializable {
    private int thumbnail, Favorite;
    private String Filename,Hashtag1,Hashtag2,Hashtag3;


    public FavoriteChildModel(String Filename, String Hashtag1, String Hashtag2, String Hashtag3, int Favorite){
        this.Filename = Filename;
        this.Hashtag1 = Hashtag1;
        this.Hashtag2 = Hashtag2;
        this.Hashtag3 = Hashtag3;
        this.Favorite = Favorite;

    }
    public int getFavorite() {
        return Favorite;
    }

    public void setFavorite(int favorite) {
        Favorite = favorite;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getHashtag1() {
        return Hashtag1;
    }

    public void setHashtag1(String hashtag1) {
        Hashtag1 = hashtag1;
    }

    public String getHashtag2() {
        return Hashtag2;
    }

    public void setHashtag2(String hashtag2) {
        Hashtag2 = hashtag2;
    }

    public String getHashtag3() {
        return Hashtag3;
    }

    public void setHashtag3(String hashtag3) {
        Hashtag3 = hashtag3;
    }
}
