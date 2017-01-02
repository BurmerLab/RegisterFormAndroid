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
import android.util.Log;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.screens.HomeScreen;
import com.mytway.behaviour.pojo.screens.MorningScreen;
import com.mytway.behaviour.pojo.screens.TravelToHomeScreen;
import com.mytway.behaviour.pojo.screens.TravelToWorkScreen;
import com.mytway.behaviour.pojo.screens.WorkScreen;
import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.Session;
import com.mytway.utility.permission.PermissionUtil;
import com.mytway.widget.MyWidgetProvider;
import com.mytway.widget.WidgetUtils;

import org.joda.time.LocalDateTime;

public class MytwayGeolocalizationService extends Service implements LocationListener {

    private Context mContext;
    private Session session;
    private static final String TAG = "MytwayGeolocalizationService";
    private static final int RED_COLOR =  -65536;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    public DirectionWay directionWay = new DirectionWay();

    public android.location.Location location;
    private double latitude;
    private double longitude;
    private AppWidgetManager manager;
    private ComponentName thisWidget;
    private RemoteViews view;

    // The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private MorningScreen morningScreen = new MorningScreen();

    public MytwayGeolocalizationService(Context context) {
        this.mContext = context;
//        getLocalization();
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
        try {
            updateGeolocalization();
        } catch (Exception e) {
            Log.i(TAG, "Problem with method updateGeolocalization in service", e);
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void updateGeolocalization() throws Exception {
        mContext = getApplicationContext();

        if(!PropertiesValues.MOCK_APP_TO_TESTS){
            //remove in unit tests:
            session = new Session(mContext);
        }

        view = new RemoteViews(getPackageName(), R.layout.mytway5_table_middle_widget_layout);

        if(session.isUserLogged()){
//            thisWidget = new ComponentName(this, MyWidgetProvider.class);
            thisWidget = new ComponentName(this, MyWidgetProvider.class);
            manager = AppWidgetManager.getInstance(this);

            if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, mContext)
                    && PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, mContext)) {

                getLocalization();

                Position currentPosition = new Position(location.getLatitude(), location.getLongitude());

                //Direction, is user is going to work or home??
                Distance distanceBetweenHomeAndWork = new Distance("", Double.parseDouble(session.getWayDistance()));
                directionWay.setDistanceBetweenHomeAndWork(distanceBetweenHomeAndWork);
                directionWay.decideWhichDirectionIs(currentPosition, session);

                directionWay.decideIsInHome(currentPosition, session.getHomePlace());
                directionWay.decideIsInWork(currentPosition, session.getWorkPlace());

                LocalDateTime whenUserLeaveHome = directionWay.getLeaveHomeToGoToWorkTime();//directionWay.getLeaveHomeToGoToWorkTime()
                LocalDateTime startWorkTime = directionWay.getStartWorkTime(); //directionWay.getStartWorkTIme

                if(directionWay.isInHome()){
                    //Morning screen
                    morningScreen.prepareScreen(view, directionWay, session, mContext, currentPosition);

                } else if(directionWay.isInWayToWork()){
                    //TravelToWorkScreen
                    TravelToWorkScreen travelToWorkScreen = new TravelToWorkScreen();
                    travelToWorkScreen.prepareScreen(view, directionWay, session, mContext, currentPosition);

                } else if(directionWay.isInWork()){
                    //WorkScreen
                    WorkScreen workScreen = new WorkScreen();
                    workScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, startWorkTime);

                } else if(directionWay.isInWayToHome()){
                    //TravelToHomeScreen
                    TravelToHomeScreen travelToHomeScreen = new TravelToHomeScreen();
                    travelToHomeScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, whenUserLeaveHome);

                } else if(directionWay.getIsInHome()){
                    HomeScreen homeScreen = new HomeScreen();
                    homeScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, whenUserLeaveHome);
                }
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public DirectionWay getDirectionWay() {
        return directionWay;
    }

    public void setDirectionWay(DirectionWay directionWay) {
        this.directionWay = directionWay;
    }

    public MorningScreen getMorningScreen() {
        return morningScreen;
    }

    public void setMorningScreen(MorningScreen morningScreen) {
        this.morningScreen = morningScreen;
    }

    public AppWidgetManager getManager() {
        return manager;
    }

    public void setManager(AppWidgetManager manager) {
        this.manager = manager;
    }

    public ComponentName getThisWidget() {
        return thisWidget;
    }

    public void setThisWidget(ComponentName thisWidget) {
        this.thisWidget = thisWidget;
    }

    public RemoteViews getView() {
        return view;
    }

    public void setView(RemoteViews view) {
        this.view = view;
    }
}
