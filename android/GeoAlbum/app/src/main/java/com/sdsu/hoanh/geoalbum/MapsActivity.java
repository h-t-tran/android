package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoModel;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap _map; // Might be null if Google Play services APK is not available.

    /**
     * the mapping between marker and photo associated with each marker
     */
    private HashMap<Marker, Photo> _markerToPhotoDictionary = new HashMap<Marker, Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        displayPhotos();
    }

    private void displayPhotos()
    {
        List<Photo> photos = PhotoModel.getInstance().getPhotos();
        for(Photo photo : photos)
        {
            LatLng picLocation = new LatLng(photo.getLat(), photo.getLon());
            Marker marker = _map.addMarker(new MarkerOptions()
                    .position(picLocation)
                    .title(photo.getTitle()));
            _markerToPhotoDictionary.put(marker, photo);

            // display the title of the marker
            marker.showInfoWindow();
        }

        _map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()   {
            public boolean onMarkerClick(Marker marker) {
                MapsActivity.this.onMakerSelected(marker);
                return true;
            }
        });
    }

    private void onMakerSelected(Marker selMarker)
    {
        Photo photo = _markerToPhotoDictionary.get(selMarker);
        if(photo != null) {
            // display the detail marker
            Intent intent = new Intent(this, PictureDetailActivity.class);
            intent.putExtra(PictureDetailActivity.PHOTO_ID_KEY, photo.getId());
            this.startActivity(intent);
        }
        else {
            Toast.makeText(this, "Unable to find associated photo.", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #_map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (_map == null) {
            // Try to obtain the map from the SupportMapFragment.
            _map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (_map != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        LatLng sanDiego = new LatLng(Constants.sanDiegoLatitudeDeg, Constants.sanDiegoLongitudeDeg);


        _map.addMarker(new MarkerOptions()
                .position(sanDiego)
                .title("Marker"));
        // zoom in to san diego
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(sanDiego, 8));
        _map.setMyLocationEnabled(true);
        _map.setBuildingsEnabled(true);
        _map.setIndoorEnabled(true);
        _map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }
}
