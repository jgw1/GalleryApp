package com.example.galleryapp.GalleryDay;

import com.example.galleryapp.Gallery.GalleryModel;

import java.util.ArrayList;

public class DayMotherModel {
    private String headerTitle;
    private String shotaddress;
    private ArrayList<GalleryModel> allItemsInSection;
    private boolean checked = false;


    public DayMotherModel() {

    }
    public DayMotherModel(String headerTitle,String shotaddress, ArrayList<GalleryModel> allItemsInSection) {
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
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return this.checked;
    }

}
