package com.mytway.utility;

import android.content.Context;

import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TravelTimeTest extends TestCase {

    @InjectMocks
    TravelTime travelTime;

    @Mock
    private Session sessionMock;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testObtainStaticTravelTimeBasedOnDirectonWay() throws Exception {
    //given
        PropertiesValues.MOCK_APP_TO_TESTS = true;
        Context contextMock = null;
        sessionMock = Mockito.mock(Session.class);

        //distance: 62,5km
        //duration 2308 - 38min

        Position currentPosition = new Position(20.4857967, 50.0032076);
        Position homePosition = new Position(20.8945914, 50.056891);
        Position workPosition = new Position(19.939811, 50.032661);

        Mockito.when(sessionMock.isUserLogged()).thenReturn(true);
        Mockito.when(sessionMock.getWayDistance()).thenReturn("83683");//meters
        Mockito.when(sessionMock.getWayDuration()).thenReturn("3630");//seconds 1h 1 m

        Mockito.when(sessionMock.getHomePlace()).thenReturn(homePosition);
        Mockito.when(sessionMock.getWorkPlace()).thenReturn(workPosition);

    //when
        TravelTime travelTime = new TravelTime();
        travelTime.obtainStaticTravelTime(contextMock, sessionMock, currentPosition, workPosition);

    //then
        int expectedDuration = 2082;
        assertEquals(expectedDuration, travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getValue());
    }

}