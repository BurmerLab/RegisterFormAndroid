package com.mytway.behaviour.pojo;

import android.content.Context;

import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import java.util.Calendar;

public interface IDisplayedTime {

    String displayMessage();

    void processTime(Context context, Position currentPosition, Session session);

}
