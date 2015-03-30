package com.sdsu.hoanh.geoalbum;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsu.hoanh.geoalbum.Model.GpsProvider;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoDatabaseHelper;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class PicDetailFragment extends Fragment {

    private Photo _photo;
    private static final Random _locationRandomizer = new Random();

    // the formatter for degree to 4 decimal places
    private static final NumberFormat _degreeFormatter = new DecimalFormat("#0.0000");

    private EditText _titleTextView;
    private EditText _descTextView;
    private EditText _locationTextEntry;
    private ImageView _imageView;
    private TextView _dateTextView;
    private TextView _pathTextView;

    public PicDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);

        _titleTextView = (EditText)rootView.findViewById(R.id._picTitle);
        _descTextView = (EditText)rootView.findViewById(R.id._picDescEntry);
        _locationTextEntry = (EditText)rootView.findViewById(R.id._picLocationEntry);
        _imageView = (ImageView)rootView.findViewById(R.id._picImageView);
        _dateTextView = (TextView)rootView.findViewById(R.id._picDateEntry);
        _pathTextView = (TextView)rootView.findViewById(R.id._picPathEntry);

        // TEST CODE
        PhotoDatabaseHelper db = PhotoDatabaseHelper.getInstance(null);
        //List<Photo> photos = db.getAllPhotos();
        Photo photo = db.getPhoto(3);
        if(photo != null)
        {
            this.setExistingPhoto(photo);
            //this.setExistingPhoto(photos.get(photos.size() - 1));
        }

        return rootView;
    }

    public boolean deletePhoto()
    {
        boolean result = false;
        // is the path exist?
        if(_photo.getImagePath() != null) {
            File fileToDelete = new File(_photo.getImagePath());
            result = fileToDelete.delete();
        }

        return result;
    }
    /**
     * Update the photo with the entered title and desc.  Then save it
     * @return true if the photo is saved.  false if user did not enter title or desc
     */
    public boolean savePhoto()
    {
        boolean isSaved = false;
        String title = _titleTextView.getText().toString();
        String desc = _descTextView.getText().toString();
        if(title == null || title.trim().length() == 0) {
            Toast.makeText(this.getActivity(),
                    "Please enter a title", Toast.LENGTH_LONG).show();
        }
        else if(desc == null || desc.trim().length() == 0) {
            Toast.makeText(this.getActivity(),
                    "Please enter a description", Toast.LENGTH_LONG).show();
        }
        else {
            _photo.setTitle(title);
            _photo.setDesc(desc);

            if(PhotoModel.getInstance().savePhoto(_photo)) {
                isSaved = true;
            }
            else {
                Toast.makeText(this.getActivity(),
                        "Unable to save photo.", Toast.LENGTH_LONG).show();
            }

        }

        return isSaved;
    }

    /**
     * inform the fragment of an existing photo.
     */
    public void setExistingPhoto(Photo photo)
    {
        this._photo = photo;

        // set the image if exist, else use the image path.
        if(photo.getImage() != null) {
            _imageView.setImageBitmap(photo.getImage());
        }
        else {
            try {
                Uri uri = Uri.fromFile(new File(photo.getImagePath()));
                _imageView.setImageURI(uri);
            }
            catch(Exception e)
            {
                Log.e(Constants.ThisAppName, "Image path " +
                        photo.getImagePath() +
                        " does not exist. " + e.getMessage());
            }
        }

        _locationTextEntry.setText(getLocationStr(photo.getLat(), photo.getLon()));
        _dateTextView.setText(PhotoDatabaseHelper._dateFormatter.format(photo.getDate()));
        _pathTextView.setText(photo.getImagePath());
        _titleTextView.setText(photo.getTitle());
        _descTextView.setText(photo.getDesc());
    }

    /**
     * inform the fragment of new photo.  It update the photo POCO with additional data
     * such as location
     */
    public void setNewPhoto(Photo photo) {

        // save the photo
        this._photo = photo;

        // if the image exist, set it in the viewer
        if(photo.getImage() != null) {
            _imageView.setImageBitmap(photo.getImage());
        }

        // get the lat/lon and display it.
        Location currLoc = GpsProvider.getInstance().getCurrLocation();

        // if there is no location, we'll generate a random location around san diego
        if(currLoc == null){
            Toast.makeText(this.getActivity(),
                    "GPS maybe disabled.  Using default location", Toast.LENGTH_LONG).show();
            double sanDiegoLatitudeDeg = 32.7152778;
            double sanDiegoLongitudeDeg = -117.1563889;
            double randomizeLat = sanDiegoLatitudeDeg + _locationRandomizer.nextDouble();
            double randomizeLon = sanDiegoLongitudeDeg + _locationRandomizer.nextDouble();
            _locationTextEntry.setText(getLocationStr(randomizeLat, randomizeLon));

            // save the location
            _photo.setLat(randomizeLat);
            _photo.setLon(randomizeLon);
        }
        else {
            _locationTextEntry.setText(getLocationStr(currLoc.getLatitude(), currLoc.getLongitude()));

            // save the location.  We add some randomization to vary the location so the photos
            // won't lay on top of each other on the map.
            _photo.setLat(currLoc.getLatitude() + _locationRandomizer.nextDouble());
            _photo.setLon(currLoc.getLongitude() + _locationRandomizer.nextDouble());
        }

        // display the date
        if(photo.getDate() != null) {
            //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            _dateTextView.setText(PhotoDatabaseHelper._dateFormatter.format(photo.getDate()));

        }

        // display the path.
        if(photo.getImagePath() != null) {
            _pathTextView.setText(photo.getImagePath());
        }
    }

    private static double generateRandomLatitude(double baseLatitude)
    {
        return 0;
    }

    private static String getLocationStr(double latitude, double longitude)
    {
        return degreeToLatLonStr(latitude, true) + "/" + degreeToLatLonStr(longitude, false);
    }
    /**
     * convert decimal degree to latitude or longitude string
     * @return the string representing the degree with direction.
     */
    private static String degreeToLatLonStr(double degree, boolean isLatitude)
    {
        StringBuffer resultDegreeString = new StringBuffer();

        // display up to 4 decimal precision
        resultDegreeString.append(_degreeFormatter.format(Math.abs(degree)));
        if(isLatitude) {
            resultDegreeString.append(degree >= 0 ? "N" : "S");
        }
        else {
            // it is longitude
            resultDegreeString.append(degree >= 0 ? "E" : "W");

        }

        return resultDegreeString.toString();
    }
}
