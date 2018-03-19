package com.mytway.activity.registerformactivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.mytway.activity.R;
import com.mytway.geolocalization.MytwayGeolocalizationService;
import com.mytway.pojo.Position;
import com.mytway.pojo.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.EthernetConnectivity;
import com.mytway.utility.Session;
import com.mytway.utility.permission.PermissionUtil;

public class WorkPlaceRegisterActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    private GoogleMap mMap;

    private double latitudeLocalization;
    private double longitudeLocalization;

    protected Button registerWorkLocalizationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_work_map_form_registration);

        if(!EthernetConnectivity.isEthernetOnline(WorkPlaceRegisterActivity.this)){
            Toast.makeText(WorkPlaceRegisterActivity.this,
                    getResources().getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.workMapRegistration);
        mapFragment.getMapAsync(this);

        //initialize
        registerWorkLocalizationButton = (Button) findViewById(R.id.buttonRegisterWorkLocalization);

        Intent intent = getIntent();
        final User user = intent.getParcelableExtra("user");
        final String processingAccount = intent.getStringExtra(PropertiesValues.PROCESSING_ACCOUNT);

        registerWorkLocalizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(WorkPlaceRegisterActivity.this.getApplicationContext(), "Latitude: " + latitudeLocalization +" Longitude: " + longitudeLocalization, Toast.LENGTH_LONG).show();

                Position workPosition = new Position(longitudeLocalization, latitudeLocalization);
                user.setWorkPlace(workPosition);

                Intent intent = new Intent(WorkPlaceRegisterActivity.this, HomePlaceRegisterActivity.class);
                intent.putExtra("user", user);
                intent.putExtra(PropertiesValues.PROCESSING_ACCOUNT, processingAccount);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext())
                && PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext())) {
            fetchLocationData();
        }else{
            PermissionUtil.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                                            PERMISSION_REQUEST_CODE_LOCATION,
                                            getApplicationContext(),
                                            WorkPlaceRegisterActivity.this,
                                            getString(R.string.localization_will_help_with_choose_your_work_place));
        }
    }

    private void fetchLocationData() {
//        Toast.makeText(WorkPlaceRegisterActivity.this.getApplicationContext(), "FETCH DATA",Toast.LENGTH_LONG).show();
        setUpMap();
    }

    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        Intent intentFromRegistrationActivity = getIntent();
        final String processingAccount = intentFromRegistrationActivity.getStringExtra(PropertiesValues.PROCESSING_ACCOUNT);
        final Session session = new Session(getApplicationContext());


        if(processingAccount.equals(PropertiesValues.UPDATE_USER)){
            latitudeLocalization = Double.parseDouble(session.getWorkLatitude());
            longitudeLocalization = Double.parseDouble(session.getWorkLongitude());
        }else{
            MytwayGeolocalizationService geolocalization = new MytwayGeolocalizationService(WorkPlaceRegisterActivity.this);
            geolocalization.getLocalization();
            latitudeLocalization = geolocalization.getLatitude();
            longitudeLocalization = geolocalization.getLongitude();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        final LatLng latLng = new LatLng(latitudeLocalization, longitudeLocalization);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, PropertiesValues.WORK_AND_HOME_PLACE_MAP_ZOOM_LEVEL));
            }
        });

        final String markerTitleWhereIsYourWorkPlace = getResources().getString(R.string.tap_on_map_to_indicate_work_place);

        Marker defaultMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitudeLocalization, longitudeLocalization))
                .title(markerTitleWhereIsYourWorkPlace)
                .flat(true)
                .draggable(true));
        defaultMarker.showInfoWindow();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Marker insertedMarkerManually = mMap.addMarker(new MarkerOptions()
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
                .title("ME")
                .flat(true)
                .draggable(true));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocationData();
                } else {
                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}