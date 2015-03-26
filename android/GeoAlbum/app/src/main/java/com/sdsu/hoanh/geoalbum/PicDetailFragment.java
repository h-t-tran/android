package com.sdsu.hoanh.geoalbum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sdsu.hoanh.geoalbum.Model.Photo;

public class PicDetailFragment extends Fragment {

    private Photo photo;

    
    public PicDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);

        Button acceptButton = (Button)rootView.findViewById(R.id._acceptPhoto);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicDetailFragment.this.savePhoto();
            }
        });
        return rootView;
    }

    private void savePhoto()
    {
        // save the photo in database.
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
