package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.SumTimeSpentUserForWork;
import com.mytway.behaviour.pojo.TimeArriveToHome;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public class TravelToHomeScreen implements Screen{
    //3) laczny czas od wyjscia z pracy do domu = current time - kiedy user wyszedl z domu(param)
    private static final String TAG = "TravelToHomeScreen";

    private TravelTime travelTimeToHome;
    private TimeArriveToHome timeArriveToHome;
    private SumTimeSpentUserForWork sumTimeSpentUserForWork;

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext,
                              Position currentPosition, LocalDateTime whenUserLeaveHome, TravelTime travelTimeToHome, boolean useEstimate)
            throws Exception {

        //1st Travel time travelTimeToHome
        setTravelTimeToHome(travelTimeToHome);

        //2nd TimeArriveToHome = currentTime + travelTimeToHome
        TimeArriveToHome timeArriveToHome = new TimeArriveToHome();
        timeArriveToHome.setDirectionWay(directionWay);
        timeArriveToHome.processTime(mContext, currentPosition, session);
        setTimeArriveToHome(timeArriveToHome);

        //3rd SumTimeSpentForWork = current time - kiedy user wyszedl z domu(param)
        SumTimeSpentUserForWork sumTimeSpentUserForWork = new SumTimeSpentUserForWork();
        sumTimeSpentUserForWork.processTime(mContext, currentPosition, session, whenUserLeaveHome, useEstimate);
        setSumTimeSpentUserForWork(sumTimeSpentUserForWork);

        //times:
        String time = Calendar.getInstance().getTime().toString();
        view.setTextViewText(R.id.title, "TravelToHomeScreen " + time);
        view.setTextViewText(R.id.firstTimeTextView, this.getTravelTimeToHome().displayMessage());
        view.setTextViewText(R.id.secondTimeTextView, this.getTimeArriveToHome().displayMessage());
        view.setTextViewText(R.id.thirdTimeTextView, this.getSumTimeSpentUserForWork().displayMessage());

        //icons:
        view.setImageViewResource(R.id.firstWidgetImageView, R.drawable.ic_time_in_road_white);
        view.setImageViewResource(R.id.secondWidgetImageView, R.drawable.ic_arrive_to_home_white);
        view.setImageViewResource(R.id.thirdWidgetImageView, R.drawable.ic_time_sum_spent_from_leave_home_white);

        //small titles:
        view.setTextViewText(R.id.firstTimeSmallTitle, mContext.getString(R.string.time_in_road_small_titles));
        view.setTextViewText(R.id.secondTimeSmallTitle, mContext.getString(R.string.time_arrive_to_home_small_titles));
        view.setTextViewText(R.id.thirdTimeSmallTitle, mContext.getString(R.string.time_spent_from_leave_home));

    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext, Position currentPosition,
                              TravelTime travelTime, boolean useEstimate) throws Exception {
        throw new Exception("Not implemented method prepareScreen in " + TAG);
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
