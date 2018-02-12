package com.mytway.behaviour.pojo;


import org.joda.time.LocalDateTime;

public class UserDailyTimes {

    private LocalDateTime currentTime = new LocalDateTime();
    private LocalDateTime leaveHomeTime;
    private LocalDateTime startWorkTime;
    private LocalDateTime endWorkTime;
    private LocalDateTime leaveWorkTime;
    private LocalDateTime arriveToHomeTime;

    //sum time from leave home to arrive to home
    private LocalDateTime sumTimeSpentForWork;

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public LocalDateTime getLeaveHomeTime() {
        return leaveHomeTime;
    }

    public void setLeaveHomeTime(LocalDateTime leaveHomeTime) {
        this.leaveHomeTime = leaveHomeTime;
    }

    public LocalDateTime getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(LocalDateTime startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public LocalDateTime getEndWorkTime() {
        return endWorkTime;
    }

    public void setEndWorkTime(LocalDateTime endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    public LocalDateTime getLeaveWorkTime() {
        return leaveWorkTime;
    }

    public void setLeaveWorkTime(LocalDateTime leaveWorkTime) {
        this.leaveWorkTime = leaveWorkTime;
    }

    public LocalDateTime getArriveToHomeTime() {
        return arriveToHomeTime;
    }

    public void setArriveToHomeTime(LocalDateTime arriveToHomeTime) {
        this.arriveToHomeTime = arriveToHomeTime;
    }

    public LocalDateTime getSumTimeSpentForWork() {
        return AProcessingTime.subtractTimeTo(arriveToHomeTime,
                leaveHomeTime.getHourOfDay(),
                leaveHomeTime.getMinuteOfHour(),
                leaveHomeTime.getSecondOfMinute());

    }

    public void setSumTimeSpentForWork(LocalDateTime sumTimeSpentForWork) {
        this.sumTimeSpentForWork = sumTimeSpentForWork;
    }
}
