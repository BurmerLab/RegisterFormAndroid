package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.widget.RemoteViews;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.SumTimeSpentUserForWork;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

public class TravelToHomeScreen implements Screen{
    //3) laczny czas od wyjscia z pracy do domu = current time - kiedy user wyszedl z domu(param)
    private static final String TAG = "TravelToHomeScreen";

    private TravelTime travelTimeToHome;
    private TimeArriveToHome timeArriveToHome;
    private SumTimeSpentUserForWork sumTimeSpentUserForWork;

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext, Position currentPosition) throws Exception {
        throw new Exception("Not implemented method prepareScreen in " + TAG);
    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context context,
                              Position currentPosition, LocalDateTime whenUserLeaveHome)
            throws Exception {
        //1st Travel time travelTimeToHome
        TravelTime travelTimeToHome = new TravelTime();
        travelTimeToHome.setDirectionWay(directionWay);
        travelTimeToHome.obtainTravelTimeBasedOnDirectonWay(context, currentPosition, session);
        setTravelTimeToHome(travelTimeToHome);

        //2nd TimeArriveToHome = currentTime + travelTimeToHome
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.processTime(context, currentPosition, session);
        setTimeArriveToHome(timeArriveToHome);

        //3rd SumTimeSpentForWork = current time - kiedy user wyszedl z domu(param)
        SumTimeSpentUserForWork sumTimeSpentUserForWork = new SumTimeSpentUserForWork();
        sumTimeSpentUserForWork.processTime(context, currentPosition, session, whenUserLeaveHome);
        setSumTimeSpentUserForWork(sumTimeSpentUserForWork);

    }

    public TravelTime getTravelTimeToHome() {
        return travelTimeToHome;
    }

    public void setTravelTimeToHome(TravelTime travelTimeToHome) {
        this.travelTimeToHome = travelTimeToHome;
    }

    public TimeArriveToHome getTimeArriveToHome() {
        return timeArriveToHome;
    }

    public void setTimeArriveToHome(TimeArriveToHome timeArriveToHome) {
        this.timeArriveToHome = timeArriveToHome;
    }

    public SumTimeSpentUserForWork getSumTimeSpentUserForWork() {
        return sumTimeSpentUserForWork;
    }

    public void setSumTimeSpentUserForWork(SumTimeSpentUserForWork sumTimeSpentUserForWork) {
        this.sumTimeSpentUserForWork = sumTimeSpentUserForWork;
    }
}
