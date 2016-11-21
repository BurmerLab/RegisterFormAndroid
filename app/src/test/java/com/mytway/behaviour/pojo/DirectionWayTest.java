package com.mytway.behaviour.pojo;

import android.util.Log;

import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class DirectionWayTest extends TestCase {

    private DirectionWay directionWay = new DirectionWay();

    @Test
    public void testDecideDirectionShouldChooseWayToWork() throws Exception {
        //given
        PowerMockito.mockStatic(Log.class);
        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61
        // home vs current = 5.890494509956249

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(1.1);
        previousDistancesToHome.add(3.4);
        previousDistancesToHome.add(4.3);
        directionWay.setDistancesToHome(previousDistancesToHome);

        //todo: check what should be here, KM or Meters?
        Distance fullDistanceBetweenHomeAndWork = new Distance("40 km", 40);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        directionWay.decideDirection(currentPosition, homePosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.isWayToHome());
        assertEquals(Boolean.TRUE, directionWay.isWayToWork());
    }

    @Test
    public void testDecideDirectionShouldNotChooseAnyWay() throws Exception {
        //given
        PowerMockito.mockStatic(Log.class);
        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61
        // home vs current = 5.890494509956249

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(14.3);
        previousDistancesToHome.add(16.4);
        previousDistancesToHome.add(12.1);
        directionWay.setDistancesToHome(previousDistancesToHome);

        //todo: check what should be here, KM or Meters?
        Distance fullDistanceBetweenHomeAndWork = new Distance("40 km", 40);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        directionWay.decideDirection(currentPosition, homePosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.isWayToHome());
        assertEquals(Boolean.FALSE, directionWay.isWayToWork());
    }

    @Test
    public void testOfDesignateDistanceBetween(){
        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61

        double distance = Distance.designateDistanceBetween(currentPosition, homePosition);
        // distance = 5.890494509956249

        assertEquals(5.890494509956249, distance);
    }

    @Test
    public void testOfLinkedList(){
        List<Integer> linkedLIst = new LinkedList<>();
        linkedLIst.add(1);
        linkedLIst.add(2);
        linkedLIst.add(3);
        int a = 0;
        int b = 0;
        int c = 0;
        if(linkedLIst.size() >= 3){
            a = linkedLIst.get(linkedLIst.size()-1);
            b = linkedLIst.get(linkedLIst.size()-2);
            c = linkedLIst.get(linkedLIst.size()-3);
        }

        int i = 0;
        System.out.println("_" + i++ + ", result:" + a);
        System.out.println("_" + i++ + ", result:" + b);
        System.out.println("_" + i++ + ", result:" + c);
    }
}