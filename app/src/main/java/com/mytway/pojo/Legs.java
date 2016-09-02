package com.mytway.pojo;

public class Legs {

    private Distance distance = null;
    private Duration duration = null;

    public Legs(Distance distance, Duration duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public Legs() {
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
