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

import java.util.ArrayList;
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
        previousDistancesToHome.add(14.3);
        previousDistancesToHome.add(16.4);
        previousDistancesToHome.add(12.1);
        previousDistancesToHome.add(20.1);
        previousDistancesToHome.add(25.1);
        directionWay.setDistancesToHomeList(previousDistancesToHome);

        List<Boolean> isInWayToHomePreviousDecisions = new LinkedList<>();
        isInWayToHomePreviousDecisions.add(false);
        isInWayToHomePreviousDecisions.add(true);
        isInWayToHomePreviousDecisions.add(false);
        isInWayToHomePreviousDecisions.add(false);
        isInWayToHomePreviousDecisions.add(false);
        isInWayToHomePreviousDecisions.add(false);
        isInWayToHomePreviousDecisions.add(false);
        directionWay.setIsInWayToHomePreviousDecisions(isInWayToHomePreviousDecisions);

        List<Double> previousDistancesToWork = new LinkedList<>();
        previousDistancesToWork.add(66700.1);
        previousDistancesToWork.add(66600.1);
        previousDistancesToWork.add(66500.1);
        previousDistancesToWork.add(66400.1);
        previousDistancesToWork.add(66300.1);
        directionWay.setDistancesToWorkList(previousDistancesToWork);

        List<Boolean> isInWayToWorkPreviousDecisions = new LinkedList<>();
        isInWayToWorkPreviousDecisions.add(true);
        isInWayToWorkPreviousDecisions.add(true);
        isInWayToWorkPreviousDecisions.add(true);
        isInWayToWorkPreviousDecisions.add(true);
        isInWayToWorkPreviousDecisions.add(true);
        isInWayToWorkPreviousDecisions.add(true);
        isInWayToWorkPreviousDecisions.add(true);
        directionWay.setIsInWayToWorkPreviousDecisions(isInWayToWorkPreviousDecisions);

        Distance fullDistanceBetweenHomeAndWork = new Distance("40 km", 40);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        directionWay.decideDirection(currentPosition, homePosition, workPosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.isWayToHome());
        assertEquals(Boolean.TRUE, directionWay.isWayToWork());
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
    public void testObtainStartCurrentDirection(){

        Double currentDistanceToHome = new Double(5);

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(10D);
        previousDistancesToHome.add(20D);
        previousDistancesToHome.add(30D);
        directionWay.setPreviousDistancesToHome(previousDistancesToHome);
        directionWay.obtainStartCurrentDirection(currentDistanceToHome);
        assertEquals(true, directionWay.isInWayToHome());
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

//    @Test
//    public void testObtainListOfDirections(){
//        List<Double> previousDistancesToHomeList = new LinkedList<>();
//        previousDistancesToHomeList.add(10.0);
//        previousDistancesToHomeList.add(20.0);
//        previousDistancesToHomeList.add(30.0);
//
//        double currentDistanceToHome = 40.0;
//
//        List<Boolean> actualListOfDirections = directionWay.obtainDirection(previousDistancesToHomeList, currentDistanceToHome);
//        List<Boolean> expectedListOfDirections = new LinkedList<>();
//        expectedListOfDirections.add(Boolean.FALSE);
//        expectedListOfDirections.add(Boolean.FALSE);
//        expectedListOfDirections.add(Boolean.FALSE);
//
//        assertEquals(expectedListOfDirections, actualListOfDirections);
//
//    }

    @Test
    public void testDouble(){
        double first = 2.20;
        Double second = 2.20;

        if(second < first){
            System.out.println("MNIEJSZY");
        }else{
            System.out.println("NIE");
        }
    }

    @Test
    public void testKolejnosci(){

        List<Integer> previousDistancesList = new LinkedList<>();
        previousDistancesList.add(50);
        previousDistancesList.add(40);
        previousDistancesList.add(40);
        previousDistancesList.add(30);
        previousDistancesList.add(30);
        previousDistancesList.add(20);
        previousDistancesList.add(10);
        previousDistancesList.add(10);
        previousDistancesList.add(5);

        int currentDistanceToPoint = 4;

        boolean isDirectionToHome = false;

        int i = previousDistancesList.size() - 1;
        isDirectionToHome = (previousDistancesList.get(i) < previousDistancesList.get(i - 1)
                ||
                previousDistancesList.get(i - 1) < previousDistancesList.get(i - 2)
                ||
                previousDistancesList.get(i - 2) < previousDistancesList.get(i - 3)
        )
                &&
                currentDistanceToPoint < previousDistancesList.get(i);

        assertEquals(isDirectionToHome, true);

    }

    @Test
    public void testKolejnosciBoolean(){

        List<Boolean> previousDistancesList = new LinkedList<>();
        previousDistancesList.add(true);
        previousDistancesList.add(true);
        previousDistancesList.add(true);
        previousDistancesList.add(false);
        previousDistancesList.add(true);


        boolean result = false;

        int i = previousDistancesList.size() - 1;

        result = ((previousDistancesList.get(i) && previousDistancesList.get(i - 1))
                ||
                (previousDistancesList.get(i) && previousDistancesList.get(i - 2))
                ||
                (previousDistancesList.get(i) && previousDistancesList.get(i - 3))
                ||
                (previousDistancesList.get(i) && previousDistancesList.get(i - 4))
                );

        assertEquals(result, true);

    }


    @Test
    public void checkMethodToRemoveListElements(){
        List<String> elements = new LinkedList<>();
        elements.add("1");
        elements.add("2");
        elements.add("3");
        elements.add("4");
        elements.add("5");
        elements.add("6");
        elements.add("7");
        elements.add("8");
        elements.add("9");
        elements.add("10");
        elements.add("11");
        elements.add("12");
        elements.add("13");

        removeUnusedElementsFromList(elements);

        for(String element : elements){
            System.out.println(" " + element);
        }
    }

    private void removeUnusedElementsFromList(List<String> elements) {
        while(elements.size() > 7){
            elements.subList(0,3).clear();
        }

    }

}