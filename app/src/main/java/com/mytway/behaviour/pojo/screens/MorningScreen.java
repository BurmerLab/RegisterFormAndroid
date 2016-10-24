package com.mytway.behaviour.pojo.screens;

import com.mytway.behaviour.pojo.TimeArrive;
import com.mytway.behaviour.pojo.TimeInRoad;
import com.mytway.behaviour.pojo.TimeToDeparture;

public class MorningScreen {

    private TimeToDeparture timeToDeparture;
    private TimeInRoad timeInRoad;
    private TimeArrive timeArrive;

    public void prepareMorningScreen(){

    }

    public TimeToDeparture getTimeToDeparture() {
        return timeToDeparture;
    }

    public void setTimeToDeparture(TimeToDeparture timeToDeparture) {
        this.timeToDeparture = timeToDeparture;
    }

    public TimeInRoad getTimeInRoad() {
        return timeInRoad;
    }

    public void setTimeInRoad(TimeInRoad timeInRoad) {
        this.timeInRoad = timeInRoad;
    }

    public TimeArrive getTimeArrive() {
        return timeArrive;
    }

    public void setTimeArrive(TimeArrive timeArrive) {
        this.timeArrive = timeArrive;
    }
}
