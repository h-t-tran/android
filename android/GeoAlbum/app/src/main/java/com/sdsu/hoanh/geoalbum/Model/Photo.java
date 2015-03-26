package com.sdsu.hoanh.geoalbum.Model;


import android.graphics.Bitmap;

public class Photo {

    private double lat;
    private double lon;
    private String title;
    private String desc;
    private String imagePath;
    private Bitmap image;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getImage()
    {
        if(image == null)
        {
            // TODO: generate image from path
        }
        return image;
    }

    public void setImage(Bitmap bmp)
    {
        image = bmp;
    }

}
