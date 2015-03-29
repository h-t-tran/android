package com.sdsu.hoanh.geoalbum;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsu.hoanh.geoalbum.Model.GpsProvider;
import com.sdsu.hoanh.geoalbum.Model.GpsReceiver;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

public class PicDetailFragment extends Fragment {

    private Photo _photo;

    private TextView _titleTextView;
    private TextView _descTextView;
    private TextView _latTextView;
    private TextView _lonTextView;
    private TextView _dateTextView;

    public PicDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);

        _titleTextView = (TextView)rootView.findViewById(R.id._picTitle);
        _descTextView = (TextView)rootView.findViewById(R.id._picDescEntry);
        _latTextView = (TextView)rootView.findViewById(R.id._picLatEntry);
        _lonTextView = (TextView)rootView.findViewById(R.id._picLonEntry);
        //lonTextView = (TextView)rootView.findViewById(R.id._picLonEntry);

//        Button acceptButton = (Button)rootView.findViewById(R.id._acceptPhoto);
//        acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PicDetailFragment.this.savePhoto();
//            }
//        });
        return rootView;
    }

    private void savePhoto()
    {
        // save the photo in database.
        Photo photo = new Photo();

        photo.setTitle(_titleTextView.getText().toString());
        photo.setLat(Double.valueOf(_latTextView.getText().toString()));
        photo.setLon(Double.valueOf(_lonTextView.getText().toString()));
        photo.setDesc(_descTextView.getText().toString());

        PhotoModel.getInstance().savePhoto(photo);
    }

    public Photo getPhoto() {
        return _photo;
    }

    public void setPhoto(Photo photo) {
        this._photo = photo;

        // display data into

        ImageView imageView = (ImageView)this.getActivity().findViewById(R.id._picImageView);
        imageView.setImageBitmap(photo.getImage());

        // get the lat/lon and display it.
        Location currLoc = GpsProvider.getInstance().getCurrLocation();
        if(currLoc == null){
            Toast.makeText(this.getActivity(),
                    "No location.  GPS maybe disabled", Toast.LENGTH_LONG).show();
            _latTextView.setText("0.0");
            _lonTextView.setText("0.0");
        }
        else {
            _latTextView.setText(degreeToLatLonStr(currLoc.getLatitude(), true));
            _lonTextView.setText(degreeToLatLonStr(currLoc.getLongitude(), false));
        }
    }

    /**
     * convert decimal degree to latitude or longitude string
     * @return the string representing the degree with direction.
     */
    private static String degreeToLatLonStr(double degree, boolean isLatitude)
    {
        StringBuffer resultDegreeString = new StringBuffer();
        resultDegreeString.append(Double.toString(Math.abs(degree)));
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
