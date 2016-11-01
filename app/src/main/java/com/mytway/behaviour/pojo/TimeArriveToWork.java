package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TimeArriveToWork extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeArriveToWork";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTimeToWork;
    private TravelTime travelTimeToHome;
    private DirectionWay directionWay;
    private LocalDateTime timeArrive;

    @Override
    public String displayMessage() {
        return displayTimeMessage;
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
        Log.i(TAG, "starting processing, current time: " + getCurrentTime());
        if(session.isUserLogged()){
            LocalDateTime timeArriveDateTime;
            LocalDateTime lengthWorkTime = prepareTimeFromStringToCalendar(session.getLengthTimeWork());
            //TimeArriveToWork = currentTime + travelTime

            LocalDateTime timeArriveToWork = addTimeTo(currentTime.getCurrentTime(),
                    travelTimeToWork.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                    travelTimeToWork.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                    travelTimeToWork.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

            String displayMessage = prepareTimeFromLocalDateTimeToString(timeArriveToWork);
            Log.i(TAG, "Time arrive to work: " + displayMessage);

            setTimeArrive(timeArriveToWork);
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

    public TravelTime getTravelTimeToHome() {
        return travelTimeToHome;
    }

    public void setTravelTimeToHome(TravelTime travelTimeToHome) {
        this.travelTimeToHome = travelTimeToHome;
    }

    public DirectionWay getDirectionWay() {
        return directionWay;
    }

    public void setDirectionWay(DirectionWay directionWay) {
        this.directionWay = directionWay;
    }

    public LocalDateTime getTimeArrive() {
        return timeArrive;
    }

    public void setTimeArrive(LocalDateTime timeArrive) {
        this.timeArrive = timeArrive;
    }
}
