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
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.AProcessingTime;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.screens.MorningScreen;
import com.mytway.behaviour.pojo.screens.TravelToHomeScreen;
import com.mytway.behaviour.pojo.screens.TravelToWorkScreen;
import com.mytway.behaviour.pojo.screens.WorkScreen;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.database.userLocalizations.UserLocalizationsRepo;
import com.mytway.database.userLocalizations.UserLocalizationsTable;
import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.pojo.TypeWork;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.ScheduledProcess;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;
import com.mytway.utility.TravelTimeGogDirectionRequest;
import com.mytway.utility.Utility;
import com.mytway.utility.permission.PermissionUtil;
import com.mytway.widget.MyWidgetProvider;
import com.mytway.widget.WidgetUtils;
import com.mytway.widget.utils.StandardRepeatIntervalProcessor;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

public class MytwayGeolocalizationService extends Service implements LocationListener {

    private Context mContext;
    private Session session;
    private static final String TAG = "MytwayGeolocalizationService";
    private static final int RED_COLOR =  -65536;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    public DirectionWay directionWay;

    public android.location.Location location;
    private double latitude;
    private double longitude;
    private AppWidgetManager manager;
    private ComponentName thisWidget;
    private RemoteViews view;
    private LocalDateTime currentTime = new LocalDateTime();

    private LocalDateTime temporaryTimeForLogging;

    // The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    // The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private MorningScreen morningScreen = new MorningScreen();

    public MytwayGeolocalizationService(Context context) {
        this.mContext = context;
        directionWay = new DirectionWay(mContext);
    }

    public MytwayGeolocalizationService() {
    }


