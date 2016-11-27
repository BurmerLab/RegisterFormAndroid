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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.api.mockito.PowerMockito;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class TimeArriveToHomeTest extends TestCase {

    @InjectMocks private TimeArriveToHome timeArriveToHomeWithInjectMock;

    @Mock private TravelTime travelTime;

    @Test
    public void testFullProcessTime() throws Exception {
        //TimeArriveToHome = CurrentTime + TravelTime (toWork) + workLength + travelTimeToWork (back)
        //TimeArriveToHome = 05:30:26.497 + 10min (toWork) = 5:40:00
        //5:40:00 + 08:00:00.000 = 13:40:00
        //13:40:00  + 00:20:00 (back) = 14:00

        //Given
        PowerMockito.mockStatic(Log.class);

        Session s = new Session();
        Session session = s.createMockedSession("standard");
        Context contextMock = null;

        CurrentTime currentTime = new CurrentTime();
        LocalDateTime mockedCurrentTime =
                LocalDateTime.parse("3/15/2016T0530", DateTimeFormat.forPattern("MM/dd/yyyy'T'HHmm"));
        currentTime.setMockedCurrentTime(mockedCurrentTime);
        currentTime.obtainCurrentTime();

        timeArriveToHomeWithInjectMock.setSession(session);
        timeArriveToHomeWithInjectMock.setCurrentTime(currentTime);

        //Context context, Position currentPosition, Session session, LocalDateTime startWorkTime
        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61

        GoogleMapsDirectionJson gMapsDirectionToWork = creategMapsDirectionJson("100km", 600);//600s - 10min
        GoogleMapsDirectionJson gMapsDirectionToHome = creategMapsDirectionJson("100km", 1200);//1200s - 20min
        Mockito.when(travelTime.obtainCurrentTravelTimeToWork(contextMock, currentPosition, session)).thenReturn(gMapsDirectionToWork);
        Mockito.when(travelTime.obtainCurrentTravelTimeToHome(contextMock, currentPosition, session)).thenReturn(gMapsDirectionToHome);

        //When
        timeArriveToHomeWithInjectMock.fullProcessTime(contextMock, currentPosition, session);

        //Then
        //05:30 + 10 min + 8h + 20min = 14.00?
        System.out.println(timeArriveToHomeWithInjectMock.getDisplayTimeMessage());
        assertEquals("14:00", timeArriveToHomeWithInjectMock.getDisplayTimeMessage());
        //15:21
    }

    private GoogleMapsDirectionJson creategMapsDirectionJson(String durationText, int durationSeconds) {
        GoogleMapsDirectionJson googleMapsDirectionJson = new GoogleMapsDirectionJson();
        Legs legs = new Legs();
        Duration duration = new Duration(durationText, durationSeconds);
        legs.setDuration(duration);
        googleMapsDirectionJson.setLegs(legs);
        return googleMapsDirectionJson;
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