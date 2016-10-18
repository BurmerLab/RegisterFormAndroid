package com.mytway.utility;

import junit.framework.TestCase;

public class CurrentTimeTest extends TestCase {

    private CurrentTime currentTime = new CurrentTime();

    public void testObtainCurrentTime() throws Exception {
        System.out.println("test: " + currentTime.getCurrentTime());
    }
}