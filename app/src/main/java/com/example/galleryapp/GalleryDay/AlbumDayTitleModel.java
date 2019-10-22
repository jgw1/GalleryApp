package com.example.galleryapp.GalleryDay;

public class AlbumDayTitleModel implements ItemInterface {
    public String title;

    public AlbumDayTitleModel(String title) {
        this.title = title;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}