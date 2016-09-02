package com.mytway.utility;

import java.util.Calendar;

public class TimeInWay {

    private Calendar timeInWay;

    public Calendar getTimeInWay() {
        return timeInWay;
    }

    public void setTimeInWay(Calendar timeInWay) {
        this.timeInWay = timeInWay;
    }

    public void obtainTimeInWay(){
        Session session = new Session();
        //todo: Add obtaining timeInWay from:
        //1) google trace/trasy
        //2) from database
        //3) example i.e: 10min.

    }
}
