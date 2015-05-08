package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsu.hoanh.geoalbum.Model.GpsProvider;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoDatabaseHelper;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private PhotoOverviewAdapter _photoAdapter;
    private int _selectedPhotoIdx = Constants.INVALID_PHOTO_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bootstrap the database with this context.
        // Must call this first before any attempt to access DB
        PhotoDatabaseHelper database = PhotoDatabaseHelper.getInstance(this);


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
                MainActivity.this._showMap();
            }
        });


        Button takePicButton = (Button)this.findViewById(R.id._btnTakePics);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this._takePicture();
            }
        });

        Button deletePicButton = (Button)this.findViewById(R.id._btnDeleteSelectedPhotos);
        deletePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this._deleteSelectedPhotos();
            }
        });

        CheckBox selAllCheckbox = (CheckBox)this.findViewById(R.id._checkBoxSelectDeselectAll);
        selAllCheckbox.setChecked(false); // uncheck initially
        selAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.this._selectAllPhotos(isChecked);
            }
        });

        _showRecentPhotos();

        // make sure the controls state are updated.
        _synchSelectAllControls();
    }

    /**
     * see if there is any photo, if so enable the Delete and Select All checkbox. If
     * there is no photo, hide them
     */
    private void _synchSelectAllControls() {
        CheckBox selAllCheckbox = (CheckBox)this.findViewById(R.id._checkBoxSelectDeselectAll);
        Button deletePicButton = (Button)this.findViewById(R.id._btnDeleteSelectedPhotos);
        TextView recentPhotoTextview = (TextView)this.findViewById(R.id._recentPhotoText);

        int numPhotos = _photoAdapter.getCount();
        boolean isEnabled =  numPhotos > 0;

        //selAllCheckbox.setEnabled(isEnabled);
        //deletePicButton.setEnabled(isEnabled);

        int visibiity = isEnabled ? View.VISIBLE : View.INVISIBLE;
        selAllCheckbox.setVisibility(visibiity);
        deletePicButton.setVisibility(visibiity);

        // if we are disabling, we want to make sure the checkbox is unselected.
        if( ! isEnabled) {
            selAllCheckbox.setChecked(false);
        }

        // update the photo count
        recentPhotoTextview.setText("Recent Photos (" + numPhotos + ")");
    }

    /**
     * select all photo checkboxes or not
     * @param selectAll - true if selected all, or false if deselect all
     */
    private void _selectAllPhotos(boolean selectAll) {
        ListView recentPhotosListView = (ListView) this.findViewById(R.id._recentPhotosListView);

        // get views of all items
        for (int i = 0; i < _photoAdapter.getCount(); i++) {
            View rowView = recentPhotosListView.getChildAt(i);
            CheckBox selPhotoCheckbox = (CheckBox) rowView.findViewById(R.id._photoDeleteCheckbox);
            selPhotoCheckbox.setChecked(selectAll);
        }
    }

    private void _showRecentPhotos()
    {
        ListView recentPhotosListView = (ListView)this.findViewById(R.id._recentPhotosListView);

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
        _selectedPhotoIdx = selIndex; // save for future use
        Photo selPhoto = _photoAdapter.getItem(selIndex);

        // display the detail view.
        showPhotoDetail(selPhoto.getId());
    }

    private void  _deleteSelectedPhotos(){

        List<Photo> selPhotos = new ArrayList<Photo>();

        ListView recentPhotosListView = (ListView)this.findViewById(R.id._recentPhotosListView);

        // get all photos that are selected
        for(int i=0; i < _photoAdapter.getCount(); i++) {
            View rowView = recentPhotosListView.getChildAt(i);

            // get the checkbox from the view
            CheckBox selPhotoCheckbox =
                    (CheckBox)rowView.findViewById(R.id._photoDeleteCheckbox);

            // if photo is selected, then add it to the list to be deleted
            if(selPhotoCheckbox.isChecked()){
                Photo photo = _photoAdapter.getItem(i);
                selPhotos.add(photo);
            }
        }

        // ask the model to delete the photos
        int deletedItems = PhotoModel.getInstance().deletePhotos(selPhotos);
        if(deletedItems == selPhotos.size()) {
            Toast.makeText(this, deletedItems + " photos deleted.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Error, only " + deletedItems + " photos deleted.",
                                Toast.LENGTH_LONG).show();
        }

        // remove the delete photo from the listview
        for(Photo delPhoto : selPhotos) {
            _photoAdapter.remove(delPhoto);
        }

        // make sure the controls state are updated.
        _synchSelectAllControls();
    }
    private void _showMap()
    {
        Intent i = new Intent(this, MapsActivity.class);
        this.startActivity(i);

    }

    private void showPhotoDetail(long photoId)
    {
        Intent intent = new Intent(this, PictureDetailActivity.class);
        intent.putExtra(PictureDetailActivity.PHOTO_ID_KEY, photoId);
        this.startActivityForResult(intent, PictureDetailActivity.DISPLAY_PHOTO_DETAIL_ACTIVITY_REQUEST_CODE);

    }

    private void _takePicture()
    {
        Intent i = new Intent(this, TakePictureActivity.class);
        this.startActivityForResult(i, TakePictureActivity.TAKE_PHOTO_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == TakePictureActivity.TAKE_PHOTO_IMAGE_ACTIVITY_REQUEST_CODE) {

                long newPhotoId = data.getLongExtra(PictureDetailActivity.PHOTO_ID_KEY,
                                                        Constants.INVALID_PHOTO_ID);

                if (newPhotoId != Constants.INVALID_PHOTO_ID) {
                    // add to the list of photo.
                    Photo photo = PhotoModel.getInstance().getPhoto((int) newPhotoId);
                    _photoAdapter.insert(photo, 0);  // show top of list as newest.
                }
            }
            else if(requestCode == TakePictureActivity.DISPLAY_PHOTO_DETAIL_ACTIVITY_REQUEST_CODE) {
                // update the photo in the listview

                long newPhotoId = data.getLongExtra(PictureDetailActivity.PHOTO_ID_KEY,
                                                        Constants.INVALID_PHOTO_ID);
                if(newPhotoId != Constants.INVALID_PHOTO_ID) {
                    // add to the list of photo.
                    Photo photo = PhotoModel.getInstance().getPhoto((int)newPhotoId);
                    _updatePhotoInListView(photo);

                    // we are done with showing the detail so reset the index
                    _selectedPhotoIdx = Constants.INVALID_PHOTO_ID;
                }
            }

            // make sure the controls state are updated.
            _synchSelectAllControls();
        }

    }

    private void _updatePhotoInListView(Photo photo) {
        ListView recentPhotosListView = (ListView)this.findViewById(R.id._recentPhotosListView);
        View rowView = recentPhotosListView.getChildAt(this._selectedPhotoIdx);

        // get the title textbox from the view
        TextView titleTextView =
                (TextView)rowView.findViewById(R.id._photoTitleTextView);
        titleTextView.setText(photo.getTitle());

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
