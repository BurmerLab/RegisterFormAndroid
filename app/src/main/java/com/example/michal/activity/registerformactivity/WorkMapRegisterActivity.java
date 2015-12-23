package com.example.michal.activity.registerformactivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.michal.activity.R;
import com.example.michal.geolocalization.GeolocalizationResources;
import com.example.michal.pojo.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class WorkMapRegisterActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private double latitudeLocalization;
    private double longitudeLocalization;

    private Geocoder geoCoder;
    protected Button registerWorkLocalizationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_work_map_form_registration);
        setUpMapIfNeeded();

        geoCoder = new Geocoder(this);

        //initialize
        registerWorkLocalizationButton = (Button) findViewById(R.id.buttonRegisterWorkLocalization);

        Intent intent = getIntent();
        User user = (User) intent.getParcelableExtra("user");
                        Toast.makeText(WorkMapRegisterActivity.this, "User: "
                                + user.getUserName() +
                                " email:" + user.getEmail()
                                , Toast.LENGTH_LONG).show();

        registerWorkLocalizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    List<Address> addresses =
                            geoCoder.getFromLocation(getLatitudeLocalization(), getLongitudeLocalization(), 1);

                    for (Address address : addresses) {
//                        Toast.makeText(WorkMapRegisterActivity.this, "Adres: "
//                                + address.getAddressLine(0)
//                                , Toast.LENGTH_LONG).show();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.workMapRegistration)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i("tag", "jestem w ifie Permission problem");

            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // redraw the marker when get location update.
//                drawMarker(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(GeolocalizationResources.MAP_ZOOM_ALTITUDE));

        final String markerTitleWhereIsYourWorkPlace = getResources().getString(R.string.marker_title_where_is_your_work_place);

        Marker defaultMarker = mMap.addMarker(new MarkerOptions()
//                .snippet("Lat: " + myLocation.getLatitude() + ", Lng: " + myLocation.getLongitude())
                .position(new LatLng(latitude, longitude))
                .title(markerTitleWhereIsYourWorkPlace)
                .flat(true)
                .draggable(true));
        defaultMarker.showInfoWindow();

        setLatitudeLocalization(myLocation.getLatitude());
        setLongitudeLocalization(myLocation.getLongitude());


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Marker insertedMarkerManually = mMap.addMarker(new MarkerOptions()
//                        .snippet("Lat: " + point.latitude + ", Lng: " + point.longitude)
                        .snippet("Please press Save button")
                        .position(point)
                        .title(markerTitleWhereIsYourWorkPlace)
                        .flat(true)
                        .draggable(true));

                insertedMarkerManually.showInfoWindow();

                setLatitudeLocalization(point.latitude);
                setLongitudeLocalization(point.longitude);
            }
        });
    }

    private void drawMarker(Location location){
        // Remove any existing markers on the map
        mMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("ME"));
        setLatitudeLocalization(location.getLatitude());
        setLongitudeLocalization(location.getLongitude());
    }

    public double getLatitudeLocalization() {
        return latitudeLocalization;
    }

    public void setLatitudeLocalization(double latitudeLocalization) {
        this.latitudeLocalization = latitudeLocalization;
    }

    public double getLongitudeLocalization() {
        return longitudeLocalization;
    }

    public void setLongitudeLocalization(double longitudeLocalization) {
        this.longitudeLocalization = longitudeLocalization;
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

//100% — FF
//    95% — F2
//    90% — E6
//    85% — D9
//    80% — CC
//    75% — BF
//    70% — B3
//    65% — A6
//    60% — 99
//            55% — 8C
//    50% — 80
//            45% — 73
//            40% — 66
//            35% — 59
//            30% — 4D
//            25% — 40
//            20% — 33
//            15% — 26
//            10% — 1A
//    5% — 0D
//            0% — 00

}
