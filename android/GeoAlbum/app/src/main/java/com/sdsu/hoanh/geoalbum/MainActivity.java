package com.sdsu.hoanh.geoalbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    private List<Photo> _allPhotos;

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

        // display all photos from db
        _showRecentPhotos();

        // make sure the controls state are updated.
        _synchSelectAllControls();
    }

    /**
     * attach handlers to the various buttons & controls
     */
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
            if(rowView != null) {
                CheckBox selPhotoCheckbox = (CheckBox) rowView.findViewById(R.id._photoDeleteCheckbox);
                selPhotoCheckbox.setChecked(selectAll);
            }
        }

        // mark/unmark each photo to be deleted.
        for(Photo eachPhoto : _allPhotos) {
            eachPhoto.setSelForDeletion(selectAll);
        }
    }

    private void _showRecentPhotos()
    {
        ListView recentPhotosListView = (ListView)this.findViewById(R.id._recentPhotosListView);

        // newest photo is at highest row in DB, so we want to add those to beginning of the
        // listview
        _allPhotos = PhotoModel.getInstance().getPhotos();
        //final int numPhotosToDisplay = Math.min(100, _allPhotos.size());

        List<Photo> photosToDisplay = new ArrayList<Photo>();
        for(int i=0; i < _allPhotos.size(); i++) {
            photosToDisplay.add(i, _allPhotos.get( _allPhotos.size() - i - 1));
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

        // find each photo that are selected for deletion and save it in the list
        for(Photo eachPhoto :  _allPhotos) {
            if(eachPhoto.isSelForDeletion()) {
                selPhotos.add(eachPhoto);
            }
        }

        // prompt user if > 1 photo is selected
        if(selPhotos.size() > 0 && MainActivity.this._askQuestion("", "Delete " + selPhotos.size() + " photo(s)?")) {

            // ask the model to delete the photos
            int deletedItems = PhotoModel.getInstance().deletePhotos(selPhotos);
            if (deletedItems == selPhotos.size()) {
                Toast.makeText(this, deletedItems + " photos deleted.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error, only " + deletedItems + " photos deleted.",
                        Toast.LENGTH_LONG).show();
            }

            // remove the delete photo from the listview and from our saved _allPhotos
            for (Photo delPhoto : selPhotos) {
                _photoAdapter.remove(delPhoto);
                _allPhotos.remove(delPhoto);
            }

            // make sure the controls state are updated.
            _synchSelectAllControls();
        }
    }

    private boolean isYes = false;
    private boolean _askQuestion(String title, String question) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(question);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                isYes = true;
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isYes = false;
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        return isYes;
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
            final Photo nthPhoto = this.getItem(position);

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

            CheckBox delCheckbox =
                    (CheckBox)convertView.findViewById(R.id._photoDeleteCheckbox);
            //solvedCheckBox.setChecked(false);
            delCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    nthPhoto.setSelForDeletion(isChecked);
                }
            });

            // save the photo id for closure
            final long photoId = nthPhoto.getId();
            viewer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // launch the photo viewer activity to display only the photo.
                    Intent intent = new Intent(MainActivity.this, PhotoViewerActivity.class);
                    intent.putExtra(PhotoViewerActivity.PHOTO_ID_KEY, photoId);
                    MainActivity.this.startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
