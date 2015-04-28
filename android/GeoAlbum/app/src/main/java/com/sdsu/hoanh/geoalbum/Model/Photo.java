package com.sdsu.hoanh.geoalbum.Model;


import android.graphics.Bitmap;

import java.util.Date;
import java.util.Random;

public class Photo {
    // unique ID of this photo.  this corresponds to the database row where this photo is stored.
    private long id;
    private double lat;
    private double lon;
    private String title;
    private String desc;
    private String imagePath;
    private Bitmap image;
    private Date date;

//    private static Random _randomGenerator = new Random();
//    static {
//        // seed it with current time.
//        _randomGenerator.setSeed(new Date().getTime());
//    }
//    public Photo() {
//        // generate a unique ID for the photo
//        id = _randomGenerator.nextInt();
//    }

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
        return image;
    }

    public void setImage(Bitmap bmp)
    {
        image = bmp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
