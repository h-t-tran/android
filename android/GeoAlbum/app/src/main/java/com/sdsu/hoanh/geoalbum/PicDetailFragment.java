package com.sdsu.hoanh.geoalbum;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsu.hoanh.geoalbum.Model.GpsProvider;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

public class PicDetailFragment extends Fragment {

    private Photo _photo;
    private static final Random _locationRandomizer = new Random();

    // the formatter for degree to 4 decimal places
    private static final NumberFormat _degreeFormatter = new DecimalFormat("#0.0000");

    private EditText _titleTextView;
    private EditText _descTextView;
    private EditText _locationTextEntry;
    //private EditText _lonTextView;
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
        //_lonTextView = (EditText)rootView.findViewById(R.id._picLonEntry);
        _dateTextView = (TextView)rootView.findViewById(R.id._picDateEntry);
        _pathTextView = (TextView)rootView.findViewById(R.id._picPathEntry);

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

            PhotoModel.getInstance().savePhoto(_photo);
            isSaved = true;
        }

        return isSaved;
    }

   // public Photo getPhoto() {
   //     return _photo;
   // }

    public void setPhoto(Photo photo) {
        this._photo = photo;

        // display data into

        ImageView imageView = (ImageView)this.getActivity().findViewById(R.id._picImageView);
        imageView.setImageBitmap(photo.getImage());

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

            // save the location
            _photo.setLat(currLoc.getLatitude());
            _photo.setLon(currLoc.getLongitude());
        }

        // display the date
        if(photo.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            _dateTextView.setText(sdf.format(photo.getDate()));

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
