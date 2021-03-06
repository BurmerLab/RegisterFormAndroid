package com.mytway.behaviour.pojo.screens;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.behaviour.pojo.TimeInRoad;
import com.mytway.behaviour.pojo.TimeToDeparture;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.DisplayMessageStyle;
import com.mytway.utility.GraphUtil;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public class MorningScreen implements Screen {

    private static final String TAG = "MorningScreen";
    private TimeToDeparture timeToDeparture;
    private TimeInRoad timeInRoad;
    private TimeArriveToHome timeArriveToHome;

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session,
                              Context mContext, Position currentPosition, TravelTime travelTime, boolean useEstimate)
                              throws Exception {
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
        timeInRoad.processTime();
        setTimeInRoad(timeInRoad);
        Log.i(TAG, "timeInRoad: " + timeInRoad.displayMessage());

        //3rd Time Arrive Time (When We will come back)
        //ArriveTime = CurrentTime + TravelTime (toWork) + workLength(from session) + travelTime(back)
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.setTravelTimeToWork(travelTime);//travel time To work
        timeArriveToHome.setSession(session);
        timeArriveToHome.setTravelTimeToHome(travelTime);//travel time to home
        timeArriveToHome.fullProcessTime(mContext, currentPosition, session, useEstimate);
        setTimeArriveToHome(timeArriveToHome);
        Log.i(TAG, "timeArriveToHome: " + timeArriveToHome.displayMessage());

        ObjectAnimator flip = ObjectAnimator.ofFloat(R.id.firstTimeTextView, "rotationX", 0f, 180f);
        flip.setDuration(1000);
        flip.start();

        String time = Calendar.getInstance().getTime().toString();

        //times:
        view.setTextViewText(R.id.title, "Morning Screen: " + time);

        DisplayMessageStyle.displayLeftTime(view, R.id.firstTimeTextView, this.getTimeToDeparture().displayMessage());
        DisplayMessageStyle.displayLeftTime(view, R.id.secondTimeTextView, this.getTimeInRoad().displayMessage());
        view.setTextViewText(R.id.thirdTimeTextView, this.getTimeArriveToHome().displayMessage());

        //progressBars
        int timeToLeftHomeInSeconds = timeToDeparture.obtainTimeInSeconds(timeToDeparture.getTimeToDeparture());
//        int timeToLeftHomeInSeconds = 1200;
        recalibrateTimeToLeftForDisplayingInProgressBar(view, timeToLeftHomeInSeconds, mContext);

        view.setViewVisibility(R.id.secondProgressBar, View.INVISIBLE);
        view.setViewVisibility(R.id.thirdProgressBar, View.INVISIBLE);

        //icons:
//        view.setInt(R.id.contentContainer, "setBackgroundColor", 0x00E676);

        view.setImageViewResource(R.id.firstWidgetImageView, R.drawable.ic_time_to_departure_white);
        view.setImageViewResource(R.id.secondWidgetImageView, R.drawable.ic_time_in_road_white);
        view.setImageViewResource(R.id.thirdWidgetImageView, R.drawable.ic_arrive_to_home_white);

        //small titles:
        view.setTextViewText(R.id.firstTimeSmallTitle, mContext.getString(R.string.time_to_departure_small_titles));
        view.setTextViewText(R.id.secondTimeSmallTitle, mContext.getString(R.string.time_in_road_small_titles));
        view.setTextViewText(R.id.thirdTimeSmallTitle, mContext.getString(R.string.time_arrive_to_home_small_titles));


    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext, Position currentPosition, LocalDateTime startWorkTime, TravelTime travelTime, boolean useEstimate) throws Exception {
        throw new Exception("Not implemented method prepareScreen in " + TAG);
    }

    public void recalibrateTimeToLeftForDisplayingInProgressBar(RemoteViews view, int timeToLeftHomeInSeconds, Context context) {
        if(timeToLeftHomeInSeconds < PropertiesValues.SECONDS_IN_FOUR_HOURS){
            int graphicalRepresentTimeToLeftHome  = GraphUtil.calculateProgressBar(PropertiesValues.SECONDS_IN_FOUR_HOURS, timeToLeftHomeInSeconds);
            view.setProgressBar(R.id.firstProgressBar, PropertiesValues.SECONDS_IN_FOUR_HOURS, graphicalRepresentTimeToLeftHome, false);

        }else{
            view.setViewVisibility(R.id.firstProgressBar, View.INVISIBLE);
        }
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
