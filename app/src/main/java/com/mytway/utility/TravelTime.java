package com.mytway.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.pojo.Distance;
import com.mytway.pojo.Duration;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Legs;
import com.mytway.pojo.Position;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class TravelTime {

    private final String TAG = "TravelTime";

    private GoogleMapsDirectionJson googleMapsDirectionJson;

//Example:
//    https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyBLPJAGAe4Ypmb70IHMVh5WWz1OsEjGTMw
//    http://stackoverflow.com/questions/16756955/travel-time-between-two-locations-in-google-map-android-api-v2

    public GoogleMapsDirectionJson getTravelTimeBetweenTwoPositions(Context context, Position startPosition, Position endPosition) {

        GoogleMapsDirectionJson travelTime = null;

        if(EthernetConnectivity.isEthernetOnline(context)) {
            MytwayWebserviceGetTravelTimeBetweenTwoPositions webServiceGetTravelTime = new MytwayWebserviceGetTravelTimeBetweenTwoPositions();

            try {
                travelTime = webServiceGetTravelTime.execute(startPosition, endPosition).get();

            }catch(InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with obtaining isPasswordCorrectInExternalDatabase ", e);
                e.printStackTrace();
            }

            if (travelTime == null) {
                Log.i(TAG, "travelTime is null");
                travelTime = new GoogleMapsDirectionJson();
                Legs legs = new Legs();
                legs.setDistance(new Distance());
                legs.setDuration(new Duration());
                travelTime.setLegs(legs);
            }
        }else{
            //no internet, set on widget icon that time travel is static not from internet
            Toast.makeText(context, context.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            travelTime = new GoogleMapsDirectionJson();
            Legs legs = new Legs();
            legs.setDistance(new Distance());
            legs.setDuration(new Duration());
            travelTime.setLegs(legs);
        }
        return travelTime;
    }

    private class MytwayWebserviceGetTravelTimeBetweenTwoPositions extends AsyncTask<Position, Void, GoogleMapsDirectionJson> {

        @Override
        protected GoogleMapsDirectionJson doInBackground(Position... arg0) {
            GoogleMapsDirectionJson webServiceResult = null;
            Position startPosition = arg0[0];
            Position endPosition = arg0[0];


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


    public GoogleMapsDirectionJson getGoogleMapsDirectionJson() {
        return googleMapsDirectionJson;
    }

    public void setGoogleMapsDirectionJson(GoogleMapsDirectionJson googleMapsDirectionJson) {
        this.googleMapsDirectionJson = googleMapsDirectionJson;
    }
    public void setGoogleMapsDirectionJson(Context context, Position currentPosition, Position endPosition) {
        this.googleMapsDirectionJson =  getTravelTimeBetweenTwoPositions(context, currentPosition, endPosition);
    }



}
