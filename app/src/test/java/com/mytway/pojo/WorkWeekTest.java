package com.mytway.pojo;

import junit.framework.TestCase;

public class WorkWeekTest extends TestCase {

    public void testCreateWorkWeekFromString() throws Exception {
        String testedWorkWeekString = "1011110";

        WorkWeek result = WorkWeek.createWorkWeekFromString(testedWorkWeekString);
        WorkWeek expected = new WorkWeek(true,false,true,true,true,true,false);

        assertEquals(expected, result);
    }
}