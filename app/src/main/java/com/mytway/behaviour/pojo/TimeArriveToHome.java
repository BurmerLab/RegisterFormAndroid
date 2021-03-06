package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.GoogleMapsDirectionJson;
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
    private TravelTime travelTime;
    private DirectionWay directionWay;
    private LocalDateTime timeArrive;

    public TimeArriveToHome() {
    }

    public TimeArriveToHome(TravelTime travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session,
                            LocalDateTime startWorkTime, boolean useEstimate) throws Exception{
        Log.i(TAG, "Starting processing of TimeArriveToHome");
        Log.i(TAG, "Current time: " + getCurrentTime());
        if(session.isUserLogged()){
            //TimeArriveToHome - (TimeToEndWork + travelTime(ToHome)
            LocalDateTime lenghtWorkTime = prepareTimeFromStringToCalendar(session.getLengthTimeWork());

            //startWorkTime + lengthWorkTime + travelTime
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

    //Full time, from current by travelToWork by WorkLength to travelBackToHome
    @Override
    public void fullProcessTime(Context context, Position currentPosition, Session session, boolean useEstimate) throws Exception {
//      TimeArriveToHome = CurrentTime + TravelTimeToWork (toWork) + workLength + travelTimeToHome (back)
        if(session.isUserLogged()){
            LocalDateTime lenghtWorkTime = prepareTimeFromStringToCalendar(session.getLengthTimeWork());

            GoogleMapsDirectionJson currentTravelTimeToWork =
                    travelTimeToWork.obtainCurrentTravelTimeToWork(context, currentPosition, session, useEstimate);

            //Travel Time took from session to catch full time
            //todo: replace currentTravelTimeToHome by full travel Time from session
            // BRAC CZAS CALEJ TRASY POWROTNEJ A NIE TAK JAK TERAZ ZE OBLICZA DO MIEJSCVA GDZIE SIE ZNAJDUJE
            GoogleMapsDirectionJson currentTravelTimeToHome =
                    travelTimeToHome.obtainCurrentTravelTimeToHome(context, currentPosition, session, useEstimate);

            LocalDateTime currentAndTravelTime = addTimeTo(currentTime.getCurrentTime(),
                    currentTravelTimeToWork.getLegs().getDuration().getHour(),
                    currentTravelTimeToWork.getLegs().getDuration().getMinutes(),
                    currentTravelTimeToWork.getLegs().getDuration().getSeconds());

            LocalDateTime currentAndTravelAndWorkLenghtTime = addTimeTo(currentAndTravelTime,
                    lenghtWorkTime.getHourOfDay(),
                    lenghtWorkTime.getMinuteOfHour(),
                    lenghtWorkTime.getSecondOfMinute());

            LocalDateTime timeArriveToHome = addTimeTo(currentAndTravelAndWorkLenghtTime,
                    currentTravelTimeToHome.getLegs().getDuration().getHour(),
                    currentTravelTimeToHome.getLegs().getDuration().getMinutes(),
                    currentTravelTimeToHome.getLegs().getDuration().getSeconds());


            String displayMessage = prepareTimeFromLocalDateTimeToString(timeArriveToHome);
            Log.i(TAG, "TimeArriveToHome: " + timeArriveToHome);

            setTimeArrive(timeArriveToHome);
            setDisplayTimeMessage(displayMessage);
        }
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
            travelTime.obtainEstimationByStaticTravelTimeBasedOnDirectonWay(context, currentPosition, session);
            //todo: commented request for google travel time:
//            travelTime.obtainTravelTimeBasedOnDirectonWay(context, currentPosition, session);
//            --------

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

    public TravelTime getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(TravelTime travelTime) {
        this.travelTime = travelTime;
    }
}
