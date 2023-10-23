package com.applet.tool.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static LocationObj parseLocation(Context context, Location location) {
        return parseLocation(context, location.getLongitude(), location.getLatitude());
    }

    public static LocationObj parseLocation(Context context, double lng, double lat) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> locationList = null;
        try {
            locationList = geocoder.getFromLocation(lat, lng, 1);
            if (locationList != null && locationList.size() > 0) {
                Address address = locationList.get(0);

                String countryName = address.getCountryName();
                String countryCode = address.getCountryCode();
                String adminArea = address.getAdminArea();
                String locality = address.getLocality();
                String subAdminArea = address.getSubLocality();
                String featureName = address.getThoroughfare();
                String addressLine = null;
                String addressLineTwo = null;

                if (address.getMaxAddressLineIndex() >= 1) {
                    addressLine = address.getAddressLine(0);
                    addressLineTwo = address.getAddressLine(1);
                } else {
                    addressLine = address.getAddressLine(0);
                }
                LocationObj locationObj = new LocationObj(lng, lat);
                if (!TextUtils.isEmpty(countryCode)) locationObj.countryCode = countryCode;
                if (!TextUtils.isEmpty(countryName)) locationObj.country = countryName;
                if (!TextUtils.isEmpty(adminArea)) locationObj.prov = adminArea;
                if (!TextUtils.isEmpty(locality)) locationObj.city = locality;
                if (!TextUtils.isEmpty(subAdminArea)) locationObj.area = subAdminArea;
                if (!TextUtils.isEmpty(featureName)) locationObj.feature = featureName;
                if (!TextUtils.isEmpty(addressLine)) locationObj.address = addressLine;
                if (!TextUtils.isEmpty(addressLineTwo)) locationObj.addressTwo = addressLineTwo;

                return locationObj;
            }
        } catch (IOException ioException) {
            return new LocationObj("parse_error");
        }
        return new LocationObj("parse_none");
    }
}
