package com.sdsu.hoanh.geoalbum.Model;

import com.sdsu.hoanh.geoalbum.Constants;

import java.util.ArrayList;
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

    /**
     * delete all photos with the specified id
     * @param photosToDelete - list of photo
     * @return number of photos deleted
     */
    public int deletePhotos(List<Photo> photosToDelete) {
        List<Long> photoIds = new ArrayList<>();
        for(Photo photo : photosToDelete) {
            photoIds.add(photo.getId());
        }
        return PhotoDatabaseHelper.getInstance(null).deletePhotos(photoIds);
    }

    /**
     * Retrieve all the photos
     * @return
     */
    public List<Photo> getPhotos()
    {
        return PhotoDatabaseHelper.getInstance(null).getAllPhotos();
    }
}
