package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import com.mytway.pojo.Duration;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Legs;
import com.mytway.pojo.Position;
import com.mytway.utility.CurrentTime;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.DriverManager;
import java.util.Calendar;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class TimeToDepartureTest extends TestCase {

    private  TimeToDeparture timeToDeparture = new TimeToDeparture();

    @Test
    public void testOfConvertStringToLocalDateTime(){
        LocalDateTime time = LocalDateTime.parse("3/30/2013T0900", DateTimeFormat.forPattern("MM/dd/yyyy'T'HHmm"));
        LocalDateTime time1 = LocalDateTime.parse("T0900", DateTimeFormat.forPattern("'T'HHmm"));
        LocalDateTime time2 = LocalDateTime.parse("0900", DateTimeFormat.forPattern("HHmm"));
        LocalDateTime time3 = LocalDateTime.parse("09:00", DateTimeFormat.forPattern("HH:mm"));
        LocalDateTime time4 = LocalDateTime.parse("9:00", DateTimeFormat.forPattern("HH:mm"));

        System.out.println("time :" + time);
        System.out.println("time :" + time1);
        System.out.println("time :" + time2);
        System.out.println("time :" + time3.getHourOfDay() + "_" + time3.getMinuteOfHour());
        System.out.println("time :" + time4.getHourOfDay() + "_" + time4.getMinuteOfHour());

    }

    @Test
    public void testProcessingTimeToDepartureForStandardUser(){
        /*
        STANDARD USER TYPE
        From Work - wadowicka 8 to Home - Bobrowniki male 61
        http://maps.googleapis.com/maps/api/directions/json?origin=50.03260440260576,19.939129762351513&destination=50.0564951,20.8950155&sensor=true&units=metric

        timeToDeparture = (startWorkTime - travelTime) - currentTime
        timeToDeparture = (08:00 - 00:59:58) - 05:30:47 = 07:00:02 - 05:30:47 = 01:29:15

        * User type: standard type
        * Current time: //2016-03-15 05:30:00
        * Travel Time: 3538s -> 59min
        * start work at: 8:00
        * length time work: 8:00
        * result: 01:30
        */

        //Given
        PowerMockito.mockStatic(Log.class);
        Context context = null;
        TimeToDeparture timeToDeparture = new TimeToDeparture();
        Session s = new Session();
        Session session = s.createMockedSession("standard");

        CurrentTime currentTime = new CurrentTime();

        LocalDateTime mockedCurrentTime =
                LocalDateTime.parse("3/15/2016T0530", DateTimeFormat.forPattern("MM/dd/yyyy'T'HHmm"));
        currentTime.setMockedCurrentTime(mockedCurrentTime);
        currentTime.obtainCurrentTime();

        TravelTime travelTime = createTravelTime();
        timeToDeparture.setSession(session);
        timeToDeparture.setCurrentTime(currentTime);
        timeToDeparture.setTravelTime(travelTime);

        Position currentPosition = new Position(10.00, 20.00);

        //When
        timeToDeparture.processTime(context, currentPosition, session);

        //Then
        System.out.println(timeToDeparture.getDisplayTimeMessage());
        assertEquals("01:30", timeToDeparture.getDisplayTimeMessage());
    }

    @Test
    public void testProcessingTimeToDepartureForFlexibleUser(){
        /*
        FLEXIBLE USER
        From Work - wadowicka 8 to Home - Bobrowniki male 61
        http://maps.googleapis.com/maps/api/directions/json?origin=50.03260440260576,19.939129762351513&destination=50.0564951,20.8950155&sensor=true&units=metric

        timeToDeparture = currentTime + travelTime
        timeToDeparture = 05:30:00 + 00:59:00 = 06:29:00

        * User type: flexible type
        * Current time: //2016-03-15 05:30:00
        * Travel Time: 3538s -> 59min
        * length time work: 8:00
        * result: 06:29:00
        */

        //Given
        PowerMockito.mockStatic(Log.class);
        Context context = null;
        TimeToDeparture timeToDeparture = new TimeToDeparture();
        Session s = new Session();
        Session session = s.createMockedSession("flexible");
        CurrentTime currentTime = new CurrentTime();

        LocalDateTime mockedCurrentTime =
                LocalDateTime.parse("3/15/2016T0530", DateTimeFormat.forPattern("MM/dd/yyyy'T'HHmm"));
        currentTime.setMockedCurrentTime(mockedCurrentTime);
        currentTime.obtainCurrentTime();

        TravelTime travelTime = createTravelTime();
        timeToDeparture.setSession(session);
        timeToDeparture.setCurrentTime(currentTime);
        timeToDeparture.setTravelTime(travelTime);

        Position currentPosition = new Position(10.00, 20.00);

        //When
        timeToDeparture.processTime(context, currentPosition, session);

        //Then
        System.out.println(timeToDeparture.getDisplayTimeMessage());
        assertEquals("06:29", timeToDeparture.getDisplayTimeMessage());
    }

    private TravelTime createTravelTime() {
        TravelTime travelTime = new TravelTime();
        GoogleMapsDirectionJson googleMapsDirectionJson = new GoogleMapsDirectionJson();
        Legs legs = new Legs();
        Duration duration = new Duration("59 min", 3540);
        legs.setDuration(duration);
        googleMapsDirectionJson.setLegs(legs);
        travelTime.setGoogleMapsDirectionJson(googleMapsDirectionJson);
        return travelTime;
    }

    @Test
    public void testRoundToTop(){
        int digits = 80;

        BigDecimal TWO = BigDecimal.valueOf(2);
        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = BigDecimal.ONE;

        for (int i = 0; i <= 10 * digits / 3; i++) {
            BigDecimal mid = low.add(high).divide(TWO, digits, RoundingMode.HALF_UP);
            if (mid.equals(low) || mid.equals(high))
                break;
            if (Math.round(Double.parseDouble(mid.toString())) > 0)
                high = mid;
            else
                low = mid;
        }

        System.out.println("Math.round(" + low + ") is " + Math.round(Double.parseDouble(low.toString())));
        System.out.println("Math.round(" + high + ") is " + Math.round(Double.parseDouble(high.toString())));
    }

    @Test
    public void testRoundTwo(){
        int totalSecs = 3539;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        System.out.println(timeString);
        assertEquals(0, hours);
        assertEquals(58, minutes);
        assertEquals(59, seconds);
    }


}