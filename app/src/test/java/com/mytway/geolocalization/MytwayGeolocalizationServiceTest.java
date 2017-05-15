package com.mytway.geolocalization;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.behaviour.pojo.screens.MorningScreen;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;
import com.mytway.utility.permission.PermissionUtil;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, PermissionUtil.class})
public class MytwayGeolocalizationServiceTest extends TestCase{

    @InjectMocks
    private MytwayGeolocalizationService serviceInjectMocks = new MytwayGeolocalizationService();

    @Mock
    private Session sessionMock;

    @Mock
    Location locationMock;

    @Mock
    DirectionWay directionWayMock;

    @Mock
    MorningScreen MorningScreenMock;

    @Mock
    RemoteViews viewMock;

    @Mock
    AppWidgetManager managerMock;

    @Mock
    ComponentName thisWidget;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldShowMorningScreenTest() throws Exception {
        //given
        final String DISTANCE_BETWEEN_HOME_AND_WORK = "222";
        Context contextMock = null;
        sessionMock = Mockito.mock(Session.class);
        MytwayGeolocalizationService serviceMock = Mockito.mock(MytwayGeolocalizationService.class);

        Mockito.when(sessionMock.isUserLogged()).thenReturn(true);
        Mockito.when(sessionMock.getWayDistance()).thenReturn(DISTANCE_BETWEEN_HOME_AND_WORK);

        Location currentLocationMocked = createCurrentLocation();

        Mockito.when(serviceMock.getLocalization()).thenReturn(currentLocationMocked);
        Mockito.when(locationMock.getLatitude()).thenReturn(22.0d);
        Mockito.when(locationMock.getLongitude()).thenReturn(33.0d);

        Mockito.when(directionWayMock.isInHome()).thenReturn(true);
        Position mockedCurrentPosition = new Position(22.0d, 33.0d);

        Mockito.doNothing().when(directionWayMock).decideTravelDirectionsAre(mockedCurrentPosition, sessionMock);

        PowerMock.mockStatic(PermissionUtil.class);
        EasyMock.expect(PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, contextMock)).andReturn(true).anyTimes();
        EasyMock.expect(PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, contextMock)).andReturn(true).anyTimes();
        PowerMock.replay(PermissionUtil.class);
        PowerMock.verify(PermissionUtil.class);

        //when
        MytwayGeolocalizationService serviceInjectMocks = new MytwayGeolocalizationService();
        serviceInjectMocks.setLocation(locationMock);
        serviceInjectMocks.setSession(sessionMock);
        serviceInjectMocks.setDirectionWay(directionWayMock);
        serviceInjectMocks.setMorningScreen(MorningScreenMock);
        serviceInjectMocks.setManager(managerMock);
        serviceInjectMocks.setThisWidget(thisWidget);
        serviceInjectMocks.setView(viewMock);
        serviceInjectMocks.updateGeolocalization();

        //then
        Mockito.doNothing().when(MorningScreenMock).prepareScreen(viewMock, directionWayMock,sessionMock, contextMock, mockedCurrentPosition);
        Mockito.doNothing().when(managerMock).updateAppWidget(thisWidget, viewMock);
        Mockito.verify(MorningScreenMock, Mockito.times(1)).prepareScreen(viewMock, directionWayMock,sessionMock, contextMock, mockedCurrentPosition);
    }

    private Location createCurrentLocation() {
        final Location location = new Location("yourprovidername");
        location.setLatitude(1.2345d);
        location.setLongitude(1.2345d);
        return location;
    }


    @Test
    public void testDat(){
        LocalDateTime localDateTime = new LocalDateTime();
        System.out.println("TUTAJ: " + localDateTime.toString("dd-mm-yyyy MM:hh:ss"));
        System.out.println("TUTAJ2: " + localDateTime.toString("dd-mm-yyyy MM:hh:ss aa"));
        //TUTAJ: 2017-03-29T17:04:18.213
    }
}