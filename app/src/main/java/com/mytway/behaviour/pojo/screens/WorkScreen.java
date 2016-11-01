package com.mytway.behaviour.pojo.screens;

import android.content.Context;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.behaviour.pojo.TimeToEndWork;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class WorkScreen implements Screen {

    private TimeToEndWork timeToEndWork;
    private TravelTime travelTimeOnRoadToHome;
    private TimeArriveToHome timeArriveToHome;

    private static final String TAG = "WorkScreen";

    @Override
    public void prepareScreen(DirectionWay directionWay, Session session, Context mContext,
                              Position currentPosition, LocalDateTime startWorkTime)
            throws Exception {

        //1) TimeToEndWork = startWorkParameter  + session.workLength
        //(startWorkParameter  + session.workLength)  - currentTime =
        // (12.00 + 8.00) - 14.00 = 16.00 - 14.00 = 02:00 (koniec pracy)
        TimeToEndWork timeToEndWork = new TimeToEndWork();
        timeToEndWork.processTime(mContext, currentPosition, session, startWorkTime);
        setTimeToEndWork(timeToEndWork);

        //2) TravelTimeBackToHome  = travelTime (toHome)
        TravelTime travelTimeOnRoadToHome = new TravelTime();
        travelTimeOnRoadToHome.setDirectionWay(directionWay);
        travelTimeOnRoadToHome.obtainTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);
        setTravelTimeOnRoadToHome(travelTimeOnRoadToHome);

        //3) TimeArriveToHome - o ktorej dojedziemy do domu (TimeToEndWork + travelTime(ToHome)
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.processTime(mContext, currentPosition, session, startWorkTime);
        setTimeArriveToHome(timeArriveToHome);
    }

    @Override
    public void prepareScreen(DirectionWay directionWay, Session session, Context mContext, Position currentPosition) throws Exception {
        throw new Exception("Not implemented method prepareScreen in " + TAG);
    }

    public TimeToEndWork getTimeToEndWork() {
        return timeToEndWork;
    }

    public void setTimeToEndWork(TimeToEndWork timeToEndWork) {
        this.timeToEndWork = timeToEndWork;
    }

    public TravelTime getTravelTimeOnRoadToHome() {
        return travelTimeOnRoadToHome;
    }

    public void setTravelTimeOnRoadToHome(TravelTime travelTimeOnRoadToHome) {
        this.travelTimeOnRoadToHome = travelTimeOnRoadToHome;
    }

    public TimeArriveToHome getTimeArriveToHome() {
        return timeArriveToHome;
    }

    public void setTimeArriveToHome(TimeArriveToHome timeArriveToHome) {
        this.timeArriveToHome = timeArriveToHome;
    }
}
