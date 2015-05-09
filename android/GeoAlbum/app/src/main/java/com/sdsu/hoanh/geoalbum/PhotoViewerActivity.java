package com.sdsu.hoanh.geoalbum;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.io.File;


public class PhotoViewerActivity extends ActionBarActivity {

    public static final String PHOTO_ID_KEY = "com.sdsu.hoanh.geoalbum.PhotoViewerActivity.PhotoId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Bundle extras = getIntent().getExtras();

        // if there is a photo ID, get the real photo and save it for later use.
        if (extras != null) {
            long photoId = extras.getLong(PHOTO_ID_KEY);
            Photo photo = PhotoModel.getInstance().getPhoto((int)photoId);
            ImageView imageView = (ImageView)findViewById(R.id._photoImageView);

            // set the image if exist, else use the image path.
            if(photo.getImage() != null) {
                imageView.setImageBitmap(photo.getImage());
            }
            else {
                try {
                    Uri uri = Uri.fromFile(new File(photo.getImagePath()));
                    imageView.setImageURI(uri);
                }
                catch(Exception e)
                {
                    Log.e(Constants.ThisAppName, "Image path " +
                            photo.getImagePath() +
                            " does not exist. " + e.getMessage());
                }
            }
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_photo_viewer, menu);
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
