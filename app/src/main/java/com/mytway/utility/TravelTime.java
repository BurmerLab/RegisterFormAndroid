package com.mytway.utility;

import android.content.Context;
import android.os.AsyncTask;
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

import org.joda.time.LocalDateTime;
import org.json.JSONException;

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
    public void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public void processTime() throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public void fullProcessTime(Context context, Position currentPosition, Session session) throws Exception {
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
            setGoogleMapsDirectionJson(context, currentPosition, session.getHomePlace());

        }else if( directionWay.isWayToWork()){
            setGoogleMapsDirectionJson(context, currentPosition, session.getWorkPlace());

        }else if(directionWay.isInHome()) {
            setGoogleMapsDirectionJson(context, currentPosition, session.getWorkPlace());

        }else{
            Log.i(TAG, "There is no direction defined (isWayToHome and Work set to false, not supported");
        }
    }

    public GoogleMapsDirectionJson obtainCurrentTravelTimeToHome(Context context, Position currentPosition, Session session) {
        GoogleMapsDirectionJson currentTravelTimeToHome = getTravelTimeBetweenTwoPositions(context, currentPosition, session.getHomePlace());
        return currentTravelTimeToHome;
    }

    public GoogleMapsDirectionJson obtainCurrentTravelTimeToWork(Context context, Position currentPosition, Session session) {
        GoogleMapsDirectionJson currentTravelTimeToWork = getTravelTimeBetweenTwoPositions(context, currentPosition, session.getWorkPlace());
        return currentTravelTimeToWork;
    }

    public GoogleMapsDirectionJson getGoogleMapsDirectionJson() {
        return googleMapsDirectionJson;
    }

    public void setGoogleMapsDirectionJson(GoogleMapsDirectionJson googleMapsDirectionJson) {
        this.googleMapsDirectionJson = googleMapsDirectionJson;
    }
    public void setGoogleMapsDirectionJson(Context context, Position currentPosition, Position endPosition) {
        this.googleMapsDirectionJson =  getTravelTimeBetweenTwoPositions(context, currentPosition, endPosition);
        this.displayTimeMessage = this.googleMapsDirectionJson.getLegs().getDuration().getDurationTime().toString();
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
