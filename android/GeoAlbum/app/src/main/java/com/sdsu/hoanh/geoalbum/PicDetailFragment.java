package com.sdsu.hoanh.geoalbum;

import android.app.Activity;
import android.content.Intent;
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
        View rootView = inflater.inflate(R.layout.fragment_take_picture, container, false);

//        Button acceptButton = (Button)this.getActivity().findViewById(R.id._acceptPic);
//        acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PicDetailFragment.this.returnResultToCallingActivity();
//            }
//        });
        return rootView;
    }

    private void returnResultToCallingActivity()
    {
        //Intent data = new Intent();
        this.getActivity().setResult(Activity.RESULT_OK, null);
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
