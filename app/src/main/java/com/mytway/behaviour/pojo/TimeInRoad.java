package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

public class TimeInRoad extends AProcessingTime implements IDisplayedTime{
    private static final String TAG = "TimeInRoad";

    private LocalDateTime timeInRoad;
    private String displayTimeMessage;

    @Override
    public String displayMessage() {
        return this.displayTimeMessage;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in TimeInRoad");
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime) throws Exception {
        throw new Exception("Not supported processTime here, in TimeInRoad");
    }

    @Override
    public void processTime() {
        if(timeInRoad != null){
            String displayMessage = prepareTimeFromLocalDateTimeToString(timeInRoad);
            Log.i(TAG, "Time in road: " + displayMessage);
            setTimeInRoad(timeInRoad);
            setDisplayTimeMessage(displayMessage);
        }else{
            Log.i(TAG, "time in road is null, firstly add time in road before start processing");
        }
    }

    @Override
    public void fullProcessTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    public LocalDateTime getTimeInRoad() {
        return timeInRoad;
    }

    public void setDisplayTimeMessage(String displayTimeMessage) {
        this.displayTimeMessage = displayTimeMessage;
    }

    public void setTimeInRoad(LocalDateTime timeInRoad) {
        this.timeInRoad = timeInRoad;
    }

    public void setTimeInRoad(TimeToDeparture timeToDeparture) {
        this.timeInRoad =
                timeToDeparture.getTravelTime().getGoogleMapsDirectionJson().getLegs().getDuration().getDurationTime();
    }

    public String getDisplayTimeMessage() {
        return displayTimeMessage;
    }
}
