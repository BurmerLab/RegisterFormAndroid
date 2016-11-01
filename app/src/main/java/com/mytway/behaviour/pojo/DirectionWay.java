package com.mytway.behaviour.pojo;

import com.mytway.pojo.Position;
import com.mytway.utility.Session;

public class DirectionWay {

    private boolean wayToWork;
    private boolean wayToHome;

    public DirectionWay() {
    }

    public DirectionWay(boolean wayToWork, boolean wayToHome) {
        this.wayToWork = wayToWork;
        this.wayToHome = wayToHome;
    }

    public void decideWhichDirectionIs(Position currentPosition, Session session){
        // algorytm A* do definiowania czy user sie zbliza do pracy czy do domu


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
