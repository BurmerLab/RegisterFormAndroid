package com.mytway.geolocalization;

import android.Manifest;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.TimeToDeparture;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;
import com.mytway.utility.permission.PermissionUtil;
import com.mytway.widget.MyWidgetProvider;
import com.mytway.widget.WidgetUtils;

import org.joda.time.LocalDateTime;

import java.lang.annotation.Target;
import java.util.Calendar;
import java.util.Date;

public class MytwayGeolocalizationService extends Service implements LocationListener {

    private Context mContext;
    private Session session;
    private static final String TAG = "MytwayGeolocalizationService";
    private static final int RED_COLOR =  -65536;

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

    public MytwayGeolocalizationService(Context context) {
        this.mContext = context;
        getLocalization();
    }

    public MytwayGeolocalizationService() {
    }

    public android.location.Location getLocalization() {

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

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("DUPKA")){
                //action for sms received
                WidgetUtils.location = getLocalization();
            }
            else if(action.equals(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                //action for phone state changed
            }
        }
    };

    @Override
    public void onCreate(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("DUPKA");
        filter.addAction("your_action_strings"); //further more
        filter.addAction("your_action_strings"); //further more

        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        updateGeolocalization();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateGeolocalization() {
        mContext = getApplicationContext();
        session = new Session(mContext);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.mytway5_table_middle_widget_layout);

        if(session.isUserLogged()){
            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, MyWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);

            if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, mContext)
                    && PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, mContext)) {

                Location currentLocation = getLocalization();
                Position currentPosition = new Position(currentLocation.getLatitude(), currentLocation.getLongitude());

                //Direction, is user is going to work or home??
                DirectionWay directionWay = new DirectionWay(Boolean.TRUE, Boolean.FALSE);

                //Travel time
                TravelTime travelTime = new TravelTime();
                travelTime.setDirectionWay(directionWay);
                travelTime.obtainTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);

                //1st Time- time to departure
                TimeToDeparture timeToDeparture = new TimeToDeparture();
                timeToDeparture.setSession(session);
                timeToDeparture.setTravelTime(travelTime);
                timeToDeparture.processTime(mContext, currentPosition, session);

                //2th Time - Time in road
                LocalDateTime timeInRoad = timeToDeparture.getTravelTime().getGoogleMapsDirectionJson().getLegs().getDuration().getDurationTime();
                String timeInRoadString = timeInRoad.toString();

                //3rd Time Arrive Time (When We will come back)
                //ArriveTime = CurrentTime + TravelTime (toWork) + workLength + travelTime (back)
                String arriveTime =
                view.setImageViewResource(R.id.refreshImage, R.drawable.ic_sync_button);

                String messageToDisplay = "" + timeToDeparture.getDisplayTimeMessage();

                view.setTextViewText(R.id.firstTimeTextView, messageToDisplay);

            }else{
                view.setImageViewResource(R.id.refreshImage, R.drawable.ic_error);
                MyWidgetProvider.openNewActivity(mContext, manager, manager.getAppWidgetIds(thisWidget), view, R.id.refreshImage, new String[0]);
            }

            manager.updateAppWidget(thisWidget, view);
        }else{
            Log.i(TAG, "User is not logged");
        }


    }

    public void onDestroy(){
        unregisterReceiver(receiver);
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

    public void myTwayMessage(){
        Intent intent = new Intent(WidgetUtils.WIDGET_UPDATE_ACTION);
        intent.putExtra("newItemArrived", "Neue Frage erschienen");
        sendBroadcast(intent);
    }

}
