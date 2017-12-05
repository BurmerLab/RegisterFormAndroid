package com.mytway.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.AProcessingTime;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.IDisplayedTime;
import com.mytway.pojo.Distance;
import com.mytway.pojo.Duration;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Legs;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;

import org.joda.time.LocalDateTime;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TravelTime extends AProcessingTime implements IDisplayedTime {

    private final String TAG = "TravelTime";

    private GoogleMapsDirectionJson googleMapsDirectionJson;
    private DirectionWay directionWay;
    private String displayTimeMessage;

    public GoogleMapsDirectionJson getTravelTimeBetweenTwoPositions(Context context, Position startPosition, Position endPosition) {

        GoogleMapsDirectionJson googleMapsDirectionJson = null;

        if(EthernetConnectivity.isEthernetOnline(context)) {
            MytwayWebserviceGetTravelTimeBetweenTwoPositions webServiceGetTravelTime = new MytwayWebserviceGetTravelTimeBetweenTwoPositions();

            try {
                googleMapsDirectionJson = webServiceGetTravelTime.execute(startPosition, endPosition).get();

            }catch(InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with obtaining travel time from web service ", e);
                e.printStackTrace();
            }

            if (googleMapsDirectionJson == null) {
                Log.i(TAG, "travelTime is null");
                googleMapsDirectionJson = new GoogleMapsDirectionJson();
                Legs legs = new Legs();
                legs.setDistance(new Distance());
                legs.setDuration(new Duration());
                googleMapsDirectionJson.setLegs(legs);
            }
        }else{
            //no internet, set on widget icon that time travel is static not from internet
            Toast.makeText(context, context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            googleMapsDirectionJson = new GoogleMapsDirectionJson();
            Legs legs = new Legs();
            legs.setDistance(new Distance());
            legs.setDuration(new Duration());
            googleMapsDirectionJson.setLegs(legs);
        }
        return googleMapsDirectionJson;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime, boolean useEstimate) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public void processTime() throws Exception {
        String travelTime = convertTimeToTimeLeftFormat(googleMapsDirectionJson.getLegs().getDuration().getDurationTime());
        setDisplayTimeMessage(travelTime);
    }

    @Override
    public void fullProcessTime(Context context, Position currentPosition, Session session, boolean useEstimate) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public String displayMessage() {
        return displayTimeMessage;
    }

    private class MytwayWebserviceGetTravelTimeBetweenTwoPositions extends AsyncTask<Position, Void, GoogleMapsDirectionJson> {

        @Override
        protected GoogleMapsDirectionJson doInBackground(Position... arg0) {
            GoogleMapsDirectionJson webServiceResult = null;
            Position startPosition = arg0[0];
            Position endPosition = arg0[1];


            MytwayWebservice mytwayWebservice = new MytwayWebservice();
            if(startPosition != null && endPosition != null){
                try {
                    Log.i(TAG, "Trying get travel time between positions");
                    webServiceResult  = mytwayWebservice.getTravelTimeBetweenPlaces(startPosition, endPosition);
                } catch (JSONException e) {
                    Log.i(TAG, "Problem with getting travel time: ", e);
                    e.printStackTrace();
                }
            }else{
                Log.i(TAG, "Start or End Position is empty, can't obtain travel time");
            }

            return webServiceResult;
        }
    }

    public void obtainTravelTimeBasedOnDirectonWay(Context context, Position currentPosition, Session session) {
        if(directionWay.isWayToHome()){
            setGoogleMapsDirectionJson(context, currentPosition, session.getHomePlace(), session);

        }else if( directionWay.isWayToWork()){
            setGoogleMapsDirectionJson(context, currentPosition, session.getWorkPlace(), session);

        }else if(directionWay.isInHome()) {
            setGoogleMapsDirectionJson(context, currentPosition, session.getWorkPlace(), session);

        }else if(directionWay.isInWork()) {
            setGoogleMapsDirectionJson(context, currentPosition, session.getHomePlace(), session);

        }else{
            Log.i(TAG, "There is no direction defined (isWayToHome and Work set to false, not supported");
        }
    }

    public void obtainEstimationByStaticTravelTimeBasedOnDirectonWay(Context context, Position currentPosition, Session session) {
        if(directionWay.isWayToHome()){
            this.googleMapsDirectionJson = obtainStaticTravelTime(context, session, currentPosition, session.getHomePlace());

        }else if( directionWay.isWayToWork()){
            this.googleMapsDirectionJson = obtainStaticTravelTime(context, session, currentPosition, session.getWorkPlace());

        }else if(directionWay.isInHome()) {
            this.googleMapsDirectionJson = obtainStaticTravelTime(context, session, currentPosition, session.getWorkPlace());

        }else if(directionWay.isInWork()) {
            this.googleMapsDirectionJson = obtainStaticTravelTime(context, session, currentPosition, session.getHomePlace());

        }else{
            Log.i(TAG, "There is no direction defined (isWayToHome and Work set to false, not supported");
        }
    }

    public GoogleMapsDirectionJson obtainCurrentTravelTimeToHome(Context context, Position currentPosition, Session session, boolean useEstimate) {
        GoogleMapsDirectionJson currentTravelTimeToHome;
        if(useEstimate){
            currentTravelTimeToHome = obtainStaticTravelTime(context, session, currentPosition, session.getHomePlace());
        }else{
            currentTravelTimeToHome = getTravelTimeBetweenTwoPositions(context, currentPosition, session.getHomePlace());
        }

        return currentTravelTimeToHome;
    }

    public GoogleMapsDirectionJson obtainCurrentTravelTimeToWork(Context context, Position currentPosition, Session session, boolean useEstimate) {
        GoogleMapsDirectionJson currentTravelTimeToWork;
        if(useEstimate){
            currentTravelTimeToWork = obtainStaticTravelTime(context, session, currentPosition, session.getWorkPlace());
        }else{
            currentTravelTimeToWork = getTravelTimeBetweenTwoPositions(context, currentPosition, session.getWorkPlace());
        }
        return currentTravelTimeToWork;
    }

    public GoogleMapsDirectionJson getGoogleMapsDirectionJson() {
        return googleMapsDirectionJson;
    }

    public void setGoogleMapsDirectionJson(GoogleMapsDirectionJson googleMapsDirectionJson) {
        this.googleMapsDirectionJson = googleMapsDirectionJson;
    }

    public void setGoogleMapsDirectionJson(Context context, Position currentPosition, Position endPosition, Session session) {
        this.googleMapsDirectionJson =  getTravelTimeBetweenTwoPositions(context, currentPosition, endPosition);

        if(this.googleMapsDirectionJson == null){
            Integer fullDuration = Integer.parseInt(session.getWayDuration()); //i.e. 3630 - seconds
            Double fullDistanceHomeAndWork = Distance.designateDistanceBetween(session.getHomePlace(), session.getWorkPlace());
            Double currentDistanceToEndWay = Distance.designateDistanceBetween(currentPosition, endPosition);
            Double estimatedDurationToEndWay = estimateTravelDuration(fullDistanceHomeAndWork, currentDistanceToEndWay, fullDuration);
            this.googleMapsDirectionJson = getGmapsDirectionJsonFromStaticEstimation(estimatedDurationToEndWay, currentDistanceToEndWay);
        }

        String displayMessage = convertTimeToTimeLeftFormat(this.googleMapsDirectionJson.getLegs().getDuration().getDurationTime());
        setDisplayTimeMessage(displayMessage);
    }

    public GoogleMapsDirectionJson obtainStaticTravelTime(Context context, Session session, Position currentPosition, Position endPosition){
        Integer fullDuration = Integer.parseInt(session.getWayDuration()); //i.e. 3630 - seconds

        Double fullDistanceHomeAndWork = Distance.designateDistanceBetween(session.getHomePlace(), session.getWorkPlace());
        Double currentDistanceToEndWay = Distance.designateDistanceBetween(currentPosition, endPosition);

        saveToFileStaticTravel("fullDistanceHomeAndWork: " + fullDistanceHomeAndWork);
        saveToFileStaticTravel("currentDistanceToEndWay: " + currentDistanceToEndWay);

        //1.698107877088591
        Double estimatedDurationToEndWay = estimateTravelDuration(fullDistanceHomeAndWork, currentDistanceToEndWay, fullDuration);

        GoogleMapsDirectionJson gmapsDirectionJsonFromStaticEstimation = getGmapsDirectionJsonFromStaticEstimation(estimatedDurationToEndWay, currentDistanceToEndWay);
        this.googleMapsDirectionJson = gmapsDirectionJsonFromStaticEstimation;

        saveToFileStaticTravel("Duration: " + estimatedDurationToEndWay);

        String displayMessage = convertTimeToTimeLeftFormat(googleMapsDirectionJson.getLegs().getDuration().getDurationTime());
        setDisplayTimeMessage(displayMessage);

        saveToFileStaticTravel("displayMessage: " + displayMessage);
        return gmapsDirectionJsonFromStaticEstimation;
    }

    public static void saveToFileStaticTravel(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "STATIC_TRAVELTIME.txt");
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
    }

    public GoogleMapsDirectionJson getGmapsDirectionJsonFromStaticEstimation(Double estimatedDurationToEndWay, double currentDistanceToEndWay) {
        GoogleMapsDirectionJson gMapsDirectionFromStaticEstimation = new GoogleMapsDirectionJson();
        Legs legs = new Legs();
        legs.setDistance(new Distance("" + currentDistanceToEndWay * PropertiesValues.METERS_IN_KILOMETER, currentDistanceToEndWay * PropertiesValues.METERS_IN_KILOMETER));
        legs.setDuration(new Duration("" + estimatedDurationToEndWay.intValue(), estimatedDurationToEndWay.intValue()));
        gMapsDirectionFromStaticEstimation.setLegs(legs);
        return gMapsDirectionFromStaticEstimation;
    }

    public Double estimateTravelDuration(Double fullDistance, Double currentDistance, Integer fullDuration){
//        68.2km - 3630s
//        39.1km - x
//        68.2x = 39.1*3630 = 141933/68.2
//        x = 2081.12
        Double estimatedTravelDuration = 0d;

        if(fullDistance != null && currentDistance != null){
            estimatedTravelDuration = (currentDistance * fullDuration) / fullDistance;
        }//3351.9033364121105
        return estimatedTravelDuration;
    }

    public DirectionWay getDirectionWay() {
        return directionWay;
    }

    public void setDirectionWay(DirectionWay directionWay) {
        this.directionWay = directionWay;
    }

    public String getDisplayTimeMessage() {
        return displayTimeMessage;
    }

    public void setDisplayTimeMessage(String displayTimeMessage) {
        this.displayTimeMessage = displayTimeMessage;
    }
}
