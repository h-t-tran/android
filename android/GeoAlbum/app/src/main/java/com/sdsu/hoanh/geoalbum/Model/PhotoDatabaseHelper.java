package com.sdsu.hoanh.geoalbum.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sdsu.hoanh.geoalbum.Constants;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PhotoDatabaseHelper  extends SQLiteOpenHelper {

    public static final SimpleDateFormat _dateFormatter = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss");
    public static final String DB_NAME = "photo6.sqlite";
    public static final int VERSION = 1;
    private static final String PHOTO_TABLE_NAME = "photo";


    private static final String ID_PK_COL = "_id";
    private static final String TITLE_COL = "title";
    private static final String DESC_COL = "desc";
    private static final String LATITUDE_COL = "latitude";
    private static final String LONGITUDE_COL = "longitude";
    private static final String IMAGE_PATH_COL = "image_path";
    private static final String DATE_COL = "date";


    private static PhotoDatabaseHelper _instance = null;

    /**
     * Create a singleton of the the database helper using the passed in contenxt
     * @return the TeacherDatabaseHelper singleton
     */
    public static PhotoDatabaseHelper getInstance(Context appContext)
    {
        if(_instance == null)
        {
            _instance = new PhotoDatabaseHelper(appContext);

        }
        return _instance;
    }

    private PhotoDatabaseHelper(Context appContext)
    {
        super(appContext, DB_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            // CREATE TABLE IF NOT EXISTS
            String sqlCreatePhtoTable = "create table " +
                    PHOTO_TABLE_NAME +
                    " (" + ID_PK_COL + " integer primary key autoincrement, " +
                    TITLE_COL +     " TEXT , " +
                    DESC_COL +      " TEXT , " +
                    LATITUDE_COL +  " REAL, " +
                    LONGITUDE_COL + " REAL, " +
                    IMAGE_PATH_COL + " TEXT , " +
                    DATE_COL +      " TEXT )";
            db.execSQL(sqlCreatePhtoTable);


        }
        catch(Exception e)
        {
            Log.e(Constants.ThisAppName, "Unable to create table " +
                    PHOTO_TABLE_NAME + ".  Error code " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private long updatePhoto(Photo photo)
    {
        long rowsUpdated = -1;
        try {
            ContentValues content = getPhotoContentValues(photo);

            // since all photo have unique path, we'll key off of that
            String whereCondition = IMAGE_PATH_COL + " = '" + photo.getImagePath() + "' ";
            rowsUpdated = getWritableDatabase().update(PHOTO_TABLE_NAME, content, whereCondition, null);
        }
        catch(Exception e)
        {
            Log.e(Constants.ThisAppName, "Unable to insert to table " +
                    PHOTO_TABLE_NAME + ".  Error code " + e.getMessage());
        }

        return rowsUpdated;
    }

    /**
     * insert or update the photo if it already exist
     * @return the row id of newly inserted photo or Constants.INVALID_DB_ROW_ID (-1)
     * if the insert or update failed
     */
    public long insertOrUpdateTeacher(Photo photo)
    {
        long rowId = updatePhoto(photo);;
        // try update first.  If failed, then insert.
        if(rowId <= 0) {

            try {
                ContentValues content = getPhotoContentValues(photo);

                // success if we get a valid primary key back
                rowId = getWritableDatabase().insert(PHOTO_TABLE_NAME, null, content);

                photo.setId(rowId);
            }
            catch (Exception e) {
                Log.e(Constants.ThisAppName, "Unable to insert to table " +
                        PHOTO_TABLE_NAME + ".  Error code " + e.getMessage());
            }
        }

        return rowId;
    }


    /**
     * delete all photos with the specified id
     * @param photoIds - list of photo ids
     * @return number of photos deleted
     */
    public int deletePhotos(List<Long> photoIds) {
        int numSuccessDelete = 0;
        for(Long id : photoIds) {
            String deleteStatement = "DELETE FROM " + PHOTO_TABLE_NAME +
                    " WHERE " + ID_PK_COL + " = " + id;
            SQLiteDatabase db = this.getReadableDatabase();
            numSuccessDelete ++;
            try {
                db.execSQL(deleteStatement);
            } catch(Exception e) {
                Log.e(Constants.ThisAppName, "Unable to delete photo ID " + id + " from table " +
                        PHOTO_TABLE_NAME + ".  Error code " + e.getMessage());
            }
        }

        return numSuccessDelete;
    }
    public List<Photo> getAllPhotos()
    {
        List<Photo> photos = new ArrayList<Photo>();
        String selectQuery = "SELECT  * FROM " + PHOTO_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            // keep looping until end of query
            do {
                Photo photo = _generatePhotoFromCursor(cursor);
                photos.add(photo);

            } while (cursor.moveToNext());
        }
        db.close();

        return photos;
    }

    private Photo _generatePhotoFromCursor(Cursor cursor) {
        // get  the  data into array,or class variable
        String title = cursor.getString(cursor.getColumnIndex(TITLE_COL));
        int id = cursor.getInt(cursor.getColumnIndex(ID_PK_COL));
        String desc = cursor.getString(cursor.getColumnIndex(DESC_COL));
        double latitude = cursor.getDouble(cursor.getColumnIndex(LATITUDE_COL));
        double longitude = cursor.getDouble(cursor.getColumnIndex(LONGITUDE_COL));
        String imgPath = cursor.getString(cursor.getColumnIndex(IMAGE_PATH_COL));

        String dateStr = cursor.getString(cursor.getColumnIndex(DATE_COL));

        // create new photo
        Photo photo = new Photo();
        photo.setId(id);
        photo.setTitle(title);
        photo.setDesc(desc);
        photo.setImagePath(imgPath);
        photo.setLat(latitude);
        photo.setLon(longitude);

        // save the date
        try {
            photo.setDate(_dateFormatter.parse(dateStr));
        }
        catch (ParseException e) {
            // if error set the date to 1970
            photo.setDate(new Date(0));
        }
        return photo;
    }

    public List<Photo> getPhotos(String searchCriteria)
    {
        return new ArrayList<Photo>();
    }


    public Photo getPhoto(int photoId)
    {
        Photo photo = null;
        String selectQuery = "SELECT  * FROM " + PHOTO_TABLE_NAME +
                        " WHERE " + ID_PK_COL + " = " + photoId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // get  the  data into array,or class variable
                photo = _generatePhotoFromCursor(cursor);

                // once we find the photo, we are done.
                break;
            } while (cursor.moveToNext());
        }
        db.close();

        return photo;
    }

    private ContentValues getPhotoContentValues(Photo photo) {
        ContentValues content = new ContentValues();
        content.put(TITLE_COL, photo.getTitle());
        content.put(DESC_COL, photo.getDesc());
        content.put(LATITUDE_COL, photo.getLat());
        content.put(LONGITUDE_COL, photo.getLon());
        content.put(IMAGE_PATH_COL, photo.getImagePath());
        String dateStr = _dateFormatter.format(photo.getDate());
        content.put(DATE_COL, dateStr);

        return content;
    }

}
