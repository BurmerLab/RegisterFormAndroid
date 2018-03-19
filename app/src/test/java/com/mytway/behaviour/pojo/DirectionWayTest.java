package com.mytway.behaviour.pojo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;

import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class DirectionWayTest extends TestCase {
    private Context contextMock = null;
    private DirectionWay directionWay = new DirectionWay(contextMock);

     SharedPreferences sharedPrefs;
     Context context;

    @Before
    public void before() throws Exception {
        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
        this.context = Mockito.mock(Context.class);
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
    }




//    @Test
//    public void testDecideDirectionShouldChooseWayToWork() throws Exception {
//        //given
//        PowerMockito.mockStatic(Log.class);
//        Position currentPosition = new Position( 50.007520, 20.866382);//position1: Bogumilowice ~5km to home
//        Position homePosition = new Position(50.057135, 20.895283);//Bobrowniki Male 61
//        Position workPosition = new Position(50.032492, 19.940118);//Wadowicka 6, Krakow
//        // home vs current = 5.890494509956249
//
//        List<Double> previousDistancesToHome = new LinkedList<>();
//        previousDistancesToHome.add(14.3);
//        previousDistancesToHome.add(16.4);
//        previousDistancesToHome.add(12.1);
//        previousDistancesToHome.add(20.1);
//        previousDistancesToHome.add(25.1);
//        directionWay.setDistancesToHomeList(previousDistancesToHome);
//
//        List<Boolean> isInWayToHomePreviousDecisions = new LinkedList<>();
//        isInWayToHomePreviousDecisions.add(false);
//        isInWayToHomePreviousDecisions.add(true);
//        isInWayToHomePreviousDecisions.add(false);
//        isInWayToHomePreviousDecisions.add(false);
//        isInWayToHomePreviousDecisions.add(false);
//        isInWayToHomePreviousDecisions.add(false);
//        isInWayToHomePreviousDecisions.add(false);
//        directionWay.setIsInWayToHomePreviousDecisions(isInWayToHomePreviousDecisions);
//
//        List<Double> previousDistancesToWork = new LinkedList<>();
//        previousDistancesToWork.add(66700.1);
//        previousDistancesToWork.add(66600.1);
//        previousDistancesToWork.add(66500.1);
//        previousDistancesToWork.add(66400.1);
//        previousDistancesToWork.add(66300.1);
//        directionWay.setDistancesToWorkList(previousDistancesToWork);
//
//        List<Boolean> isInWayToWorkPreviousDecisions = new LinkedList<>();
//        isInWayToWorkPreviousDecisions.add(true);
//        isInWayToWorkPreviousDecisions.add(true);
//        isInWayToWorkPreviousDecisions.add(true);
//        isInWayToWorkPreviousDecisions.add(true);
//        isInWayToWorkPreviousDecisions.add(true);
//        isInWayToWorkPreviousDecisions.add(true);
//        isInWayToWorkPreviousDecisions.add(true);
//        directionWay.setIsInWayToWorkPreviousDecisions(isInWayToWorkPreviousDecisions);
//
//        Distance fullDistanceBetweenHomeAndWork = new Distance("40 km", 40);
//        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);
//
//        //when
//        directionWay.decideDirection(currentPosition, homePosition, workPosition);
//
//        //then
//        assertEquals(Boolean.FALSE, directionWay.isWayToHome());
//        assertEquals(Boolean.TRUE, directionWay.isWayToWork());
//    }

    @Test
    public void testOfDesignateDistanceBetween(){
        Position currentPosition = new Position(20.866382, 50.007520);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(20.895283, 50.057135);//Bobrowniki Male 61

        double distance = Distance.designateDistanceBetween(currentPosition, homePosition);
        // distance = 5.890494509956249

        assertEquals(5.890494509956249, distance);
    }

    @Test
    public void testObtainStartCurrentDirection_directionToHome(){
        //given
        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
        Double currentDistanceToHome = new Double(0.5);

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(7d);
        previousDistancesToHome.add(6d);
        previousDistancesToHome.add(5d);
        previousDistancesToHome.add(4d);
        previousDistancesToHome.add(3d);
        previousDistancesToHome.add(2d);
        previousDistancesToHome.add(1d);

        directionWay.setPreviousDistancesToHome(previousDistancesToHome);

        //when
        directionWay.obtainStartCurrentDirection(currentDistanceToHome);

        //then
        assertTrue(directionWay.getDirectionsStatus().isInWayToHome());
        assertFalse(directionWay.getDirectionsStatus().isInWayToWork());
    }

    @Test
    public void testObtainStartCurrentDirection_directionToWork(){
        //given
        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
        Double currentDistanceToHome = new Double(10);

        List<Double> previousDistancesToHome = new LinkedList<>();
        previousDistancesToHome.add(1d);
        previousDistancesToHome.add(2d);
        previousDistancesToHome.add(3d);
        previousDistancesToHome.add(4d);
        previousDistancesToHome.add(5d);
        previousDistancesToHome.add(6d);
        previousDistancesToHome.add(7d);

        directionWay.setPreviousDistancesToHome(previousDistancesToHome);

        //when
        directionWay.obtainStartCurrentDirection(currentDistanceToHome);

        //then
        assertFalse(directionWay.getDirectionsStatus().isInWayToHome());
        assertTrue(directionWay.getDirectionsStatus().isInWayToWork());
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
        Context contextMock = null;
        DirectionWay directionWay = new DirectionWay(contextMock);
        Position currentPosition = new Position(20.866382, 50.007520);//position1: Bogumilowice ~5km to home
        Position homePosition = new Position(20.895283, 50.057135);//Bobrowniki Male 61

        //when
        directionWay.decideIsInHome(currentPosition, homePosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().getIsInHome());
    }

    @Test
    public void testOfDecideIsInHome(){
        //given
        PowerMockito.mockStatic(Log.class);
        DirectionWay directionWay = new DirectionWay(contextMock);
        Position currentPosition = new Position(20.895955, 50.055475);// BobrownikiMale, crossroad near home
        Position homePosition = new Position(20.895283, 50.057135);//Bobrowniki Male 61

        //0.1907162102378375km

        //when
        directionWay.decideIsInHome(currentPosition, homePosition);

        //then
        assertEquals(Boolean.TRUE, directionWay.getDirectionsStatus().getIsInHome());
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().getIsInWork());
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

    @Test
    //case when user is in travel to work, and is 1,5 to work place;
    public void testDecideDirectionNew_inHome(){
        //przypadek Kosciol Lagiewniki - powinno byc Travel To Work bo jest jescze 1,5 km od pracy

        //given
        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
        DirectionWay directionWay = new DirectionWay(contextMock);

        Position currentPosition = new Position(19.9437808, 50.019351);
        Position homePosition = new Position(20.8945914, 50.056891);
        Position workPosition = new Position(19.939811, 50.032661);

        directionWay.getDirectionsStatus().setWayToWork(Boolean.TRUE);
        // potrzebne pola
        Distance fullDistanceBetweenHomeAndWork = new Distance("82km", 82006.0);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);
//        directionWay.getPercentageOfDistanceBtwHomeAndWorkInMeters();
//        directionWay.percentageDistanceBtwHomeAndWork

        //when
        directionWay.decideDirectionNew(currentPosition, homePosition, workPosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().getIsInWork());
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().getIsInHome());
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().isWayToHome());
        assertEquals(Boolean.TRUE, directionWay.getDirectionsStatus().isWayToWork());
    }

    //case when user is in travel to work, and is 0,5km to work place;
    public void testDecideDirectionNew_isInTravelToWork_200mToWOrk(){
        //przypadek skrzyzowanie lagiewnicka zakopianska - powinno byc Travel To Work bo jest jescze 0,5 km od pracy
        //given
        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
        DirectionWay directionWay = new DirectionWay(contextMock);

        Position currentPosition = new Position(19.9374749, 50.0289214);
        Position homePosition = new Position(20.8945914, 50.056891);
        Position workPosition = new Position(19.939811, 50.032661);

        directionWay.getDirectionsStatus().setWayToWork(Boolean.TRUE);
        // potrzebne pola
        Distance fullDistanceBetweenHomeAndWork = new Distance("82km", 82006.0);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        directionWay.decideDirectionNew(currentPosition, homePosition, workPosition);

        //then
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().getIsInWork());
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().getIsInHome());
        assertEquals(Boolean.FALSE, directionWay.getDirectionsStatus().isWayToHome());
        assertEquals(Boolean.TRUE, directionWay.getDirectionsStatus().isWayToWork());
    }

