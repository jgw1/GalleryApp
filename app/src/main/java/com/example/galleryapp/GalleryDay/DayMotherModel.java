package com.example.galleryapp.GalleryDay;

import java.util.ArrayList;

public class DayMotherModel {
    private String headerTitle;
    private String shotaddress;
    private ArrayList<DayChildModel> allItemsInSection;


    public DayMotherModel() {

    }
    public DayMotherModel(String headerTitle,String shotaddress, ArrayList<DayChildModel> allItemsInSection) {
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

    public ArrayList<DayChildModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<DayChildModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
