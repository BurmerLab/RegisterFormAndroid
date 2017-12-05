package com.mytway.behaviour.pojo;

import android.content.Context;

import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public interface IDisplayedTime {

    void processTime(Context context, Position currentPosition, Session session) throws Exception;
    void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime, boolean useEstimate) throws Exception;
    void processTime() throws Exception;

    //Full time, from current by travelToWork by WorkLength to travelBackToHome
    void fullProcessTime(Context context, Position currentPosition, Session session, boolean useEstimate) throws Exception;

    String displayMessage();

}
