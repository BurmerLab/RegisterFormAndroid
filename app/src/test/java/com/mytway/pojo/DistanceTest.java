package com.mytway.pojo;

import junit.framework.TestCase;

public class DistanceTest extends TestCase {

    public void testObtainSevenPercentFromDistance() throws Exception {
        //given
        Distance distance = new Distance();
        distance.setValueInMeters(100);

        //when
        double result = distance.obtainSevenPercentFromDistance();

        //then
        assertEquals(7.0, result);
    }
}