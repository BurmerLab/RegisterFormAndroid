package com.mytway.behaviour.pojo;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.util.Calendar;

public class AProcessingTimeTest extends TestCase {

    private TimeToDeparture timeToDeparture = new TimeToDeparture();



    public void testDateToCalendar() throws Exception {

    }

    public void testPrepareTimeFromStringToCalendar() throws Exception {

    }

    public void testPrepareTimeFromCalendarToString() throws Exception {

    }

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

        LocalDateTime result = timeToDeparture.subtractTimeTo(basedTime, 2, 20, 0);
        System.out.println("Result hour: " + result.getHourOfDay());
        System.out.println("Result minutes: " + result.getMinuteOfHour());
        System.out.println("Result seconds: " + result.getSecondOfMinute());
        assertEquals(1, result.getHourOfDay());
        assertEquals(20, result.getMinuteOfHour());
        assertEquals(1, result.getSecondOfMinute());
    }
}