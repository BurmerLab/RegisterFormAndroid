package com.mytway.behaviour.pojo;

import android.content.Context;

import com.mytway.pojo.Position;

import java.util.Calendar;

public interface IDisplayedTime {

    String displayMessage();

    Calendar processTime(Context context, Position currentPosition);

}
