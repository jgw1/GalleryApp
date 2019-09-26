package com.example.galleryapp.GalleryFavorite;

import com.example.galleryapp.Gallery.GalleryModel;

import java.util.ArrayList;

public class FavoriteMotherModel {
    private String headerTitle;
    private String shotaddress;
    private ArrayList<GalleryModel> allItemsInSection;


    public FavoriteMotherModel() {

    }
    public FavoriteMotherModel(String headerTitle, String shotaddress, ArrayList<GalleryModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.shotaddress = shotaddress;
        this.allItemsInSection = allItemsInSection;
    }

    public String getShotaddress() {
        return shotaddress;
    }

    public void setShotaddress(String shotaddress) {
        this.shotaddress = shotaddress;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<GalleryModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<GalleryModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}