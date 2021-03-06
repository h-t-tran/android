package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.graphics.Bitmap;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);

        _titleTextView = (EditText)rootView.findViewById(R.id._picTitle);
        _descTextView = (EditText)rootView.findViewById(R.id._picDescEntry);
        _locationTextEntry = (EditText)rootView.findViewById(R.id._picLocationEntry);
        _imageView = (ImageView)rootView.findViewById(R.id._picImageView);
        _dateTextView = (TextView)rootView.findViewById(R.id._picDateEntry);
        _pathTextView = (TextView)rootView.findViewById(R.id._picPathEntry);

        return rootView;
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
    public void setExistingPhoto(final Photo photo)
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

        // add click listener to the image viewer so we can display the photo
        _imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // launch the photo viewer activity to display only the photo.
                Intent intent = new Intent(getActivity(), PhotoViewerActivity.class);
                intent.putExtra(PhotoViewerActivity.PHOTO_ID_KEY, photo.getId());
                getActivity().startActivity(intent);
            }
        });
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

        // since a degree is about 72 miles and we want to randomize it around our current location
        // and reduce the randomize factor by .01 which is about 0.72 miles.  This will prevent
        // our pics from overlapping with each other, but keep them group closely together
        final double reduceRandomizeFactor = 0.01;
        double randLat = _locationRandomizer.nextDouble() * reduceRandomizeFactor;
        double randLon = _locationRandomizer.nextDouble() * reduceRandomizeFactor;

        // if there is no location, we'll generate a random location around san diego
        if(currLoc == null){
            Toast.makeText(this.getActivity(),
                    "GPS maybe disabled.  Using default location", Toast.LENGTH_LONG).show();

            double randomizeLat = Constants.sanDiegoLatitudeDeg + randLat;
            double randomizeLon = Constants.sanDiegoLongitudeDeg + randLon;
            _locationTextEntry.setText(getLocationStr(randomizeLat, randomizeLon));

            // save the location
            _photo.setLat(randomizeLat);
            _photo.setLon(randomizeLon);
        }
        else {

            // save the location.  We add some randomization to vary the location so the photos
            // won't lay on top of each other on the map.
            _photo.setLat(currLoc.getLatitude() + randLat);
            _photo.setLon(currLoc.getLongitude() + randLon);

            _locationTextEntry.setText(getLocationStr(_photo.getLat(), _photo.getLon()));
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

    /**
     *
     * @return the most recent photo we take.
     */
    public Photo getPhoto() {
        return _photo;
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
