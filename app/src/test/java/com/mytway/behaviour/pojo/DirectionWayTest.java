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
        Position workPosition = new Position(50.032492, 19.940118);//Wadowicka 6, Krakow
        // home vs current = 5.890494509956249

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(1.1);
        previousDistancesToHome.add(3.4);
        previousDistancesToHome.add(4.3);

        List<Double> previousDistancesToWork = new LinkedList<>();
        previousDistancesToWork.add(70000.1);
        previousDistancesToWork.add(69000.4);
        previousDistancesToWork.add(68000.3);

        directionWay.setDistancesToHomeList(previousDistancesToHome);
        directionWay.setDistancesToWorkList(previousDistancesToWork);

        Distance fullDistanceBetweenHomeAndWork = new Distance("40 m", 40);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        directionWay.decideDirection(currentPosition, homePosition, workPosition);

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
        Position workPosition = new Position(50.032492, 19.940118);//Wadowicka 6, Krakow
        // home vs current = 5.890494509956249

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(14.3);
        previousDistancesToHome.add(16.4);
        previousDistancesToHome.add(12.1);
        directionWay.setDistancesToHomeList(previousDistancesToHome);

        //todo: check what should be here, KM or Meters?
        Distance fullDistanceBetweenHomeAndWork = new Distance("40 km", 40);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        directionWay.decideDirection(currentPosition, homePosition, workPosition);

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


    @Test
    public void testOfDecideIsNotInHome(){
        //given
        PowerMockito.mockStatic(Log.class);
        DirectionWay directionWay = new DirectionWay();
        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61

        //when
        directionWay.decideIsInHome(currentPosition, homePosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.getIsInHome());
    }

    @Test
    public void testOfDecideIsInHome(){
        //given
        PowerMockito.mockStatic(Log.class);
        DirectionWay directionWay = new DirectionWay();
        Position currentPosition = new Position(50.055475, 20.895955);// BobrownikiMale, crossroad near home
        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61

        //0.1907162102378375km

        //when
        directionWay.decideIsInHome(currentPosition, homePosition);

        //then
        assertEquals(Boolean.TRUE, directionWay.getIsInHome());
        assertEquals(Boolean.FALSE, directionWay.getIsInWork());
    }

    @Test
    public void testObtainListOfDirections(){
        List<Double> previousDistancesToHomeList = new LinkedList<>();
        previousDistancesToHomeList.add(10.0);
        previousDistancesToHomeList.add(20.0);
        previousDistancesToHomeList.add(30.0);

        double currentDistanceToHome = 40.0;

        List<Boolean> actualListOfDirections = directionWay.obtainListOfDirections(previousDistancesToHomeList, currentDistanceToHome);
        List<Boolean> expectedListOfDirections = new LinkedList<>();
        expectedListOfDirections.add(Boolean.FALSE);
        expectedListOfDirections.add(Boolean.FALSE);
        expectedListOfDirections.add(Boolean.FALSE);

        assertEquals(expectedListOfDirections, actualListOfDirections);

    }

}