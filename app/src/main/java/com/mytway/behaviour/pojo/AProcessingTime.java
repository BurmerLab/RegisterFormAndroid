package com.mytway.behaviour.pojo;

import com.mytway.properties.PropertiesValues;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class AProcessingTime {

    private static final int ZERO = 0;

    public AProcessingTime() {
    }

    public LocalDateTime dateToLocalDateTime(String timeString){
        LocalDateTime localDate = LocalDateTime.parse(timeString, DateTimeFormat.forPattern("HH:mm"));
        return localDate;
    }

    public LocalDateTime prepareTimeFromStringToCalendar(String startStandardTimeWork) {

        LocalDateTime localDateTime = dateToLocalDateTime(startStandardTimeWork);
        return localDateTime;
    }

    public String prepareTimeFromLocalDateTimeToString(LocalDateTime localDateTime){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        //todo: Zrobic
//        String formattedString = formatter.format(localDateTime);
        String formattedString = localDateTime.toString("HH:mm");
        return formattedString;
    }

    public int dirrefenceHoursBetweenTimes(DateTime firstDate, DateTime secondDate){
        //jodaTime:
        int differenceHour = Hours.hoursBetween(firstDate, secondDate).getHours();
        System.out.println("Difference in hours is ="+ differenceHour);
        return differenceHour;
    }
//
    public LocalDateTime addTimeTo(LocalDateTime basedTime, int hourToAdd, int minutesToAdd, int secondsToAdd){
        LocalDateTime timeAfterSum = new LocalDateTime()
            .withYear(basedTime.getYear())
            .withMonthOfYear(basedTime.getMonthOfYear())
            .withDayOfMonth(basedTime.getDayOfMonth())
            .withHourOfDay(basedTime.getHourOfDay()).plusHours(hourToAdd)
            .withMinuteOfHour(basedTime.getMinuteOfHour()).plusMinutes(minutesToAdd)
            .withSecondOfMinute(basedTime.getSecondOfMinute()).plusSeconds(secondsToAdd);

        return timeAfterSum;
    }
//6.00 - 13:00 = 17:00
    public LocalDateTime subtractTimeTo(LocalDateTime basedTime, int hourToAdd, int minutesToAdd, int secondsToAdd){
        LocalDateTime timeAfterSubtract = new LocalDateTime()
                .withYear(basedTime.getYear())
                .withMonthOfYear(basedTime.getMonthOfYear())
                .withDayOfMonth(basedTime.getDayOfMonth())
                .withHourOfDay(basedTime.getHourOfDay()).minusHours(hourToAdd)
                .withMinuteOfHour(basedTime.getMinuteOfHour()).minusMinutes(minutesToAdd)
                .withSecondOfMinute(basedTime.getSecondOfMinute()).minusSeconds(secondsToAdd);
        return timeAfterSubtract;
    }

    public String convertTimeToTimeLeftFormat(LocalDateTime timeToConvert){
        StringBuilder timeLeft = new StringBuilder();

        if(timeToConvert.getHourOfDay() != ZERO){
            timeLeft.append(timeToConvert.getHourOfDay());
            timeLeft.append(PropertiesValues.HOUR_CHARACTER);
        }

        if(timeToConvert.getMinuteOfHour() != ZERO){
            timeLeft.append(timeToConvert.getMinuteOfHour());
            timeLeft.append(PropertiesValues.MINUTES_CHARACTER);
        }

        return timeLeft.toString();
    }

}
