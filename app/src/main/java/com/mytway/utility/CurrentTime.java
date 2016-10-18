package com.mytway.utility;

import com.mytway.properties.PropertiesValues;

import org.joda.time.LocalDateTime;


public class CurrentTime {

    private LocalDateTime currentTime;
    private LocalDateTime mockedCurrentTime = null;

    public CurrentTime() {

    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public void obtainCurrentTime(){
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            setMockedTime();
        }else{
            LocalDateTime currentTime = LocalDateTime.now();
            setCurrentTime(currentTime);
        }

    }

    public void setMockedTime(){
        //2016-03-15 05:30:00
        LocalDateTime mockedTime = new LocalDateTime()
            .withYear(mockedCurrentTime.getYear())
            .withMonthOfYear(mockedCurrentTime.getMonthOfYear())
            .withDayOfMonth(mockedCurrentTime.getDayOfMonth())
            .withHourOfDay(mockedCurrentTime.getHourOfDay())
            .withMinuteOfHour(mockedCurrentTime.getMinuteOfHour());

        setCurrentTime(mockedTime);
    }

    public LocalDateTime getMockedCurrentTime() {
        return mockedCurrentTime;
    }

    public void setMockedCurrentTime(LocalDateTime mockedCurrentTime) {
        this.mockedCurrentTime = mockedCurrentTime;
    }
}
