package com.mytway.behaviour.pojo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mytway.activity.application.MytwayActivity;
import com.mytway.pojo.Duration;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Legs;
import com.mytway.pojo.Position;
import com.mytway.pojo.User;
import com.mytway.properties.SharedPreferencesNames;
import com.mytway.utility.EthernetConnectivity;
import com.mytway.utility.Session;
import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import static org.mockito.Mockito.*;

public class TimeToDepartureTest extends TestCase {

    private  TimeToDeparture timeToDeparture = new TimeToDeparture();


    public void testPrepareTimeFromStringToCalendar() throws Exception {
        String enterTime = "06:03";

        Calendar calendar = timeToDeparture.dateToCalendar(enterTime);
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));// gets hour in 24h format
        System.out.println(calendar.get(Calendar.HOUR));        // gets hour in 12h format
        System.out.println(calendar.get(Calendar.MINUTE));      // gets month number, NOTE this is zero based!
        System.out.println(calendar.get(Calendar.SECOND));      // gets month number, NOTE this is zero based!

        assertEquals(6, calendar.get(Calendar.HOUR));
        assertEquals(3, calendar.get(Calendar.MINUTE));
    }

//    public void testProcessTime() throws Exception {
//        TimeToDeparture mockTimeToDeparture = mock(TimeToDeparture.class);
//        Session mockSession = mock(Session.class);
//        Context mockContext = mock(Context.class);
//        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
//        TravelTime mockTravelTime = mock(TravelTime.class);
//        EthernetConnectivity mockEthernetConnectivity = mock(EthernetConnectivity.class);
//
//        TimeToDeparture timeToDeparture = new TimeToDeparture();
//        Position currentPosition = new Position(10.10, 20.20);
//
//        DirectionWay directionWay = new DirectionWay(Boolean.TRUE, Boolean.FALSE);
//        timeToDeparture.setDirectionWay(directionWay);
//        User user = new User();
//
//        Position workPlace = new Position(30.30, 40.40);
//        user.setWorkPlace(workPlace);
//        timeToDeparture.setUser(user);
//
//        when(mockContext.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, 1)).thenReturn(mockSharedPreferences);
//        when(mockSession.getUserName()).thenReturn(null);
//        when(mockSession.isUserLogged()).thenReturn(null);
//        when(mockSession.getTypeWork()).thenReturn(0);
//        when(mockSession.getLengthTimeWork()).thenReturn(null);
//        when(mockSession.getStartStandardTimeWork()).thenReturn(null);
//        when(mockSession.getWorkWeek()).thenReturn(null);
//        when(mockSession.getHomeLatitude()).thenReturn(null);
//        when(mockSession.getHomeLongitude()).thenReturn(null);
//        when(mockSession.getWorkLatitude()).thenReturn(null);
//        when(mockSession.getWorkLongitude()).thenReturn(null);
//        when(mockSharedPreferences.getString("startStandardTimeWork", "")).thenReturn("06:30");
//        when(mockEthernetConnectivity.isEthernetOnline(mockContext)).thenReturn(Boolean.FALSE);
//
//        GoogleMapsDirectionJson googleMapsDirectionJson= new GoogleMapsDirectionJson();
//        Legs legs = new Legs();
//        Duration duration = new Duration("80 seconds", 80);
//        legs.setDuration(duration);
//        googleMapsDirectionJson.setLegs(legs);
//
//        when(mockTimeToDeparture.obtainTravelTimeBasedOnDirectonWay(mockContext, currentPosition)).thenReturn(mockTravelTime);
//        when(mockSession.getStartStandardTimeWork()).thenReturn("06:03");
//        when(mockTravelTime.getTravelTimeBetweenTwoPositions(mockContext, currentPosition, workPlace)).thenReturn(googleMapsDirectionJson);
//
//
//
//        Calendar calendar = timeToDeparture.processTime(mockContext, currentPosition);
//    }

//    private TravelTime createMockedTravelTime() {
//        TravelTime travelTime = new TravelTime();
//        GoogleMapsDirectionJson googleMapsDirectionJson= new GoogleMapsDirectionJson();
//        Legs legs = new Legs();
//        Duration duration = new Duration("80 seconds", 80);
//        legs.setDuration(duration);
//        googleMapsDirectionJson.setLegs(legs);
//        travelTime.setGoogleMapsDirectionJson(googleMapsDirectionJson);
//        return travelTime;
//    }
}