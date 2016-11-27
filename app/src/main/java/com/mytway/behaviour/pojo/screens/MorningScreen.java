package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.behaviour.pojo.TimeInRoad;
import com.mytway.behaviour.pojo.TimeToDeparture;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class MorningScreen implements Screen {

    private static final String TAG = "MorningScreen";

    private TimeToDeparture timeToDeparture;
    private TimeInRoad timeInRoad;
    private TimeArriveToHome timeArriveToHome;

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session,
                              Context mContext, Position currentPosition)
                              throws Exception {
        TravelTime travelTime = new TravelTime();
        travelTime.setDirectionWay(directionWay);
        travelTime.obtainTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);

        //1st Time- time to departure
        TimeToDeparture timeToDeparture = new TimeToDeparture();
        timeToDeparture.setSession(session);
        timeToDeparture.setTravelTime(travelTime);
        timeToDeparture.processTime(mContext, currentPosition, session);
        setTimeToDeparture(timeToDeparture);
        Log.i(TAG, "Time to departure: " + timeToDeparture.displayMessage());

        //2nd Time - Time in road
        TimeInRoad timeInRoad = new TimeInRoad();
        timeInRoad.setTimeInRoad(timeToDeparture);
        LocalDateTime timeInRoadDateTime = timeToDeparture.getTravelTime().getGoogleMapsDirectionJson().getLegs().getDuration().getDurationTime();
        setTimeInRoad(timeInRoad);
        Log.i(TAG, "timeInRoad: " + timeInRoad.displayMessage());

        //3rd Time Arrive Time (When We will come back)
        //ArriveTime = CurrentTime + TravelTime (toWork) + workLength(from session) + travelTime(back)
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.setTravelTimeToWork(travelTime);//travel time To work
        timeArriveToHome.setSession(session);
        timeArriveToHome.setTravelTimeToWork(travelTime);//travel time to home
        timeArriveToHome.fullProcessTime(mContext, currentPosition, session);
        setTimeArriveToHome(timeArriveToHome);
        Log.i(TAG, "timeArriveToHome: " + timeArriveToHome.displayMessage());
    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext, Position currentPosition, LocalDateTime startWorkTime) throws Exception {

    }

    public TimeToDeparture getTimeToDeparture() {
        return timeToDeparture;
    }

    public void setTimeToDeparture(TimeToDeparture timeToDeparture) {
        this.timeToDeparture = timeToDeparture;
    }

    public TimeInRoad getTimeInRoad() {
        return timeInRoad;
    }

    public void setTimeInRoad(TimeInRoad timeInRoad) {
        this.timeInRoad = timeInRoad;
    }

    public TimeArriveToHome getTimeArriveToHome() {
        return timeArriveToHome;
    }

    public void setTimeArriveToHome(TimeArriveToHome timeArriveToHome) {
        this.timeArriveToHome = timeArriveToHome;
    }
}
