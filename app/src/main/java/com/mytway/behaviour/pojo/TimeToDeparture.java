package com.mytway.behaviour.pojo;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.nfc.Tag;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.pojo.TypeWork;
import com.mytway.pojo.User;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeToDeparture extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeToDeparture";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTime;
    private DirectionWay directionWay;

    @Override
    public String displayMessage() {

        return null;
    }

    @Override
    public Calendar processTime(Context context, Position currentPosition) {
        long timeToDepartureInLong = 0;
        session = new Session(context);
        TimeToDeparture timeToDeparture = new TimeToDeparture();

        TravelTime travelTime = obtainTravelTimeBasedOnDirectonWay(context, currentPosition);

        int userTypeWork = session.getTypeWork();
        if(userTypeWork == TypeWork.STANDARD_TYPE.getStatusCode()){
            System.out.println("Standard type");
            Calendar startStandardTimeWork = prepareTimeFromStringToCalendar(session.getStartStandardTimeWork());
            timeToDepartureInLong =
                    (startStandardTimeWork.getTimeInMillis() - travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getValue())
                            - currentTime.getCurrentTime().getTimeInMillis();
        }else if(userTypeWork == TypeWork.FLEXIBLE_TYPE.getStatusCode()){
            System.out.println("Flexible type");
            timeToDepartureInLong =
                    currentTime.getCurrentTime().getTimeInMillis() + travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getValue();
        }else{
            Log.i(TAG, "User TypeWork not defined, problem");
        }

        Calendar calendarMessage = Calendar.getInstance();
        calendarMessage.setTimeInMillis(timeToDepartureInLong);

        return calendarMessage;
    }

    public Calendar prepareTimeFromStringToCalendar(String startStandardTimeWork) {

        Calendar calendar = dateToCalendar(startStandardTimeWork);
        return calendar;
    }

    public Calendar dateToCalendar(String timeString){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date date = null;
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return  calendar;
    }


    public TravelTime obtainTravelTimeBasedOnDirectonWay(Context context, Position currentPosition) {
        TravelTime travelTime = new TravelTime();
        if(directionWay.isWayToHome()){
            travelTime.setGoogleMapsDirectionJson(context, currentPosition, session.getHomePlace());
        }else if( directionWay.isWayToWork()){
            travelTime.setGoogleMapsDirectionJson(context, currentPosition, session.getWorkPlace());
        }else{
            Log.i(TAG, "There is no direction defined (isWayToHome and Work set to false, not supported");
        }
        return travelTime;
    }

    public String getDisplayTimeMessage() {
        return displayTimeMessage;
    }

    public void setDisplayTimeMessage(String displayTimeMessage) {
        this.displayTimeMessage = displayTimeMessage;
    }

    public CurrentTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(CurrentTime currentTime) {
        this.currentTime = currentTime;
    }

    public TravelTime getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(TravelTime travelTime) {
        this.travelTime = travelTime;
    }

    public DirectionWay getDirectionWay() {
        return directionWay;
    }

    public void setDirectionWay(DirectionWay directionWay) {
        this.directionWay = directionWay;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
