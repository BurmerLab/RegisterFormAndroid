package com.mytway.behaviour.pojo;

import android.content.Context;
import android.util.Log;

import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class TimeInRoadTest extends TestCase {

    public void testProcessTime() throws Exception {
        //given
        PowerMockito.mockStatic(Log.class);
        Context context = null;
        LocalDateTime timeInRoadDateTime = LocalDateTime.parse("1:30", DateTimeFormat.forPattern("HH:mm"));

        TimeInRoad timeInRoad = new TimeInRoad();
        timeInRoad.setTimeInRoad(timeInRoadDateTime);

        //when
        timeInRoad.processTime();

        //then
        String timeInRoadDisplayMessage = timeInRoad.getDisplayTimeMessage();
        assertEquals("01:30", timeInRoadDisplayMessage);
    }
}