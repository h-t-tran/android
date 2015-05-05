package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoDatabaseHelper;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.util.List;


public class PictureDetailActivity extends ActionBarActivity {
    public static final String PHOTO_ID_KEY = "com.sdsu.hoanh.geoalbum.PictureDetailActivity.PhotoId";
    public final static int DISPLAY_PHOTO_DETAIL_ACTIVITY_REQUEST_CODE = 200;

    protected PicDetailFragment _picDetailFagment = new PicDetailFragment();
    private Photo _photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture); //activity_picture_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id._picDetailContainer, _picDetailFagment)
                    .commit();
        }

        Bundle extras = getIntent().getExtras();

        // if there is a photo ID, get the real photo and save it for later use.
        if (extras != null) {
            long photoId = extras.getLong(PHOTO_ID_KEY);
            _photo = PhotoModel.getInstance().getPhoto((int)photoId);
        }


        Button acceptButton = (Button)this.findViewById(R.id._okPhoto);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save pic
                if(PictureDetailActivity.this._picDetailFagment.savePhoto()) {

                    // once we save the photo, we return to main activity
                    // pass back the intent with the ID of the new photo to the calling activity
                    Intent returnIntent = new Intent();
                    Photo recentPhoto = PictureDetailActivity.this._picDetailFagment.getPhoto();
                    returnIntent.putExtra(PHOTO_ID_KEY, recentPhoto.getId());
                    PictureDetailActivity.this.setResult(RESULT_OK, returnIntent);

                    PictureDetailActivity.this.finish();
                }
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id._cancelPhoto);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureDetailActivity.this.setResult(RESULT_CANCELED);
                PictureDetailActivity.this.finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_detail, menu);

        if(_photo != null) {
            _picDetailFagment.setExistingPhoto(_photo);
        }
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
}
