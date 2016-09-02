package com.mytway.utility;

import java.util.Calendar;

public class CurrentTime {

    private Calendar currentTime;

    public CurrentTime() {
        this.obtainCurrentTime();
    }

    public Calendar getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Calendar currentTime) {
        this.currentTime = currentTime;
    }

    public void obtainCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        setCurrentTime(calendar);
    }
}
