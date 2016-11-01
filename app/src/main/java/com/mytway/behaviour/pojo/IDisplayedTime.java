package com.mytway.behaviour.pojo;

import android.content.Context;

import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public interface IDisplayedTime {

    void processTime(Context context, Position currentPosition, Session session) throws Exception;
    void processTime(Context context, Position currentPosition, Session session, LocalDateTime startWorkTime) throws Exception;
    void processTime() throws Exception;
    String displayMessage();

}
