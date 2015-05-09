package com.sdsu.hoanh.geoalbum;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        // add cancel handler to go back to previous activity
        Button cancelButton = (Button)this.findViewById(R.id._cancelPhoto);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewerActivity.this.finish();
            }
        });
    }

}
