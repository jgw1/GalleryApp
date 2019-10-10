package com.example.galleryapp.CompareFilter;

import com.zomato.photofilters.imageprocessors.Filter;

import java.io.Serializable;

public class FilterModel implements Serializable {
    private int SampleFilter,Brightness;
    private float Contrast,Saturation;
    private String filtername;

    public FilterModel(String FilterName,int SampleFilter,int Brightness, float Contrast,float Saturation){
        this.filtername = FilterName;
        this.SampleFilter = SampleFilter;
        this.Brightness = Brightness;
        this.Contrast = Contrast;
        this.Saturation = Saturation;

    }

    public FilterModel(){
        this.filtername = new String();
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

    public float getContrast() {
        return Contrast;
    }

    public void setContrast(float contrast) {
        Contrast = contrast;
    }

    public float getSaturation() {
        return Saturation;
    }

    public void setSaturation(float saturation) {
        Saturation = saturation;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }
}
