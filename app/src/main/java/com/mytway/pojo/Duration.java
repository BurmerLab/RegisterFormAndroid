package com.mytway.pojo;

import org.joda.time.LocalDateTime;

public class Duration {

    private static final int ONE_MINUTE_ROUND = 1;
    private String text = "";
    private int value = 0;
    private LocalDateTime durationTime;

    private int hour = 0;
    private int minutes = 0;
    private int seconds = 0;

    public Duration() {
    }

    public Duration(int value) {
        this.value = value;
    }

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
        this.durationTime = prepareDurationValueToLocalDateTime(value);
    }

    public LocalDateTime prepareDurationValueToLocalDateTime(int totalSeconds){
        int hour = totalSeconds / 3600;
        int minuts = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60 ;

        setHour(hour);
        setMinutes(minuts);
        setSeconds(seconds);

        LocalDateTime localDateTime = new LocalDateTime()
            .withHourOfDay(hour)
            .withMinuteOfHour(minuts)
            .withSecondOfMinute(seconds);
        return localDateTime;
    }

    public LocalDateTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(LocalDateTime durationTime) {
        this.durationTime = durationTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
