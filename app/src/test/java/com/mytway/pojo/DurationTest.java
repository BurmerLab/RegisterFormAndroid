package com.mytway.pojo;

import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.junit.Test;

public class DurationTest extends TestCase {

    private Duration duration;

    @Test
    public void testPrepareDurationValueToLocalDateTime() throws Exception {
        duration = new Duration(18977);

        LocalDateTime localDateTime = duration.prepareDurationValueToLocalDateTime(duration.getValue());

        assertEquals(5, localDateTime.getHourOfDay());
        assertEquals(17, localDateTime.getMinuteOfHour());
        assertEquals(17, localDateTime.getSecondOfMinute());

        System.out.println(localDateTime.getHourOfDay());
        System.out.println(localDateTime.getMinuteOfHour());
        System.out.println(localDateTime.getSecondOfMinute());
        System.out.println(localDateTime.getMillisOfSecond());

    }
}