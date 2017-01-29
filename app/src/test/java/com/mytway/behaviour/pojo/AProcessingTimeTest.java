package com.mytway.behaviour.pojo;

import com.mytway.utility.CurrentTime;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

public class AProcessingTimeTest {

    private TimeToDeparture timeToDeparture = new TimeToDeparture();

    @Test
    public void testDirrefenceHoursBetweenTimes() throws Exception {
        Calendar calendarFirst = Calendar.getInstance();
        calendarFirst.set(Calendar.YEAR, 2016);
        calendarFirst.set(Calendar.MONTH, Calendar.JUNE);
        calendarFirst.set(Calendar.DATE, 10);
        calendarFirst.set(Calendar.HOUR_OF_DAY, 5);

        Calendar calendarSecond = Calendar.getInstance();
        calendarSecond.set(Calendar.YEAR, 2016);
        calendarSecond.set(Calendar.MONTH, Calendar.JUNE);
        calendarSecond.set(Calendar.DATE, 10);
        calendarSecond.set(Calendar.HOUR_OF_DAY, 10);

        DateTime firstDate = new DateTime(calendarFirst.getTime());
        DateTime secondDate = new DateTime(calendarSecond.getTime());

        int differenceInHours = timeToDeparture.dirrefenceHoursBetweenTimes(firstDate, secondDate);

        System.out.println(" difference in hour is: " + differenceInHours);
        assertEquals(5, differenceInHours);
    }


    // 14 March 22:00:00 - 15 March 03:00:00 = 5h
    @Test
    public void testDifferenceFromNet(){
        DateTime startDateTime = new DateTime()
            .withYear(2014)
            .withMonthOfYear(3)
            .withDayOfMonth(14)
            .withHourOfDay(22)
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)
            .withZone(DateTimeZone.forID("US/Eastern"));

        DateTime endDateTime = new DateTime()
            .withYear(2014)
            .withMonthOfYear(3)
            .withDayOfMonth(15)
            .withHourOfDay(3)
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)
            .withZone(DateTimeZone.forID("US/Eastern"));

        System.out.println("Expected 5, got: " + timeToDeparture.dirrefenceHoursBetweenTimes(startDateTime, endDateTime));

        assertEquals(5, Hours.hoursBetween(startDateTime, endDateTime).getHours());

        DateTime startUtc = startDateTime.withZoneRetainFields(DateTimeZone.UTC);
        DateTime endUtc = endDateTime.withZoneRetainFields(DateTimeZone.UTC);

        assertEquals(5, Hours.hoursBetween(startUtc, endUtc).getHours());
    }

    //03:21:00 + 02:40:00 = 6:01:01
    @Test
    public void testAddTimeToExisted(){
        LocalDateTime basedTime = new LocalDateTime()
            .withYear(2014)
            .withMonthOfYear(3)
            .withDayOfMonth(15)
            .withHourOfDay(3)
            .withMinuteOfHour(21)
            .withSecondOfMinute(1);

        LocalDateTime result = timeToDeparture.addTimeTo(basedTime, 2, 40, 0);
        System.out.println("Result hour: " + result.getHourOfDay());
        System.out.println("Result minutes: " + result.getMinuteOfHour());
        System.out.println("Result seconds: " + result.getSecondOfMinute());
        assertEquals(6, result.getHourOfDay());
        assertEquals(1, result.getMinuteOfHour());
        assertEquals(1, result.getSecondOfMinute());
    }

    //03:40:00 - 02:20:00 = 1:20:00
    @Test
    public void testSubstractTimeToExisted() {
        LocalDateTime basedTime = new LocalDateTime()
                .withYear(2014)
                .withMonthOfYear(3)
                .withDayOfMonth(15)
                .withHourOfDay(3)
                .withMinuteOfHour(40)
                .withSecondOfMinute(1);
//        only for avoid nullpointer for mocked time
        CurrentTime currentTime = new CurrentTime();
        LocalDateTime mockedCurrentTime =
                LocalDateTime.parse("3/15/2016T0530", DateTimeFormat.forPattern("MM/dd/yyyy'T'HHmm"));
        currentTime.setMockedCurrentTime(mockedCurrentTime);

        timeToDeparture = new TimeToDeparture(currentTime);

        LocalDateTime result = timeToDeparture.subtractTimeTo(basedTime, 2, 20, 0);
        System.out.println("Result hour: " + result.getHourOfDay());
        System.out.println("Result minutes: " + result.getMinuteOfHour());
        System.out.println("Result seconds: " + result.getSecondOfMinute());
        assertEquals(1, result.getHourOfDay());
        assertEquals(20, result.getMinuteOfHour());
        assertEquals(1, result.getSecondOfMinute());
    }

    @Test
    public void testConvertTimeToTimeLeftFormat_hourAndMinutes(){
        LocalDateTime timeToConvert = new LocalDateTime()
                .withHourOfDay(3)
                .withMinuteOfHour(40);
        String convertedString = timeToDeparture.convertTimeToTimeLeftFormat(timeToConvert);

        assertEquals("3h40m", convertedString);
    }

    @Test
    public void testConvertTimeToTimeLeftFormat_bigGourAndBigMinutes(){
        LocalDateTime timeToConvert = new LocalDateTime()
                .withHourOfDay(12)
                .withMinuteOfHour(40);
        String convertedString = timeToDeparture.convertTimeToTimeLeftFormat(timeToConvert);

        assertEquals("12h40m", convertedString);
    }

    @Test
    public void testConvertTimeToTimeLeftFormat_onlyMinutes(){
        LocalDateTime timeToConvert = new LocalDateTime()
                .withYear(0)
                .withMonthOfYear(1)
                .withDayOfMonth(1)
                .withHourOfDay(0)
                .withMinuteOfHour(40)
                .withSecondOfMinute(0);
        String convertedString = timeToDeparture.convertTimeToTimeLeftFormat(timeToConvert);

        assertEquals("40m", convertedString);
    }

}