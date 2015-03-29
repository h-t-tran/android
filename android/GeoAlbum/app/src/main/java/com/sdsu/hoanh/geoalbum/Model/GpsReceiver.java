package com.sdsu.hoanh.geoalbum.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by hoanh on 3/28/2015.
 */
public class GpsReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Location loc = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (loc != null) {

            // inform the gps provider of current location
            GpsProvider.getInstance().setCurrLocation(loc);
        }
        // if we get here, something else has happened
//        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
//            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
//            onProviderEnabledChanged(enabled);
//        }
    }

}
