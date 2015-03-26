package com.sdsu.hoanh.geoalbum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

public class PicDetailFragment extends Fragment {

    private Photo photo;

    private TextView titleTextView;
    private TextView descTextView;
    private TextView latTextView;
    private TextView lonTextView;
    private TextView dateTextView;

    public PicDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);

        titleTextView = (TextView)rootView.findViewById(R.id._picTitle);
        descTextView = (TextView)rootView.findViewById(R.id._picDescEntry);
        latTextView = (TextView)rootView.findViewById(R.id._picLatEntry);
        lonTextView = (TextView)rootView.findViewById(R.id._picLonEntry);
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

        photo.setTitle(titleTextView.getText().toString());
        photo.setLat(Double.valueOf(latTextView.getText().toString()));
        photo.setLon(Double.valueOf(lonTextView.getText().toString()));
        photo.setDesc(descTextView.getText().toString());

        PhotoModel.getInstance().savePhoto(photo);
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;

        // display data into

        ImageView imageView = (ImageView)this.getActivity().findViewById(R.id._picImageView);
        imageView.setImageBitmap(photo.getImage());

    }
}
