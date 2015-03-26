package com.sdsu.hoanh.geoalbum;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.hardware.Camera;
import android.widget.Button;
import android.widget.ImageView;

import com.sdsu.hoanh.geoalbum.Model.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class TakePictureActivity extends ActionBarActivity {

    private PicDetailFragment picDetailFagment = new PicDetailFragment();
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, picDetailFagment)
                    .commit();
        }

        Button acceptButton = (Button)this.findViewById(R.id._okPhoto);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePictureActivity.this.savePhoto();
            }
        });

        takePicture();
    }

    private void savePhoto()
    {

    }

    private void takePicture()
    {
        Uri fileUri;
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // is this from camera intent?
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            // get the pic and display the thumbnail.
            Uri uri = data.getData();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

//            ImageView imageView = (ImageView)this.findViewById(R.id._picImageView);//_picThumbnailImageView);
//            imageView.setImageBitmap(imageBitmap);
//
//            String   selectedImagePath = getRealPathFromURI(uri);
//            Log.i("GeoAlbum","image path " + selectedImagePath);

            Photo photo = new Photo();
            photo.setImage(imageBitmap);
            picDetailFagment.setPhoto(photo);
        }
        // user accepts the pic detailed from the pic detail fragment
//        else if(requestCode == Constants.ACCEPT_PIC_DETAIL_RESULT_CODE && resultCode == RESULT_OK )
//        {
//            Photo photo = this.picDetailFagment.getPhoto();
//
//        }
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

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

//    /** Create a file Uri for saving an image or video */
//    private static Uri getOutputMediaFileUri(int type){
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    /** Create a File for saving an image or video */
//    private static File getOutputMediaFile(int type){
//        // To be safe, you should check that the SDCard is mounted
//        // using Environment.getExternalStorageState() before doing this.
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
//        if (! mediaStorageDir.exists()){
//            if (! mediaStorageDir.mkdirs()){
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE){
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_"+ timeStamp + ".jpg");
//        } else if(type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_"+ timeStamp + ".mp4");
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PicDetailFragment extends Fragment {
//
//        public PicDetailFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_take_picture, container, false);
//            return rootView;
//        }
//    }
}
