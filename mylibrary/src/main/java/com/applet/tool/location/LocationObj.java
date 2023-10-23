package com.applet.tool.location;

import java.math.BigDecimal;

public class LocationObj {

    public String longitude;
    public String latitude;
    public String countryCode;
    public String country;
    public String prov;
    public String city;
    public String area;
    public String feature;
    public String address;
    public String addressTwo;

    public LocationObj() {
        longitude = "";
        latitude = "";
        countryCode = "";
        country = "";
        prov = "";
        city = "";
        area = "";
        feature = "";
        address = "";
        addressTwo = "";
    }

    public LocationObj(String countryCode) {
        longitude = "";
        latitude = "";
        this.countryCode = countryCode;
        country = "";
        prov = "";
        city = "";
        area = "";
        feature = "";
        address = "";
        addressTwo = "";
    }

    public LocationObj(double longitude, double latitude) {
        BigDecimal bdLongitude, bdLatitude;
        bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);

        this.longitude = bdLongitude + "";
        this.latitude = bdLatitude + "";
        countryCode = "";
        country = "";
        prov = "";
        city = "";
        area = "";
        feature = "";
        address = "";
        addressTwo = "";
    }

    @Override
    public String toString() {
        return "LocationObj{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", country='" + country + '\'' +
                ", prov='" + prov + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", feature='" + feature + '\'' +
                ", address='" + address + '\'' +
                ", addressTwo='" + addressTwo + '\'' +
                '}';
    }


}
