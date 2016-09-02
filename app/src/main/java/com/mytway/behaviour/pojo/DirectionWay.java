package com.mytway.behaviour.pojo;

public class DirectionWay {

    private boolean wayToWork;
    private boolean wayToHome;

    public DirectionWay() {
    }

    public DirectionWay(boolean wayToWork, boolean wayToHome) {
        this.wayToWork = wayToWork;
        this.wayToHome = wayToHome;
    }

    public boolean isWayToWork() {
        return wayToWork;
    }

    public void setWayToWork(boolean wayToWork) {
        this.wayToWork = wayToWork;
    }

    public boolean isWayToHome() {
        return wayToHome;
    }

    public void setWayToHome(boolean wayToHome) {
        this.wayToHome = wayToHome;
    }
}
