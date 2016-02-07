package com.mytway.activity.registerformactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.mytway.activity.R;
import com.mytway.pojo.Position;
import com.mytway.pojo.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytway.properties.Properties;

public class WorkPlaceRegisterActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    private GoogleMap mMap;

    private double latitudeLocalization;
    private double longitudeLocalization;

    protected Button registerWorkLocalizationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_work_map_form_registration);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.workMapRegistration);
        mapFragment.getMapAsync(this);

        //initialize
        registerWorkLocalizationButton = (Button) findViewById(R.id.buttonRegisterWorkLocalization);

        Intent intent = getIntent();

        final User user = intent.getParcelableExtra("user");
//        Toast.makeText(WorkPlaceRegisterActivity.this, "User: "
//                + user.getUserName() +
//                " email:" + user.getEmail() +
//                " start:" + user.getStartStandardTimeWork()
//                , Toast.LENGTH_LONG).show();

        registerWorkLocalizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(WorkPlaceRegisterActivity.this.getApplicationContext(), "Latitude: " + latitudeLocalization +" Longitude: " + longitudeLocalization, Toast.LENGTH_LONG).show();

                Position workPosition = new Position(longitudeLocalization, latitudeLocalization);
                user.setWorkPlace(workPosition);

                Intent intent = new Intent(WorkPlaceRegisterActivity.this, HomePlaceRegisterActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext())
                && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext())) {
            fetchLocationData();
        }else{
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
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

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location myLocation = locationManager.getLastKnownLocation(provider);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // redraw the marker when get location update.
                drawMarker(location);
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
        final LatLng latLng = new LatLng(latitude, longitude);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Properties.WORK_PLACE_MAP_ZOOM_LEVEL));
            }
        });
        // Show the current location in Google Map
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(GeolocalizationResources.MAP_ZOOM_ALTITUDE));

        final String markerTitleWhereIsYourWorkPlace = getResources().getString(R.string.tap_on_map_to_indicate_work_place);

        Marker defaultMarker = mMap.addMarker(new MarkerOptions()
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

    public static void requestPermission(String strPermission, int perCode,Context context ,Activity activity, String requestMessage){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, strPermission)){
            Toast.makeText(context.getApplicationContext(), requestMessage, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(activity,new String[]{strPermission},perCode);
        }
    }

    public static boolean checkPermission(String strPermission ,Context _c){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);

        if (result == PackageManager.PERMISSION_GRANTED){
            return true;

        } else {
            return false;
        }
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
