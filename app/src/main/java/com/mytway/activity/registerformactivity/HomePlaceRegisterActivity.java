package com.mytway.activity.registerformactivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.mytway.activity.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mytway.activity.application.MytwayActivity;
import com.mytway.database.DBHelper;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.geolocalization.MytwayGeolocalizationService;
import com.mytway.pojo.Distance;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Position;
import com.mytway.pojo.User;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.EthernetConnectivity;
import com.mytway.utility.ScheduledProcess;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;
import com.mytway.utility.permission.PermissionUtil;
import com.mytway.utility.webservice.WebServiceUtility;
import com.mytway.validation.Validation;

public class HomePlaceRegisterActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    private static final String TAG = "MytwayWebservice";

    private int userId = 0;
    private GoogleMap mMap;

    private String jsonUserDataMessage = "";

    private double latitudeLocalization;
    private double longitudeLocalization;
    private SharedPreferences sharedPreferences;
    protected Button registerHomeLocalizationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_place_form_registration);

        DBHelper databaseHelper = new DBHelper(getApplicationContext());

        if(!EthernetConnectivity.isEthernetOnline(HomePlaceRegisterActivity.this)){
            Toast.makeText(HomePlaceRegisterActivity.this,
                    getResources().getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.homeMapRegistration);
        mapFragment.getMapAsync(this);

        //initialize
        registerHomeLocalizationButton = (Button) findViewById(R.id.buttonRegisterHomeLocalization);

        Intent intent = getIntent();
        final User user = intent.getParcelableExtra("user");
        Intent intentFromRegistrationActivity = getIntent();
        final String processingAccount = intentFromRegistrationActivity.getStringExtra(PropertiesValues.PROCESSING_ACCOUNT);

        registerHomeLocalizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePlaceRegisterActivity.this.getApplicationContext(), "Latitude: " + latitudeLocalization +" Longitude: " + longitudeLocalization, Toast.LENGTH_LONG).show();
                Position workPosition = user.getWorkPlace();
                Position homePosition = new Position(longitudeLocalization, latitudeLocalization);

                int userIdAddedInExternalDB = 0;

                if (Validation.homePositionIsNotTheSameWorkPosition(homePosition, workPosition, registerHomeLocalizationButton, getString(R.string.home_place_equals_work_place))) {
                    user.setHomePlace(homePosition);

                    TravelTime travelTime = new TravelTime();
                    GoogleMapsDirectionJson gMapsDirection =
                            travelTime.getTravelTimeBetweenTwoPositions(getApplicationContext(), homePosition, workPosition);
                    int travelToWorkDuration;
                    Double travelToWorkDistance;

                    if(gMapsDirection != null){
                        travelToWorkDuration = gMapsDirection.getLegs().getDuration().getValue();
                        travelToWorkDistance = Distance.designateDistanceBetween(homePosition, workPosition);
                    }else{
                        travelToWorkDuration = 0;
                        travelToWorkDistance = Distance.designateDistanceBetween(homePosition, workPosition);
                    }

                    UserRepo userRepo = new UserRepo(HomePlaceRegisterActivity.this);
                    UserTable userTable = new UserTable();

                    userTable.userId = userId;
                    userTable.userName = user.getUserName();
                    userTable.password = user.getPassword();
                    userTable.email = user.getEmail();
                    userTable.typeWork = user.getTypeWork().getStatusCode();
                    userTable.lengthTimeWork = user.getLengthTimeWork();
                    userTable.startStandardTimeWork = user.getStartStandardTimeWork();
                    userTable.workPlaceLatitude = user.getWorkPlace().getLatitude();
                    userTable.workPlaceLongitude = user.getWorkPlace().getLongitude();
                    userTable.homePlaceLatitude = user.getHomePlace().getLatitude();
                    userTable.homePlaceLongitude = user.getHomePlace().getLongitude();
                    userTable.workWeek = user.decodeWorkWeekToString(user.getWorkWeek());
                    userTable.wayDistance = travelToWorkDistance;
                    userTable.wayDuration = travelToWorkDuration;

                    Toast.makeText(HomePlaceRegisterActivity.this, "Added distance: " + travelToWorkDistance + ", duration: " + travelToWorkDuration, Toast.LENGTH_SHORT).show();

                    Session session = new Session(getApplicationContext());
                    session.setIsUserLogged(true);
                    session.setUserName(user.getUserName());
                    session.setPassword(user.getPassword());
                    session.setEmail(user.getEmail());
                    session.setLengthTimeWork(user.getLengthTimeWork());
                    session.setStartStandardTimeWork(user.getStartStandardTimeWork());
                    session.setWorkWeek(user.decodeWorkWeekToString(user.getWorkWeek()));
                    session.setHomeLatitude("" + user.getHomePlace().getLatitude().floatValue());
                    session.setHomeLongitude("" + user.getHomePlace().getLongitude().floatValue());
                    session.setWorkLatitude("" + user.getWorkPlace().getLatitude().floatValue());
                    session.setWorkLongitude("" + user.getWorkPlace().getLongitude().floatValue());
                    session.setWayDistance(travelToWorkDistance.toString());
                    session.setWayDuration("" + travelToWorkDuration);

                    Toast.makeText(HomePlaceRegisterActivity.this, "Shared WorkLat: " + user.getWorkPlace().getLatitude().floatValue(), Toast.LENGTH_SHORT).show();

                    setJsonUserDataMessage(userTable.createJson());

                    //UPDATE ACCOUNT
                    if(processingAccount.equals(PropertiesValues.UPDATE_USER)){
                        //update local user account
                        if(userRepo.isUserExistInLocalDatabase(user.getUserName())){
                            UserTable userFromDatabase = userRepo.getUserByUserName(user.getUserName());
                            if(userFromDatabase.password.equals(user.getPassword())){
                                userRepo.updateByUserName(userTable);
                            }else{
                                Toast.makeText(HomePlaceRegisterActivity.this, R.string.password_is_not_correct, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(HomePlaceRegisterActivity.this, R.string.inserted_user_not_exist, Toast.LENGTH_SHORT).show();
                        }

                        updateUserAccountInExternalDatabase(userTable);

                    }else{
                        //INSERT USER ACCOUNT
                        if (userId == 0) {

                            userIdAddedInExternalDB = insertUserAccountToExternalDatabase(userTable);

                            if(!userRepo.isUserExistInLocalDatabase(userTable.userName)){
                                userTable.userId = userIdAddedInExternalDB;
                                userId = userRepo.insert(userTable);
                            }
                        } else {
                            userRepo.update(userTable);
                        }
                    }

                    Intent intent = new Intent(HomePlaceRegisterActivity.this, MytwayActivity.class);
                    intent.putExtra("user", user);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void updateUserAccountInExternalDatabase(UserTable userTable) {
        if(WebServiceUtility.updateUserAccountInExternalDatabase(getApplicationContext(), userTable, getJsonUserDataMessage())){
            Log.i(TAG, "Correctly updated user account to external database");
        }else{
            sharedPreferences.edit().putString(ScheduledProcess.UPDATE_ACCOUNT_USER_IN_EXTERNAL_DB, ScheduledProcess.UPDATE_ACCOUNT_USER_IN_EXTERNAL_DB).commit();
        }
    }

    public int insertUserAccountToExternalDatabase(UserTable userTable) {
        int userIdAddedInExternalDB = WebServiceUtility.insertUserAccountToExternalDatabase(getApplicationContext(), userTable, getJsonUserDataMessage());
        if(userIdAddedInExternalDB > 0){
            Log.i(TAG, "Correctly inserted user account to external database");
        }else{
            sharedPreferences.edit().putString(ScheduledProcess.INSERT_ACCOUNT_USER_TO_EXTERNAL_DB, ScheduledProcess.INSERT_ACCOUNT_USER_TO_EXTERNAL_DB).commit();
        }

        return userIdAddedInExternalDB;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext())
                && PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext())) {
            fetchLocationData();
        }else{
            PermissionUtil.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                                PERMISSION_REQUEST_CODE_LOCATION,
                                getApplicationContext(),
                                HomePlaceRegisterActivity.this,
                                getString(R.string.localization_will_help_with_choose_your_home_place));
        }
    }

    private void fetchLocationData() {
        setUpMap();
    }

    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        Intent intentFromRegistrationActivity = getIntent();
        final String processingAccount = intentFromRegistrationActivity.getStringExtra(PropertiesValues.PROCESSING_ACCOUNT);
        final Session session = new Session(getApplicationContext());

        if(processingAccount.equals(PropertiesValues.UPDATE_USER)){
            latitudeLocalization = Double.parseDouble(session.getHomeLatitude());
            longitudeLocalization = Double.parseDouble(session.getHomeLongitude());
        }else{
            MytwayGeolocalizationService geolocalization = new MytwayGeolocalizationService(HomePlaceRegisterActivity.this);
            geolocalization.getLocalization();
            latitudeLocalization = geolocalization.getLatitude();
            longitudeLocalization = geolocalization.getLongitude();
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Create a LatLng object for the current location
        final LatLng latLng = new LatLng(latitudeLocalization, longitudeLocalization);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, PropertiesValues.WORK_AND_HOME_PLACE_MAP_ZOOM_LEVEL));
            }
        });

        final String markerTitleWhereIsYourHomePlace = getResources().getString(R.string.marker_title_where_is_your_home_place);

        Marker defaultMarker = mMap.addMarker(new MarkerOptions()
//                .snippet("Lat: " + myLocation.getLatitude() + ", Lng: " + myLocation.getLongitude())
                .position(new LatLng(latitudeLocalization, longitudeLocalization))
                .title(markerTitleWhereIsYourHomePlace)
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
                        .title(markerTitleWhereIsYourHomePlace)
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

    public void setLatitudeLocalization(double latitudeLocalization) {
        this.latitudeLocalization = latitudeLocalization;
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

    public String getJsonUserDataMessage() {
        return jsonUserDataMessage;
    }

    public void setJsonUserDataMessage(String jsonUserDataMessage) {
        this.jsonUserDataMessage = jsonUserDataMessage;
    }
}
