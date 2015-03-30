package com.sdsu.hoanh.geoalbum.Model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

/**
 * this model class provide a simple api to retrieve current GPS location
 */
public class GpsProvider {

    private static GpsProvider _instance;
    public static final String ACTION_LOCATION = "com.sdsu.hoanh.geoalbum.ACTION_LOCATION";
    private Context _owningActivityCtx;
    private LocationManager _locationManager;
    private Location _currLocation = null;

    public static GpsProvider getInstance()
    {
        if(_instance == null)
        {
            _instance = new GpsProvider();
        }

        return _instance;
    }

    public void start(Context owningActivityCtx)
    {
        _owningActivityCtx = owningActivityCtx;
        _locationManager = (LocationManager)owningActivityCtx
                                                .getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;

        PendingIntent locationIntent = getLocationPendingIntent(true);
        _locationManager.requestLocationUpdates(provider, 1000, 0, locationIntent);
    }

    public void stop()
    {
        PendingIntent pendingLocationIntent = getLocationPendingIntent(false);
        if (pendingLocationIntent != null) {
            _locationManager.removeUpdates(pendingLocationIntent);
            pendingLocationIntent.cancel();
        }
    }
    public Location getCurrLocation() {
        return _currLocation;
    }
    public void setCurrLocation(Location currLocation)
    {
        _currLocation = currLocation;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(_owningActivityCtx, 0, broadcast, flags);
    }

}