//    public void testLeaveHomeTimeSetUp(){
//        //given
//        DirectionWay directionWay = new DirectionWay();
//        UserDailyTimes userDailyTimes = new UserDailyTimes();
//
//        //when
//        // first setting leaveHome (by mistake)
//        userDailyTimes.setLeaveHomeTime(new LocalDateTime(2017,11,28,7,35));
//        directionWay.setUpLeaveHomeTime();
//
//        //then
//        directionWay.getUserDailyTimes().getLeaveHomeTime();
//
//    }
//
//    public void testSaveToDatabaseIfNeeded(){
//        //given
//        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
//        directionWay.wasSavedArriveToHomeTimeBefore = false;
//
//        //when
//        directionWay.saveToDatabaseIfNeeded(directionWay.wasSavedArriveToHomeTimeBefore, "Dupa - ", new LocalDateTime(2017,11,28,7,35));
//
//        //then
//        assertEquals(true, directionWay.wasSavedArriveToHomeTimeBefore);
//    }

    @Test
    public void testLocalDateTime(){
//        LocalDateTime leaveWorkTime = new LocalDateTime(2017,11,28,7,00).plusMinutes(PropertiesValues.MINUTES_FOR_WALK_TO_PLACE);
//        System.out.println(leaveWorkTime.toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));

        String arriveToHomeTimeString = "2017-11-28 07:04";
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime arriveToHomeTime = LocalDateTime.parse(arriveToHomeTimeString, format);

    }

    @Test
    public void testInHomeOperations(){
        //given
        PropertiesValues.MOCK_APP_TO_TESTS = true;
        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
        UserDailyTimes userDailyTimes = new UserDailyTimes(contextMock);

        //directionWay z pola calegoTestu
        directionWay.setUserDailyTimes(userDailyTimes);
        directionWay.getUserDailyTimes().setWasSavedLeaveWorkTimeBefore(false);

        //when
        Mockito.when(sharedPrefs.getString(anyString(), anyString())).thenReturn("foobar");

        directionWay.inHomeOperations();
        directionWay.inWayToWorkOperations();
        directionWay.inHomeOperations();
        directionWay.inWayToWorkOperations();
        directionWay.inHomeOperations();
        directionWay.inWayToWorkOperations();
        directionWay.inHomeOperations();
        directionWay.inWayToWorkOperations();

        directionWay.inHomeOperations();
//        directionWay.inHomeOperations();
//        directionWay.inHomeOperations();
//        directionWay.inWayToWorkOperations();
//        directionWay.inWayToWorkOperations();
//        directionWay.inWayToWorkOperations();
//        directionWay.inWorkOperations();
//        directionWay.inWorkOperations();
//        directionWay.inWorkOperations();
//        directionWay.inWorkOperations();
//        directionWay.inWorkOperations();
//        directionWay.inWayToHomeOperations();
//        directionWay.inWayToHomeOperations();
//        directionWay.inWayToHomeOperations();
//        directionWay.inWayToHomeOperations();
//        directionWay.inWayToWorkOperations();
//        directionWay.inHomeOperations();
//        directionWay.inHomeOperations();
//        directionWay.inHomeOperations();
//        directionWay.inHomeOperations();



        //then
        //poprobowac z roznimy operations

    }

    @Test
    public void testStayOnlyNewestDecisions(){
        //given
        List<Double> elements = new LinkedList<>();
//        elements.add(10d);
//        elements.add(9d);
//        elements.add(8d);
        elements.add(7d);
        elements.add(6d);
        elements.add(5d);
        elements.add(4d);
        elements.add(3d);
        elements.add(2d);
        elements.add(1d);

        //when
        directionWay.stayOnlyNewestDecisions(elements);

        //odwracamy bo 1d zostalo dodane na koncy czyli najwczesniej
        Collections.reverse(elements);

        //then
        for(Double element : elements){
            System.out.println(element);
        }
    }


    @Test
    public void testDecideDirectionNew_fullDayTest_end2end(){
        //given
        PropertiesValues.SAVE_TO_FILE_ENABLE = false;
        DirectionWay directionWay = new DirectionWay(contextMock);

        Position homePosition = new Position(20.8945914, 50.056891);
        Position workPosition = new Position(19.939811, 50.032661);

        directionWay.getDirectionsStatus().setWayToWork(Boolean.TRUE);

        Position inHomePosition1 = new Position(20.8951014, 50.0569742); //08-03-2018 05:57:11
        Position inHomePosition2 = new Position(20.8951014, 50.0569742); //08-03-2018 05:57:11
        Position inHomePosition3 = new Position(20.8951014, 50.0569742); //08-03-2018 05:57:11
        Position inHomePosition4 = new Position(20.8951014, 50.0569742); //08-03-2018 05:57:11

        Position travelToWork1 = new Position(20.8648924, 50.0138027); //TravelToWork 08-03-2018 06:16:10 AM
        Position travelToWork2 = new Position(20.8744846, 50.0341271); //TravelToWork 08-03-2018 06:17:45
        Position travelToWork3 = new Position(19.9455736, 50.0295219); //TravelToWork 08-03-2018 07:12:07 AM
        Position travelToWork4 = new Position(19.9373238, 50.0293101); //TravelToWork 08-03-2018 07:13:02 AM

        Position work1 = new Position(19.939053, 50.0329706); //Work  08-03-2018 07:14:03 AM
        Position work2 = new Position(19.9400726, 50.0322021); //Work  08-03-2018 07:15:08 AM
        Position work3 = new Position(19.9405483, 50.0322457); //Work  08-03-2018 07:16:10 AM

        Position travelToHome1 = new Position(19.9454418, 50.0079846); //TravelToHome 08-03-2018 05:02:28 PM
        Position travelToHome2 = new Position(19.945448, 50.0079325); //TravelToHome 08-03-2018 05:02:45 PM
        Position travelToHome3 = new Position(20.1826137, 50.0040326); //TravelToHome 08-03-2018 05:14:34 PM
        Position travelToHome4 = new Position(20.4345295, 49.9918878); // TravelToHome 08-03-2018 05:27:05 PM

        Position morning1 = new Position(20.8951014, 50.0569742); // Morning 08-03-2018 07:28:28 PM
        Position morning2 = new Position(20.8951014, 50.0569742); // Morning 08-03-2018 07:28:28 PM
        Position morning3 = new Position(20.8951014, 50.0569742); // Morning 08-03-2018 07:28:28 PM
        Position morning4 = new Position(20.8951014, 50.0569742); // Morning 08-03-2018 07:28:28 PM

        List<Position> currentPositions = Arrays.asList(inHomePosition1, inHomePosition2, inHomePosition3, inHomePosition4,
                travelToWork1, travelToWork2, travelToWork3, travelToWork4,
                work1, work2, work3,
                travelToHome1, travelToHome2, travelToHome3, travelToHome4,
                morning1, morning2, morning3, morning4);

        Distance fullDistanceBetweenHomeAndWork = new Distance("82km", 82006.0);
        directionWay.setDistanceBetweenHomeAndWork(fullDistanceBetweenHomeAndWork);

        //when
        for(Position position : currentPositions){
            directionWay.decideDirectionNew(position, homePosition, workPosition);
        }

    }

    @Test
    public void testOfCreationFile(){
        try {
            saveToFileLocalization("dupa");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveToFileLocalization(String screenTitle) throws IOException {
        //-----------Only for saving current Localization to file-------
        File dir = new File ("c:/test/");

        if(!dir.exists()){
            dir.mkdirs();
        }

        LocalDateTime currentLocalDateTime = new LocalDateTime();
        LocalDateTime yesterdayLocalDateTime = new LocalDateTime().minusDays(1);

        File file;
        if(currentLocalDateTime.getHourOfDay() < 14){
            file = new File(dir, currentLocalDateTime.toString("dd-MM-yyyy") + "_MORNING.kml");
        }else{
            file = new File(dir, currentLocalDateTime.toString("dd-MM-yyyy") + "_AFTERNOON.kml");
        }

        if(!file.exists()){

            File fileMorning = new File(dir, yesterdayLocalDateTime.toString("dd-MM-yyyy") + "_MORNING.kml");
            File fileAfternoon = new File(dir, yesterdayLocalDateTime.toString("dd-MM-yyyy") + "_AFTERNOON.kml");

            if(fileMorning.exists()){
                FileOutputStream fop = new FileOutputStream(fileMorning, true);
                StringBuilder yesterdayMorningXml = new StringBuilder();
                yesterdayMorningXml.append("  </Document>\n" + "</kml>");
                fop.write(yesterdayMorningXml.toString().getBytes());
                fop.flush();
                fop.close();
            }

            if(fileAfternoon.exists()){
                FileOutputStream fop = new FileOutputStream(fileAfternoon, true);
                StringBuilder yesterdayAfternoonXml = new StringBuilder();
                yesterdayAfternoonXml.append("  </Document>\n" + "</kml>");
                fop.write(yesterdayAfternoonXml.toString().getBytes());
                fop.flush();
                fop.close();
            }

            file.createNewFile();
            FileOutputStream fop = new FileOutputStream(file, true);
            StringBuilder pointXml = new StringBuilder();
            pointXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                    "  <Document>\n" +
                    "    <name>Paths</name>\n" +
                    "    <description>Examples of paths. Note that the tessellate tag is by default\n" +
                    "      set to 0. If you want to create tessellated lines, they must be authored\n" +
                    "      (or edited) directly in KML.</description>\n" +
                    "    <Style id=\"yellowLineGreenPoly\">\n" +
                    "      <LineStyle>\n" +
                    "        <color>7f00ffff</color>\n" +
                    "        <width>4</width>\n" +
                    "      </LineStyle>\n" +
                    "      <PolyStyle>\n" +
                    "        <color>7f00ff00</color>\n" +
                    "      </PolyStyle>\n" +
                    "    </Style>\n");
            fop.write(pointXml.toString().getBytes());
            fop.flush();
            fop.close();
        }

        FileOutputStream fop = new FileOutputStream(file, true);
        StringBuilder pointXml = new StringBuilder();

        pointXml.append("\t<Placemark>\n" +
                "        <name> " + screenTitle + " " + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") +
                "</name>\n" +
                "        <description> " + screenTitle + " " + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + "</description>\n" +
                "        <Point>\n" +
                "            <coordinates> 20.8951014, 50.0569742 </coordinates>\n" +
                "        </Point>\n" +
                "    </Placemark>\n\n");

        fop.write(pointXml.toString().getBytes());
        fop.flush();
        fop.close();

        System.out.println("Done");
    }


}