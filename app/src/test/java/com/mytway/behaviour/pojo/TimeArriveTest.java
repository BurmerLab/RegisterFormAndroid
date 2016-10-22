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

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class TimeArriveTest extends TestCase {

    @Test
    public void testProcessTime() throws Exception {
        //TimeArrive = CurrentTime + TravelTime (toWork) + workLength + travelTimeToWork (back)
        //TimeArrive = 05:30:26.497 + 00:59:00.410 (toWork) = 6:29:17  || 2016-03-15T06:29:17.086
        //6:29:17 + 08:00:00.000 = 14:29:17 || 2016-03-15T14:29:17.700
        //14:29:17  + 00:52:20.325 (back) = 15:21 || 2016-03-15T15:21:37.209

        //Given
        PowerMockito.mockStatic(Log.class);
        Session s = new Session();
        Session session = s.createMockedSession("standard");

        CurrentTime currentTime = new CurrentTime();
        LocalDateTime mockedCurrentTime =
                LocalDateTime.parse("3/15/2016T0530", DateTimeFormat.forPattern("MM/dd/yyyy'T'HHmm"));
        currentTime.setMockedCurrentTime(mockedCurrentTime);
        currentTime.obtainCurrentTime();

        TimeArrive timeArrive = new TimeArrive();
        timeArrive.setSession(session);
        timeArrive.setCurrentTime(currentTime);
        timeArrive.setTravelTimeToWork(createTravelTime("59 min", 3540));
        timeArrive.setTravelTimeToHome(createTravelTime("50 min", 3140));

        //When
        timeArrive.processTime();

        //Then
        System.out.println(timeArrive.getDisplayTimeMessage());
        assertEquals("15:21", timeArrive.getDisplayTimeMessage());
        //15:21
    }

    private TravelTime createTravelTime(String durationText, int durationSeconds) {
        TravelTime travelTime = new TravelTime();
        GoogleMapsDirectionJson googleMapsDirectionJson = new GoogleMapsDirectionJson();
        Legs legs = new Legs();
        Duration duration = new Duration(durationText, durationSeconds);
        legs.setDuration(duration);
        googleMapsDirectionJson.setLegs(legs);
        travelTime.setGoogleMapsDirectionJson(googleMapsDirectionJson);
        return travelTime;
    }
}