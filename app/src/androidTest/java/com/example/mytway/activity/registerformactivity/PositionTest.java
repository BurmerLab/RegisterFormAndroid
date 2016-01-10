package com.example.mytway.activity.registerformactivity;

import android.app.Application;
import android.test.ApplicationTestCase;
import static org.junit.Assert.*;

import com.mytway.pojo.Position;

#parse("Position.java")
public class PositionTest {

    public PositionTest(Class<Application> applicationClass) {
        super(applicationClass);
    }

    public void testTest(){
        Position home = new Position(20.15, 50.15);
        Position work = new Position(20.15, 50.15);

        if(home.equals(work)){
            System.out.println("To samo");
        }else{
            System.out.printf("Nie to samo");
        }

    }
}
