package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TimeToEndWork extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeToEndWork";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTimeToWork;
    private TravelTime travelTimeToHome;
    private DirectionWay directionWay;
    private LocalDateTime timeToEndWork;

    //1) TimeToEndWork = startWorkParameter  + session.workLength
    //(startWorkParameter  + session.workLength)  - currentTime = (12.00 + 8.00) - 14.00 = 16.00 - 14.00 = 02:00 (koniec pracy)

    //2) TravelTimeBackToHome  = travelTime (toHome)

    //3) TimeArriveToHome - o ktorej dojedziemy do domu (TimeToEndWork + travelTime(ToHome)

    @Override
    public void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime) throws Exception {
        Log.i(TAG, "Starting processing of TimeToEndWork");
        Log.i(TAG, "TimeToEndWork current time: " + getCurrentTime());
        if(session.isUserLogged()){
            LocalDateTime lenghtWorkTime = prepareTimeFromStringToCalendar(session.getLengthTimeWork());
            //TimeToEndWork = startWorkParameter  + session.workLength

            LocalDateTime timeToEndWork = addTimeTo(startWorkTime,
                    lenghtWorkTime.getHourOfDay(),
                    lenghtWorkTime.getMinuteOfHour(),
                    lenghtWorkTime.getSecondOfMinute());

//mialem do zrobienia ile jest godzin, problem jest w tym ze po dodaniu:
            //2016-01-01 22:00 + 08:00 to wychodzi na 2016-01-02 04:00 czyli zwraca 4 godizny a nie 8

//            LocalDateTime finaltimeToEndWork = dirrefenceHoursBetweenTimes();
            String displayMessage = prepareTimeFromLocalDateTimeToString(timeToEndWork);
            Log.i(TAG, "TimeToEndWork: " + displayMessage);

            setTimeToEndWork(timeToEndWork);
            setDisplayTimeMessage(displayMessage);
        }
    }

    @Override
    public String displayMessage() {
        return displayTimeMessage;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in TimeToEndWork");
    }

    @Override
    public void processTime() throws Exception {
        throw new Exception("Not supported processTime here, in TimeToEndWork");
    }

    @Override
    public void fullProcessTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
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

    public LocalDateTime getTimeToEndWork() {
        return timeToEndWork;
    }

    public void setTimeToEndWork(LocalDateTime timeToEndWork) {
        this.timeToEndWork = timeToEndWork;
    }
}
