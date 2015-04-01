package com.sdsu.hoanh.geoalbum;

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
    protected PicDetailFragment _picDetailFagment = new PicDetailFragment();
    private Photo _photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture); //activity_picture_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, _picDetailFagment)
                    .commit();
        }

        Bundle extras = getIntent().getExtras();

        // if there is a photo ID, get the real photo and save it for later use.
        if (extras != null) {
            int photoId = extras.getInt(PHOTO_ID_KEY);
            _photo = PhotoModel.getInstance().getPhoto(photoId);
        }


        Button acceptButton = (Button)this.findViewById(R.id._okPhoto);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save pic
                if(PictureDetailActivity.this._picDetailFagment.savePhoto()) {

                    // once saved return to main activity
                    PictureDetailActivity.this.finish();
                }
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id._cancelPhoto);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