    public void computeMytwayWidget() throws Exception {


        if(!PropertiesValues.MOCK_APP_TO_TESTS){
            //remove in unit tests:
            session = new Session(mContext);
            session.setFullTimeTravelHomeToWork(mContext);
        }

        view = new RemoteViews(getPackageName(), R.layout.mytway5_table_middle_widget_layout);
        String time = Calendar.getInstance().getTime().toString();

        if(session.isUserLogged()){
            thisWidget = new ComponentName(this, MyWidgetProvider.class);
            manager = AppWidgetManager.getInstance(this);

            if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, mContext)
                    && PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, mContext)) {

                updateDBTravelTimeFromGoogleDirections();

                runScheduledProcessOncePerDay(mContext);

                getLocalization();

                Position currentPosition = new Position(longitude, latitude);

                Distance distanceBetweenHomeAndWork = new Distance("", Double.parseDouble(session.getWayDistance()));
                directionWay.setDistanceBetweenHomeAndWork(distanceBetweenHomeAndWork);

                saveToFile("--------------------BEFORE-Decisions------------------------", "DirectionWay.txt");
                saveToFile("------isInWayToWork: " + directionWay.getDirectionsStatus().isInWayToWork() + " -------- ", "DirectionWay.txt");
                saveToFile("------isInWayToHome: " + directionWay.getDirectionsStatus().isInWayToHome()+ " -------- ", "DirectionWay.txt");
                saveToFile("------isStillInWork: " + directionWay.getDirectionsStatus().isInWork()+ " -------- ", "DirectionWay.txt");
                saveToFile("------isStillInHome: " + directionWay.getDirectionsStatus().isInHome()+ " -------- ", "DirectionWay.txt");
                saveToFile("----------------------------------------------------\n", "DirectionWay.txt");

//                directionWay.getPercentageOfDistanceBtwHomeAndWorkInMeters();
                directionWay.decideTravelDirectionsAre(currentPosition);

                saveToFile("--------------------AFTER-------------------------", "DirectionWay.txt");
                saveToFile("------isInWayToWork: " + directionWay.getDirectionsStatus().isInWayToWork() + " -------- ", "DirectionWay.txt");
                saveToFile("------isInWayToHome: " + directionWay.getDirectionsStatus().isInWayToHome()+ " -------- ", "DirectionWay.txt");
                saveToFile("------isStillInWork: " + directionWay.getDirectionsStatus().isInWork()+ " -------- ", "DirectionWay.txt");
                saveToFile("------isStillInHome: " + directionWay.getDirectionsStatus().isInHome()+ " -------- ", "DirectionWay.txt");
                saveToFile("----------------------------------------------------\n", "DirectionWay.txt");


                //todo: b rac startWork time i startStandardTimeWork from directionWay.getUser... .getStartWOrk....
                //pobierac z sesji
                LocalDateTime startStandardTimeWork = AProcessingTime.prepareTimeFromStringToCalendar(session.getStartStandardTimeWork());//8:00
                LocalDateTime startWorkTime = new LocalDateTime(); //null

                //for future to remove or repleace in one place here not in every line MorningScreen :)
                TravelTime travelTime = new TravelTime();
                travelTime.setDirectionWay(directionWay);

                //--------------------------------------------------
                //todo: commented to test static obtaining (added below)
//                travelTime.obtainTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);
                travelTime.obtainEstimationByStaticTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);
                //--------------------------------------------------

                if(session.getTypeWork() == TypeWork.STANDARD_TYPE.getStatusCode()){

//                    todo: uncomment and create screen for days without work
//                    WorkWeek workWeek = WorkWeek.createWorkWeekFromString(session.getWorkWeek());
//                    if(workWeek.checkIsDayEnable(new LocalDateTime().getDayOfWeek())){
//                        saveToFile("Today is day of work", "WorkWeek.txt");

                    boolean useEstimate = true;

                        if(directionWay.getDirectionsStatus().isInHome()){
                            //for now it is only for standard user time
                            StandardRepeatIntervalProcessor.calculateSamplingTimeOfWidgetRepeatForStandardUser(session, travelTime, directionWay);

                              //todo: correct code, commented only for testing
//                            if(timeToStartMorningScreen.getHourOfDay() < Hours.FIVE.getHours()){
                            //Morning screen
                            saveToFileLocalizationNewVersion("Morning");

                            UserLocalizationsRepo localizationsRepo = new UserLocalizationsRepo(mContext);
                            UserLocalizationsTable localizationsTable = new UserLocalizationsTable();
                            localizationsTable.setCreationDate("2018-03-18");
                            localizationsTable.setLatitude(String.valueOf(getLatitude()));
                            localizationsTable.setLongitude(String.valueOf(getLongitude()));
                            localizationsTable.setTimeStatus("Morning");
                            localizationsTable.setUserName("Mike");
                            localizationsRepo.insert(localizationsTable);


                            DirectionWay.saveToFile("-------------------MORNING SCREEN-----------------------");
                            morningScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, travelTime, useEstimate);

                            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.RARELY_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
                            PropertiesValues.INTERVAL_TYPE = "RARELY";
                            StandardRepeatIntervalProcessor.saveToFileIntervals("RARELY - IS IN HOME");
//
//
// }else{
//                                saveToFileLocalizationNewVersion("Home screen");
//                                DirectionWay.saveToFile("\n\n-------------------Home SCREEN-----------------------");
//                                HomeScreen homeScreen = new HomeScreen();
//                                homeScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, whenUserLeaveHome);
//                            }
                        } else if(directionWay.getDirectionsStatus().isInWayToWork()){
                            //TravelToWorkScreen

                            //for now it is only for standard user time
                            StandardRepeatIntervalProcessor.calculateSamplingTimeOfWidgetRepeatForStandardUser(session, travelTime, directionWay);

                            saveToFileLocalizationNewVersion("TravelToWork");
                            DirectionWay.saveToFile("\n\n-------------------TravelToWork SCREEN-----------------------");
                            TravelToWorkScreen travelToWorkScreen = new TravelToWorkScreen();
                            travelToWorkScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, travelTime, useEstimate);

                            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.OFTEN_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
                            PropertiesValues.INTERVAL_TYPE = "OFTEN";
                            StandardRepeatIntervalProcessor.saveToFileIntervals("OFTEN - IS IN WAY TO WORK");

                        } else if(directionWay.getDirectionsStatus().isInWork()){
                            //WorkScreen
                            saveToFileLocalizationNewVersion("Work ");
                            DirectionWay.saveToFile("\n\n-------------------Work SCREEN-----------------------");
                            WorkScreen workScreen = new WorkScreen();
                            workScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, startWorkTime, travelTime, useEstimate);

                            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.RARELY_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
                            PropertiesValues.INTERVAL_TYPE = "RARELY";
                            StandardRepeatIntervalProcessor.saveToFileIntervals("RARELY - IS IN WORK");

                        } else if(directionWay.getDirectionsStatus().isInWayToHome()){
                            //TravelToHomeScreen
                            saveToFileLocalizationNewVersion("TravelToHome");
                            DirectionWay.saveToFile("\n\n-------------------TravelToHome SCREEN-----------------------");
                            TravelToHomeScreen travelToHomeScreen = new TravelToHomeScreen();

                            //todo: new LocalDateTime() zastapic prawidlowym whenUserLeaveHome
                            travelToHomeScreen.prepareScreen(view, directionWay, session, mContext, currentPosition, new LocalDateTime(), travelTime, useEstimate);

                            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.OFTEN_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
                            PropertiesValues.INTERVAL_TYPE = "OFTEN";
                            StandardRepeatIntervalProcessor.saveToFileIntervals("OFTEN - IS IN WAY TO HOME");

                        } else {
                            saveToFileLocalizationNewVersion("Not Found yet");
                            DirectionWay.saveToFile("\n\n------------------NOT FOUNDED YET-----------------------");
                            view.setTextViewText(R.id.title, "NOT FOUNDED YET " + time);
                        }
