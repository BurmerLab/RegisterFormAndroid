package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.behaviour.pojo.TimeArriveToWork;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TravelToWorkScreen implements Screen{

    private static final String TAG = "TravelToWorkScreen";

    private TravelTime travelTime;
    private TimeArriveToWork timeArriveToWork;
    private TimeArriveToHome timeArriveToHome;

    //1)How much Time to arrive To Work = travelTime (01:12)
    //2)TimeArriveToWork = currentTime + travelTime (12:30)
    //3)TimeArriveToHome = currentTime + travelTime + session.workLength + travelBackToHome (21.00)

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session,
                              Context mContext, Position currentPosition) throws Exception {

        //1st Time to work
        TravelTime travelTime = new TravelTime();
        travelTime.setDirectionWay(directionWay);
        travelTime.obtainTravelTimeBasedOnDirectonWay(mContext, currentPosition, session);
        setTravelTime(travelTime);

        //2nd TimeArriveToWork = currentTime + travelTime
        TimeArriveToWork timeArriveToWork = new TimeArriveToWork();
        timeArriveToWork.processTime();
        setTimeArriveToWork(timeArriveToWork);

        //3rd timeArriveToHome
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.processTime();
        setTimeArriveToHome(timeArriveToHome);

        view.setTextViewText(R.id.title, "TravelToWorkScreen");
        view.setTextViewText(R.id.firstTimeTextView, this.getTravelTime().displayMessage());
        view.setTextViewText(R.id.secondTimeTextView, this.getTimeArriveToWork().displayMessage());
        view.setTextViewText(R.id.thirdTimeTextView, this.getTimeArriveToHome().displayMessage());
    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext,
                              Position currentPosition, LocalDateTime startWorkTime) throws Exception {
        throw new Exception("Not implemented method prepareScreen in " + TAG);
    }

    public TravelTime getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(TravelTime travelTime) {
        this.travelTime = travelTime;
    }

    public TimeArriveToWork getTimeArriveToWork() {
        return timeArriveToWork;
    }

    public void setTimeArriveToWork(TimeArriveToWork timeArriveToWork) {
        this.timeArriveToWork = timeArriveToWork;
    }

    public TimeArriveToHome getTimeArriveToHome() {
        return timeArriveToHome;
    }

    public void setTimeArriveToHome(TimeArriveToHome timeArriveToHome) {
        this.timeArriveToHome = timeArriveToHome;
    }
}
