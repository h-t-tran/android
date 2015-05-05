package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sdsu.hoanh.geoalbum.Model.GpsProvider;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoDatabaseHelper;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private PhotoOverviewAdapter _photoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bootstrap the database with this context.
        // Must call this first before any attempt to access DB
        PhotoDatabaseHelper database = PhotoDatabaseHelper.getInstance(this);
        //_testDbInsert(database);

        // start GPS monitoring
        GpsProvider gpsProvider = new GpsProvider();
        gpsProvider.start(this);

        wireUpHandlers();

    }

//    private void _testDbInsert(PhotoDatabaseHelper database) {
//        Photo p = new Photo(1);
//        p.setImagePath("the path2" + (new Date()).toString());
//        p.setLon(1.0);
//        p.setLat(2.0);
//        p.setDesc("desc");
//        p.setTitle("title "+ (new Date()).toString());
//        p.setDate(new Date());
//        database.insertOrUpdateTeacher(p);
//
//        database.getAllPhotos();
//        Photo photo = database.getPhoto(3);
//
//    }


    private void wireUpHandlers()
    {
        Button showMapButton = (Button)this.findViewById(R.id._btnMap);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showMap();
            }
        });


        Button takePicButton = (Button)this.findViewById(R.id._btnTakePics);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.takePicture();
            }
        });


//        Button showPhotoListing = (Button)this.findViewById(R.id._btnTableAlbum);
//        showPhotoListing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.this.showPhotoDetail();
//            }
//        });

        showRecentPhotos();
    }

    private void showRecentPhotos()
    {
        ListView recentPhotosListView = (ListView)this.findViewById(R.id._recentPhotosListView);
//
//        final ArrayList<String> list = new ArrayList<String>();
//        for(int i=0; i < 10; i++) {
//            list.add("item1");
//            list.add("item2");
//        }
//
//        final ArrayAdapter adapter = new ArrayAdapter(this,
//                                        android.R.layout.simple_list_item_1, list);
//        recentPhotosListView.setAdapter(adapter);
//
//
//        ArrayList<Photo> photos = new ArrayList<Photo>();
//        for(int i = 0; i < 10; i++) {
//            Photo photo = new Photo();
//            photo.setTitle("photo " + i);
//            photos.add(photo);
//        }

        // newest photo is at highest row in DB, so we want to add those to begining of the
        // listview
        List<Photo> photos = PhotoModel.getInstance().getPhotos();
        final int numPhotosToDisplay = Math.min(100, photos.size());
        List<Photo> photosToDisplay = new ArrayList<Photo>();
        for(int i=0; i < numPhotosToDisplay; i++) {
            photosToDisplay.add(i, photos.get( photos.size() - i - 1));
        }

        _photoAdapter = new PhotoOverviewAdapter(photosToDisplay);
        recentPhotosListView.setAdapter(_photoAdapter);

        // add listener for when photo is selected
        recentPhotosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MainActivity.this._onPhotoSelected(position);
            }
        });
    }

    private void _onPhotoSelected(int selIndex){
        Photo selPhoto = _photoAdapter.getItem(selIndex);

        // display the detail view.
        showPhotoDetail(selPhoto.getId());
    }
    private void showMap()
    {
        Intent i = new Intent(this, MapsActivity.class);
        this.startActivity(i);

    }

    private void showPhotoDetail(long photoId)
    {
        Intent intent = new Intent(this, PictureDetailActivity.class);
        intent.putExtra(PictureDetailActivity.PHOTO_ID_KEY, photoId);
        this.startActivity(intent);

    }

    private void takePicture()
    {
        Intent i = new Intent(this, TakePictureActivity.class);
        this.startActivityForResult(i, TakePictureActivity.TAKE_PHOTO_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK &&
           requestCode == TakePictureActivity.TAKE_PHOTO_IMAGE_ACTIVITY_REQUEST_CODE) {

            long newPhotoId = data.getLongExtra(PictureDetailActivity.PHOTO_ID_KEY, Constants.INVALID_PHOTO_ID);

            if(newPhotoId != Constants.INVALID_PHOTO_ID) {
                // add to the list of photo.
                Photo photo = PhotoModel.getInstance().getPhoto((int)newPhotoId);
                _photoAdapter.insert(photo, 0);  // show top of list as newest.
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
     * custom adapter to show the photo data
     */
    private class PhotoOverviewAdapter extends ArrayAdapter<Photo> {
        public PhotoOverviewAdapter(List<Photo> photos) {
            super(MainActivity.this, android.R.layout.simple_list_item_1, photos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = MainActivity.this.getLayoutInflater()
                        .inflate(R.layout.photo_item, null);
            }

            // get the i-th photo
            Photo nthPhoto = this.getItem(position);

            // crate URI to the photo path and load it in the ImageView
            Uri uri = Uri.parse(nthPhoto.getImagePath());
            ImageView viewer = (ImageView)convertView.findViewById(R.id._photoImageView);
            viewer.setImageURI(uri);

            //
            // take care of other photo metadata
            //
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id._photoTitleTextView);
            titleTextView.setText(nthPhoto.getTitle());

            CheckBox solvedCheckBox =
                    (CheckBox)convertView.findViewById(R.id._photoDeleteCheckbox);
            solvedCheckBox.setChecked(false);

            return convertView;
        }
    }
}
