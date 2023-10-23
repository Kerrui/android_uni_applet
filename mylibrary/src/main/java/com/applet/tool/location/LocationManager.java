package com.applet.tool.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.List;

import androidx.annotation.NonNull;

public class LocationManager {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationManager() {
    }

    private static class LocationManagerHolder {
        private static final LocationManager sInstance = new LocationManager();
    }

    public static LocationManager getInstance() {
        return LocationManagerHolder.sInstance;
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(Context context, int priority, OnLocationListener onLocationListener) {
        int curPriority = priority == -1 ? Priority.PRIORITY_LOW_POWER : priority;
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        mFusedLocationProviderClient.getCurrentLocation(curPriority, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Location otherLocation = getLngLat(context);
                    onLocationListener.onLocation(getLocationObj(context, otherLocation));
                } else {
                    onLocationListener.onLocation(getLocationObj(context, location));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onLocationListener.onLocation(new LocationObj());
            }
        });
    }

    @SuppressLint("MissingPermission")
    private Location getLngLat(Context context) {
        android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }
        return location;
    }

    private LocationObj getLocationObj(Context context, Location location) {
        if (location == null) {
            return new LocationObj();
        }
        LocationObj locationObj = LocationUtils.parseLocation(context, location);
        if (locationObj == null) {
            return new LocationObj(location.getLongitude(), location.getLatitude());
        }
        return locationObj;
    }
}
