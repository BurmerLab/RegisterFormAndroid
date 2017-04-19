package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class SumTimeSpentUserForWork extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "SumTimeSpentUserForWork";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTimeToWork;
    private TravelTime travelTimeToHome;
    private DirectionWay directionWay;
    private LocalDateTime sumTimeSpentUserForWork;

    @Override
    public void processTime(Context context, Position currentPosition, Session session) throws Exception {
        throw new Exception("Not supported processTime here, in " + TAG);
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session,
                            LocalDateTime startWorkTime) throws Exception {
        Log.i(TAG, "Starting processing of SumTimeSpentUserForWork");
        Log.i(TAG, "Current time: " + getCurrentTime());
        if(session.isUserLogged()){
            //current time - kiedy user wyszedl z domu(param)
            LocalDateTime sumTimeSpentUserForWork =
                    subtractTimeTo(currentTime.getCurrentTime(),
                            startWorkTime.getHourOfDay(),
                            startWorkTime.getMinuteOfHour(),
                            startWorkTime.getSecondOfMinute());

            String displayMessage = prepareTimeFromLocalDateTimeToString(sumTimeSpentUserForWork);
            Log.i(TAG, "SumTimeSpentUserForWork: " + displayMessage);

            String sumTimeSpentUserForWorkInTimeLeftFormat = convertTimeToTimeLeftFormat(sumTimeSpentUserForWork);

            setSumTimeSpentUserForWork(sumTimeSpentUserForWork);
            setDisplayTimeMessage(sumTimeSpentUserForWorkInTimeLeftFormat);
        }
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

    public CurrentTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(CurrentTime currentTime) {
        this.currentTime = currentTime;
    }

    public LocalDateTime getSumTimeSpentUserForWork() {
        return sumTimeSpentUserForWork;
    }

    public void setSumTimeSpentUserForWork(LocalDateTime sumTimeSpentUserForWork) {
        this.sumTimeSpentUserForWork = sumTimeSpentUserForWork;
    }

    public String getDisplayTimeMessage() {
        return displayTimeMessage;
    }

    public void setDisplayTimeMessage(String displayTimeMessage) {
        this.displayTimeMessage = displayTimeMessage;
    }
}
