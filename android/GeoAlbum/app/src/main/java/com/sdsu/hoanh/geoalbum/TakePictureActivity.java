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


public class TakePictureActivity extends PictureDetailActivity { //ActionBarActivity {


    //private PicDetailFragment _picDetailFagment = new PicDetailFragment();
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_take_picture);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, _picDetailFagment)
//                    .commit();
//        }
//
//
//
//        Button acceptButton = (Button)this.findViewById(R.id._okPhoto);
//        acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // save pic
//                if(TakePictureActivity.this._picDetailFagment.savePhoto()) {
//
//                    // once saved return to main activity
//                    TakePictureActivity.this.finish();
//                }
//            }
//        });
//
//        Button cancelButton = (Button)this.findViewById(R.id._cancelPhoto);
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // user cancel, so we want to delete the image first
//                TakePictureActivity.this._picDetailFagment.deletePhoto();
//                TakePictureActivity.this.finish();
//            }
//        });

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
        }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_take_picture, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
