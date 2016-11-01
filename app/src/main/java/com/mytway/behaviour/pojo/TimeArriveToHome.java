package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TimeArriveToHome extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeArriveToHome";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTimeToWork;
    private TravelTime travelTimeToHome;
    private DirectionWay directionWay;
    private LocalDateTime timeArrive;

    @Override
    public void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime) throws Exception{
        Log.i(TAG, "Starting processing of TimeArriveToHome");
        Log.i(TAG, "Current time: " + getCurrentTime());
        if(session.isUserLogged()){
            //TimeArriveToHome - (TimeToEndWork + travelTime(ToHome)
            LocalDateTime lenghtWorkTime = prepareTimeFromStringToCalendar(session.getLengthTimeWork());

            //Travel time
            TravelTime travelTime = new TravelTime();
            travelTime.setDirectionWay(directionWay);
            travelTime.obtainTravelTimeBasedOnDirectonWay(context, currentPosition, session);

            LocalDateTime timeToEndWork = addTimeTo(startWorkTime,
                    lenghtWorkTime.getHourOfDay(),
                    lenghtWorkTime.getMinuteOfHour(),
                    lenghtWorkTime.getSecondOfMinute());

            LocalDateTime timeArriveToHome = addTimeTo(timeToEndWork,
                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

            String displayMessage = prepareTimeFromLocalDateTimeToString(timeArriveToHome);
            Log.i(TAG, "TimeArriveToHome: " + displayMessage);

            setTimeArrive(timeArriveToHome);
            setDisplayTimeMessage(displayMessage);
        }
    }

    @Override
    public void processTime() throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public String displayMessage() {
        return displayTimeMessage;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) throws Exception {
        Log.i(TAG, "Starting processing of TimeArriveToHome");
        Log.i(TAG, "Current time: " + getCurrentTime());
        if(session.isUserLogged()){
            //TimeArriveToHome for TravelToHomeScreen (2nd)
            //TimeArriveToHome = currentTime + travelTime
            TravelTime travelTime = new TravelTime();
            travelTime.setDirectionWay(directionWay);
            travelTime.obtainTravelTimeBasedOnDirectonWay(context, currentPosition, session);

            LocalDateTime timeArriveToHome = addTimeTo(currentTime.getCurrentTime(),
                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

            String displayMessage = prepareTimeFromLocalDateTimeToString(timeArriveToHome);
            Log.i(TAG, "TimeArriveToHome: " + displayMessage);

            setTimeArrive(timeArriveToHome);
            setDisplayTimeMessage(displayMessage);
        }
    }

    public String getDisplayTimeMessage() {
        return displayTimeMessage;
    }

    public void setDisplayTimeMessage(String displayTimeMessage) {
        this.displayTimeMessage = displayTimeMessage;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public CurrentTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(CurrentTime currentTime) {
        this.currentTime = currentTime;
    }

    public TravelTime getTravelTimeToWork() {
        return travelTimeToWork;
    }

    public void setTravelTimeToWork(TravelTime travelTimeToWork) {
        this.travelTimeToWork = travelTimeToWork;
    }

    public DirectionWay getDirectionWay() {
        return directionWay;
    }

    public void setDirectionWay(DirectionWay directionWay) {
        this.directionWay = directionWay;
    }

    public TravelTime getTravelTimeToHome() {
        return travelTimeToHome;
    }

    public void setTravelTimeToHome(TravelTime travelTimeToHome) {
        this.travelTimeToHome = travelTimeToHome;
    }

    public LocalDateTime getTimeArrive() {
        return timeArrive;
    }

    public void setTimeArrive(LocalDateTime timeArrive) {
        this.timeArrive = timeArrive;
    }
}
