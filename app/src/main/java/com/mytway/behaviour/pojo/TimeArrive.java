package com.mytway.behaviour.pojo;

import android.content.Context;

import com.mytway.pojo.Position;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

public class TimeArrive extends AProcessingTime implements IDisplayedTime{

    private static final String TAG = "TimeArrive";
    private static final String EMPTY_STRING = "";
    private String displayTimeMessage;
    private Session session;
    private CurrentTime currentTime = new CurrentTime();
    private TravelTime travelTime;
    private DirectionWay directionWay;

    @Override
    public String displayMessage() {
        return displayTimeMessage;
    }

    @Override
    public void processTime(Context context, Position currentPosition, Session session) {

    }
}
