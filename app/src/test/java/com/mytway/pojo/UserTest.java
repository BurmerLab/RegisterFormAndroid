package com.mytway.pojo;

import junit.framework.TestCase;

public class UserTest extends TestCase {

    public void testObtainTimeFromWorkLengthString() throws Exception {
        User user = new User();
        String result = user.obtainTimeFromTitleString("WORK LENGTH: 8:0");
        String result2 = user.obtainTimeFromTitleString("WORK LENGTH: 18:22");
        assertEquals("8:0", result);
        assertEquals("18:22", result2);
    }
}