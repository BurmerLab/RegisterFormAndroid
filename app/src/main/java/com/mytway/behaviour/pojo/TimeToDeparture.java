package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Position;
import com.mytway.pojo.TypeWork;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TimeToDeparture extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeToDeparture";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTime;
//    private DirectionWay directionWay;

    @Override
    public String displayMessage() {
        return displayTimeMessage;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) {
        Log.i(TAG, "Starting processing of TimeToDeparture");
        Log.i(TAG, "Current time: " + getCurrentTime());

        if(session.isUserLogged()){
            LocalDateTime timeToDepartureLocalDateTime = new LocalDateTime();

            int userTypeWork = session.getTypeWork();

            if(userTypeWork == TypeWork.STANDARD_TYPE.getStatusCode()){
                Log.i(TAG, "Standard type work");
                if(!session.getStartStandardTimeWork().equals(EMPTY_STRING)){
                    LocalDateTime startStandardTimeWork = prepareTimeFromStringToCalendar(session.getStartStandardTimeWork());

                    // timeToDeparture = (startWorkTime - travelTime) - currentTime
                    LocalDateTime startStandardWorkTimeMinusTravelTime =
                            subtractTimeTo(startStandardTimeWork,
                                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                                    travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

                    timeToDepartureLocalDateTime = subtractTimeTo(startStandardWorkTimeMinusTravelTime,
                            currentTime.getCurrentTime().getHourOfDay(),
                            currentTime.getCurrentTime().getMinuteOfHour(),
                            currentTime.getCurrentTime().getSecondOfMinute());

                }else{
                    Log.i(TAG, "User Standard Type doesn't have startStandardTimeWork");
                }
            }else if(userTypeWork == TypeWork.FLEXIBLE_TYPE.getStatusCode()){
                System.out.println("Flexible type user");

                //timeToDeparture = currentTime + travelTime
                timeToDepartureLocalDateTime =
                        addTimeTo(currentTime.getCurrentTime(),
                                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour());

                Log.i(TAG, "time To Departure of Flexible User is: " + timeToDepartureLocalDateTime);
            }else{
                Log.i(TAG, "User TypeWork not defined, not handled problem");
            }

            String displayMessage = prepareTimeFromLocalDateTimeToString(timeToDepartureLocalDateTime);
            Log.i(TAG, "Time to departure: " + displayMessage);

            setDisplayTimeMessage(displayMessage);

        }else{
            Log.i(TAG, "User is not logged");
        }
    }

    @Override
    public void processTime() throws Exception {
        throw new Exception("Not supported processTime here, in TimeToDeparture");
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
