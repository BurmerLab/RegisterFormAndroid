package com.mytway.geolocalization;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class MytwayGeolocalization extends Service implements LocationListener {
    private final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    android.location.Location location;
    private double latitude;
    private double longitude;

    // The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public MytwayGeolocalization(Context context) {
        this.mContext = context;
        getLocation();
    }

    public android.location.Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateGPSCoordinates(location);
                    }
                }

                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            updateGPSCoordinates(location);
                        }
                    }
                }
            }
        } catch (Exception e) {
             e.printStackTrace();
            Log.e("Error : Location", "Impossible to connect to LocationManager", e);
        }

        return location;
    }

    public void updateGPSCoordinates(Location location) {
        if (location != null) {
            setLatitude(location.getLatitude());
            setLongitude(location.getLongitude());
        }
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void onLocationChanged(Location location) {
//        Toast.makeText(mContext, "Localization Changed : lat:" + getLatitude() +" lon: "+ getLongitude(), Toast.LENGTH_SHORT).show();
        updateGPSCoordinates(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
