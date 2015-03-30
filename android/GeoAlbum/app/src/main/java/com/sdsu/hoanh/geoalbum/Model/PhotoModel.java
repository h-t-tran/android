package com.sdsu.hoanh.geoalbum.Model;

import java.util.List;


public class PhotoModel {
    private static PhotoModel instance;

    public static PhotoModel getInstance()
    {
        if(instance == null)
        {
            instance = new PhotoModel();
        }

        return instance;
    }

    public boolean savePhoto(Photo photo)
    {
        return PhotoDatabaseHelper.getInstance(null)
                        .insertOrUpdateTeacher(photo);
    }

    public Photo getPhoto(int photoId)
    {
        return null;
    }

    public List<Photo> getPhotos()
    {
        return null;
    }
}