//                    } else {
//                        saveToFile("Today is not a day of work ", "WorkWeek.txt");
//                    }
                }

            }else{
                saveToFileLocalizationNewVersion("Not Permissions granded");
                view.setTextViewText(R.id.title, "Not Permissions granded " + time);
                view.setImageViewResource(R.id.refreshImage, R.drawable.ic_error);
                MyWidgetProvider.openNewActivity(mContext, manager, manager.getAppWidgetIds(thisWidget), view, R.id.refreshImage, new String[0]);
            }

            manager.updateAppWidget(thisWidget, view);
        }else{
            Log.i(TAG, "User is not logged");
        }
    }

    private void updateDBTravelTimeFromGoogleDirections() {
        TravelTimeGogDirectionRequest directionRequest = new TravelTimeGogDirectionRequest();
        directionRequest.refreshTravelTimeBasedOnGogDirections(mContext, session);
    }

    private void runScheduledProcessOncePerDay(Context context){
        ScheduledProcess scheduledProcess = new ScheduledProcess();
        UserRepo userRepo = new UserRepo(context);
        UserTable userTable = userRepo.getUserByUserName(session.getUserName());

        saveToFile("------runScheduledProcessOncePerDay------", "scheduled.txt");

        if(Utility.shouldDoRefreshInThisDay(context)){
            saveToFile("shouldDoRefreshInThisDay = true", "scheduled.txt");
            scheduledProcess.runScheduledProcess(context, userTable, userTable.createJson());
        }
        saveToFile("------END------", "scheduled.txt");
    }

    public static void saveToFile(String content, String fileName) {
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

            if(!dir.exists()){
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            LocalDateTime currentLocalDateTime = new LocalDateTime();
            //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

            FileOutputStream fop = new FileOutputStream(file, true);
            String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


            fop.write(pointXml.getBytes());
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFileLocalizationNewVersion(String screenTitle) throws IOException {
        //-----------Only for saving current Localization to file-------
//        File dir = new File ("c:/test/");
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

        if(!dir.exists()){
            dir.mkdirs();
        }

        LocalDateTime currentLocalDateTime = new LocalDateTime();
        LocalDateTime yesterdayLocalDateTime = new LocalDateTime().minusDays(1);

        File file;
        if(currentLocalDateTime.getHourOfDay() < 14){
            file = new File(dir, currentLocalDateTime.toString("dd-MM-yyyy") + "_MORNING.kml");
        }else{
            file = new File(dir, currentLocalDateTime.toString("dd-MM-yyyy") + "_AFTERNOON.kml");
        }

        if(!file.exists()){

            File fileMorning = new File(dir, yesterdayLocalDateTime.toString("dd-MM-yyyy") + "_MORNING.kml");
            File fileAfternoon = new File(dir, yesterdayLocalDateTime.toString("dd-MM-yyyy") + "_AFTERNOON.kml");

            if(fileMorning.exists()){
                if( ! isEndDocumentPutted("</Document>", fileMorning)){
                    FileOutputStream fop = new FileOutputStream(fileMorning, true);
                    StringBuilder yesterdayMorningXml = new StringBuilder();
                    yesterdayMorningXml.append("  </Document>\n" + "</kml>");
                    fop.write(yesterdayMorningXml.toString().getBytes());
                    fop.flush();
                    fop.close();
                }
            }

            if(fileAfternoon.exists()){
                if( ! isEndDocumentPutted("</Document>", fileAfternoon)){
                    FileOutputStream fop = new FileOutputStream(fileAfternoon, true);
                    StringBuilder yesterdayAfternoonXml = new StringBuilder();
                    yesterdayAfternoonXml.append("  </Document>\n" + "</kml>");
                    fop.write(yesterdayAfternoonXml.toString().getBytes());
                    fop.flush();
                    fop.close();
                }
            }

            file.createNewFile();
            FileOutputStream fop = new FileOutputStream(file, true);
            StringBuilder pointXml = new StringBuilder();
            pointXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                    "  <Document>\n" +
                    "    <name>Paths</name>\n" +
                    "    <description>Examples of paths. Note that the tessellate tag is by default\n" +
                    "      set to 0. If you want to create tessellated lines, they must be authored\n" +
                    "      (or edited) directly in KML.</description>\n" +
                    "    <Style id=\"yellowLineGreenPoly\">\n" +
                    "      <LineStyle>\n" +
                    "        <color>7f00ffff</color>\n" +
                    "        <width>4</width>\n" +
                    "      </LineStyle>\n" +
                    "      <PolyStyle>\n" +
                    "        <color>7f00ff00</color>\n" +
                    "      </PolyStyle>\n" +
                    "    </Style>\n");
            fop.write(pointXml.toString().getBytes());
            fop.flush();
            fop.close();
        }

        FileOutputStream fop = new FileOutputStream(file, true);
        StringBuilder pointXml = new StringBuilder();

        pointXml.append("\t<Placemark>\n" +
                "        <name> " + screenTitle + " " + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") +
                "</name>\n" +
                "        <description> " + screenTitle + " " + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + "</description>\n" +
                "        <Point>\n" +
                "            <coordinates> "+ getLongitude() + " , " + getLatitude() + " </coordinates>\n" +
                "        </Point>\n" +
                "    </Placemark>\n\n");

        fop.write(pointXml.toString().getBytes());
        fop.flush();
        fop.close();

        System.out.println("Done");
    }

    public boolean isEndDocumentPutted(String neededContent, File file){
        try {
            Scanner scanner = new Scanner(file);

            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.contains("</Document>")) {
                    return true;
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("Problem with scanning file - " + e);
        }

        return false;
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
                        android.location.Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateGPSCoordinates(gpsLocation);
                        location = gpsLocation;
                    }
                }

                if (isNetworkEnabled) {
                    if (location == null) {

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            android.location.Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            updateGPSCoordinates(networkLocation);
                            location = networkLocation;
                        }
                    }
                }else{
                    Toast.makeText(mContext, "NIMA NETWORKA", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error : Location", "Impossible to connect to LocationManager ", e);
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
        }
    };

    @Override
    public void onCreate(){
        IntentFilter filter = new IntentFilter();
        mContext = getApplicationContext();
        directionWay = new DirectionWay(mContext);
        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        try {
            computeMytwayWidget();
        } catch (Exception e) {
            Log.i(TAG, "Problem with method computeMytwayWidget in service", e);
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
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
