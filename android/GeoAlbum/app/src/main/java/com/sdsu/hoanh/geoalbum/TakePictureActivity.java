package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sdsu.hoanh.geoalbum.Model.Photo;

import java.util.Date;


public class TakePictureActivity extends PictureDetailActivity {

    //private PicDetailFragment _picDetailFagment = new PicDetailFragment();
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public final static int TAKE_PHOTO_IMAGE_ACTIVITY_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // do the default behavior, the invoke the camera to take picutre.
        super.onCreate(savedInstanceState);
        takePicture();
    }


    private void takePicture()
    {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // is this from camera intent?  If so, get the photo and tell the fragment to display
        // its detail.
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            // get the pic and display the thumbnail.
            Uri uri = data.getData();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Photo photo = new Photo();
            photo.setImage(imageBitmap);
            photo.setDate(new Date());
            photo.setImagePath(getRealPathFromURI(uri));

            _picDetailFagment.setNewPhoto(photo);
//
//            // pass back the intent with the ID of the new photo to the calling activity
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra(PHOTO_ID_KEY, photo.getId());
//            this.setResult(RESULT_OK, returnIntent);
        }
        else {
            this.setResult(RESULT_CANCELED);
            finish();
        }

    }

    public Photo getMostRecentPhoto() {
        return _picDetailFagment.getPhoto();
    }
    /**
     * Taken from http://androidamaranthine.blogspot.com/2013/01/get-path-of-stored-image.html
     * @param contentUri
     * @return
     */
    @SuppressWarnings("deprecation")  // ignore deprecation of managedQuery()
    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }


}
