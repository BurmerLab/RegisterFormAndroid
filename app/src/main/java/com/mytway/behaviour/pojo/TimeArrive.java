package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.pojo.TypeWork;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TimeArrive extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeArrive";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTimeToWork;
    private TravelTime travelTimeToHome;
    private DirectionWay directionWay;

    @Override
    public void processTime() throws Exception{

        Log.i(TAG, "Starting processing of TimeToDeparture");
        Log.i(TAG, "Current time: " + getCurrentTime());
        if(session.isUserLogged()){
            LocalDateTime timeArriveDateTime;
            LocalDateTime lenghtWorkTime = prepareTimeFromStringToCalendar(session.getLengthTimeWork());
            //TimeArrive = CurrentTime + TravelTime (toWork) + workLength + travelTimeToWork (back)

            LocalDateTime currentTimePlusTravelTime = addTimeTo(currentTime.getCurrentTime(),
                    travelTimeToWork.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                    travelTimeToWork.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                    travelTimeToWork.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

            LocalDateTime currentPlusTravelPlusWorkLenghtTime =
                    addTimeTo(currentTimePlusTravelTime,
                            lenghtWorkTime.getHourOfDay(),
                            lenghtWorkTime.getMinuteOfHour(),
                            lenghtWorkTime.getSecondOfMinute());

            timeArriveDateTime = addTimeTo(currentPlusTravelPlusWorkLenghtTime,
                    travelTimeToHome.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                    travelTimeToHome.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                    travelTimeToHome.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

            String displayMessage = prepareTimeFromLocalDateTimeToString(timeArriveDateTime);
            Log.i(TAG, "Time arrive: " + displayMessage);
            setDisplayTimeMessage(displayMessage);
        }
    }

    @Override
    public String displayMessage() {
        return displayTimeMessage;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in TimeArrive");
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
}
