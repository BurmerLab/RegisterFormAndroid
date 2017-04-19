package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.behaviour.pojo.TimeToEndWork;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public class WorkScreen implements Screen {

    private TimeToEndWork timeToEndWork;
    private TravelTime travelTimeOnRoadToHome;
    private TimeArriveToHome timeArriveToHome;

    private static final String TAG = "WorkScreen";

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext,
                              Position currentPosition, LocalDateTime startWorkTime)
            throws Exception {

        Position currentPositionMocked = new Position(50.02999195226106, 19.945344775915146);


        //1) TimeToEndWork = startWorkParameter  + session.workLength
        //(startWorkParameter  + session.workLength)  - currentTime =
        // (12.00 + 8.00) - 14.00 = 16.00 - 14.00 = 02:00 (koniec pracy)
        TimeToEndWork timeToEndWork = new TimeToEndWork();
        timeToEndWork.processTime(mContext, currentPosition, session, startWorkTime);
        setTimeToEndWork(timeToEndWork);

        //2) TravelTimeBackToHome  = travelTime (toHome)
        TravelTime travelTimeOnRoadToHome = new TravelTime();
        travelTimeOnRoadToHome.setDirectionWay(directionWay);
//        travelTimeOnRoadToHome.obtainTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);
        travelTimeOnRoadToHome.obtainTravelTimeBasedOnDirectonWay(mContext, currentPositionMocked, session);
        setTravelTimeOnRoadToHome(travelTimeOnRoadToHome);
        travelTimeOnRoadToHome.processTime();


        //3) TimeArriveToHome - o ktorej dojedziemy do domu (TimeToEndWork + travelTime(ToHome)
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.setTravelTime(travelTimeOnRoadToHome);
        timeArriveToHome.processTime(mContext, currentPosition, session, startWorkTime);
        setTimeArriveToHome(timeArriveToHome);

        //times:
        String time = Calendar.getInstance().getTime().toString();
        view.setTextViewText(R.id.title, "Work Screen " + time);

        view.setTextViewText(R.id.firstTimeTextView, this.getTimeToEndWork().displayMessage());
        view.setTextViewText(R.id.secondTimeTextView, this.getTravelTimeOnRoadToHome().displayMessage());
        view.setTextViewText(R.id.thirdTimeTextView, this.getTimeArriveToHome().displayMessage());

        //icons:
        view.setImageViewResource(R.id.firstImageView, R.drawable.ic_time_to_departure_white);
        view.setImageViewResource(R.id.secondImageView, R.drawable.ic_time_in_road_white);
        view.setImageViewResource(R.id.thirdImageView, R.drawable.ic_arrive_to_home_white);

        //small titles:
        view.setTextViewText(R.id.firstTimeSmallTitle, mContext.getString(R.string.time_to_end_work));
        view.setTextViewText(R.id.secondTimeSmallTitle, mContext.getString(R.string.time_in_road_small_titles));
        view.setTextViewText(R.id.thirdTimeSmallTitle, mContext.getString(R.string.time_arrive_to_home_small_titles));


    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext, Position currentPosition) throws Exception {
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
