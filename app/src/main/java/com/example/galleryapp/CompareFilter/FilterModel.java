package com.example.galleryapp.CompareFilter;

public class FilterModel {
    public String FilterName;
    public int SampleFilter,Brightness,Contrast,Saturation;

    public FilterModel(String FilterName,int SampleFilter,int Brightness,int Contrast,int Saturation){
        this.FilterName = FilterName;
        this.SampleFilter = SampleFilter;
        this.Brightness = Brightness;
        this.Contrast = Contrast;
        this.Saturation = Saturation;
    }
    public FilterModel(){
        this.FilterName = null;
        this.SampleFilter = Integer.parseInt(null);
        this.Brightness = Integer.parseInt(null);
        this.Contrast = Integer.parseInt(null);
        this.Saturation = Integer.parseInt(null);
    }
    public String getFilterName() {
        return FilterName;
    }

    public void setFilterName(String filterName) {
        FilterName = filterName;
    }

    public int getSampleFilter() {
        return SampleFilter;
    }

    public void setSampleFilter(int sampleFilter) {
        SampleFilter = sampleFilter;
    }

    public int getBrightness() {
        return Brightness;
    }

    public void setBrightness(int brightness) {
        Brightness = brightness;
    }

    public int getContrast() {
        return Contrast;
    }

    public void setContrast(int contrast) {
        Contrast = contrast;
    }

    public int getSaturation() {
        return Saturation;
    }

    public void setSaturation(int saturation) {
        Saturation = saturation;
    }
}
