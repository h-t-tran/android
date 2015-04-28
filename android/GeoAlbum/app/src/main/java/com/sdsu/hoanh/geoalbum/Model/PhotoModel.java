package com.sdsu.hoanh.geoalbum.Model;

import com.sdsu.hoanh.geoalbum.Constants;

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
        long rowId = PhotoDatabaseHelper.getInstance(null)
                        .insertOrUpdateTeacher(photo);

        // save the photo row ID.
        //photo.setId(rowId);

        return rowId != Constants.INVALID_DB_ROW_ID;
    }

    public Photo getPhoto(int photoId)
    {
        return  PhotoDatabaseHelper.getInstance(null).getPhoto(photoId);
    }

    public List<Photo> getPhotos()
    {
        return PhotoDatabaseHelper.getInstance(null).getAllPhotos();
    }
}
