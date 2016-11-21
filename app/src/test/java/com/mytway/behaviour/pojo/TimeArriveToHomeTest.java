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
    //definiowanie mocka (InjectMocks): http://www.vogella.com/tutorials/Mockito/article.html

    @InjectMocks private TimeArriveToHome timeArriveToHomeWithInjectMock;

    @Mock private TravelTime travelTime;

    //Proba mockowania://http://stackoverflow.com/questions/2684630/how-can-i-make-a-method-return-an-argument-that-was-passed-to-it
    @Test
    public void testFullProcessTime() throws Exception {
        //TimeArriveToHome = CurrentTime + TravelTime (toWork) + workLength + travelTimeToWork (back)
        //TimeArriveToHome = 05:30:26.497 + 00:59:00.410 (toWork) = 6:29:17  || 2016-03-15T06:29:17.086
        //6:29:17 + 08:00:00.000 = 14:29:17 || 2016-03-15T14:29:17.700
        //14:29:17  + 00:52:20.325 (back) = 15:21 || 2016-03-15T15:21:37.209

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
        timeArriveToHomeWithInjectMock.setTravelTimeToWork(createTravelTime("59 min", 3540));
        timeArriveToHomeWithInjectMock.setTravelTimeToHome(createTravelTime("50 min", 3140));

        //Context context, Position currentPosition, Session session, LocalDateTime startWorkTime
        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61

        //todo: udalo sie zamockowac, teraz doprowadzic poprawne dane
        GoogleMapsDirectionJson googleMapsDirectionJson = creategMapsDirectionJson("100km", 10);
        Mockito.when(travelTime.obtainCurrentTravelTimeToWork(contextMock, currentPosition, session)).thenReturn(googleMapsDirectionJson);
        Mockito.when(travelTime.obtainCurrentTravelTimeToHome(contextMock, currentPosition, session)).thenReturn(googleMapsDirectionJson);

        //When
        timeArriveToHomeWithInjectMock.fullProcessTime(contextMock, currentPosition, session);

        //Then
        System.out.println(timeArriveToHomeWithInjectMock.getDisplayTimeMessage());
        assertEquals("15:21", timeArriveToHomeWithInjectMock.getDisplayTimeMessage());
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