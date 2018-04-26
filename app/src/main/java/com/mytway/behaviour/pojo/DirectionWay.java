package com.mytway.behaviour.pojo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.mytway.database.userLocalizations.UserLocalizationsRepo;
import com.mytway.database.userLocalizations.UserLocalizationsTable;
import com.mytway.database.userTimes.UserTimesRepo;
import com.mytway.database.userTimes.UserTimesTable;
import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DirectionWay {

    private static final String TAG = "DirectionWay";
    private static final int THREE_DISTANCES = 3;
    private static final double HOME_OR_WORK_ZONE = 300.0;

    private Context context;
    private Session session;

    private Distance distanceBetweenHomeAndWork;
    private double percentageDistanceBtwHomeAndWork;

    private List<Double> distancesToHomeList = new LinkedList<>();
    private List<Double> distancesToWorkList = new LinkedList<>();

    private List<Boolean> isInWayToHomePreviousDecisions = new LinkedList<>();
    private List<Boolean> isInWayToWorkPreviousDecisions = new LinkedList<>();

    private List<Double> previousDistancesToHome = new LinkedList<>();
    private List<Double> previousDistancesToWork = new LinkedList<>();

    private UserDailyTimes userDailyTimes;
    private DirectionsStatus directionsStatus;

    public DirectionWay(Context context) {
        setContext(context);
        userDailyTimes = new UserDailyTimes(context);
        directionsStatus = new DirectionsStatus(context);
        session = new Session(context);
    }

    public DirectionWay(boolean wayToWork, boolean wayToHome) {
        directionsStatus.setWayToWork(wayToWork);
        directionsStatus.setWayToHome(wayToHome);
    }

    public void setPreviousDistancesToHome(List<Double> previousDistancesToHome) {
        this.previousDistancesToHome = previousDistancesToHome;
    }

    public List<Double> getPreviousDistancesToHome() {
        return previousDistancesToHome;
    }

    //todo: commented because session is created for whole DirectionWay not need to transfer as argument
//    public void decideTravelDirectionsAre(Position currentPosition, Session session){
//        this.decideDirectionNew(currentPosition, session.getHomePlace(), session.getWorkPlace());
//    }
    public void decideTravelDirectionsAre(Position currentPosition){
        this.decideDirectionNew(currentPosition, session.getHomePlace(), session.getWorkPlace());
    }

    public void decideDirectionNew(Position currentPosition, Position homePosition, Position workPosition) {
        double currentDistToHomeInM = obtainDistanceBetweenInMeters(currentPosition, homePosition);
        double currentDistToWorkInM = obtainDistanceBetweenInMeters(currentPosition, workPosition);

        calculateFirstDirection(currentPosition, homePosition, workPosition, currentDistToHomeInM);

        if(directionsStatus.isInHome()) {
            //can be isStillInHome or wayToWork
            if(isStillInHome(currentDistToHomeInM)){
                inHomeOperations();
            }else{
                inWayToWorkOperations();
                saveToFileDirections("IS IN WAY TO WORK");
            }

        }else if(directionsStatus.isInWayToWork()){
            //can be wayToWork or isStillInWork or isStillInHome
            if(isStillInWork(currentDistToWorkInM)){
                inWorkOperations();
                saveToFileDirections("IS IN WORK");

            }else if(isStillInHome(currentDistToHomeInM)){
                inHomeOperations();
                saveToFileDirections("IS IN HOME");
            }else{
                inWayToWorkOperations();
            }

        }else if(directionsStatus.isInWork()){
            //can be isStillInWork or wayToHome
            if(isStillInWork(currentDistToWorkInM)){
                inWorkOperations();
            }else{
                inWayToHomeOperations();
                saveToFileDirections("IS IN WAY TO HOME");
            }

        }else if(directionsStatus.isInWayToHome()){
            //can be isStillInHome or wayToHome or isStillInWork
            if(isStillInHome(currentDistToHomeInM)) {
                inHomeOperations();
                saveToFileDirections("IS IN HOME");

            }else if(isStillInWork(currentDistToWorkInM)){
                inWorkOperations();
                saveToFileDirections("IS IN WORK");

            }else{
                inWayToHomeOperations();
            }

        }else{
            saveToFile("NIE ZNALAZLEM KIERUNKU :(");
            saveToFileDirections("NIE ZNALAZLEM KIERUNKU ");
        }
    }

    public void inHomeOperations() {
        saveToFile("SETUP IS IN HOME");
        directionsStatus.setIsInHome(TRUE);
        directionsStatus.setIsInWork(FALSE);
        directionsStatus.setWayToHome(FALSE);
        directionsStatus.setWayToWork(FALSE);

        clearLeaveHomeTime();

        saveToFileDatabaseTimes("===========wasSavedLeaveWorkTimeBefore = " +  userDailyTimes.getWasSavedLeaveWorkTimeBefore() + "===========");
        if(isLeaveWorkTimeNotNullAndWasntSavedBefore()){

            //todo: dorobic poberanie i zapisyawnie ID uzytkownika w bazie i w ogole wszdzie- przemyslec czy pob ierac to ID od bazy zewnetrznejs
            UserTimesRepo userTimesRepo = new UserTimesRepo(context);
            UserTimesTable userTimesTable = new UserTimesTable();
            userTimesTable.setUserTimesId(Integer.parseInt(session.getUserId()));
            userTimesTable.setUserName(session.getUserName());
            userTimesTable.setTimeStatus(UserDailyTimes.LEAVE_WORK_TIME);
            userTimesTable.setCreationDate(new LocalDateTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            userTimesTable.setTime(userDailyTimes.getLeaveWorkTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));

            UserLocalizationsRepo localizationsRepo = new UserLocalizationsRepo(context);
            UserLocalizationsTable localizationsTable = new UserLocalizationsTable();
            localizationsTable.setCreationDate("2018-03-18");
            localizationsTable.setLatitude(String.valueOf());
            localizationsTable.setLongitude(String.valueOf(getLongitude()));
            localizationsTable.setTimeStatus("Morning");
            localizationsTable.setUserName("Mike");
            localizationsRepo.insert(localizationsTable);

            saveToFileDatabaseTimes("IN HOME - LeaveWork - " + userDailyTimes.getLeaveWorkTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            userDailyTimes.setWasSavedLeaveWorkTimeBefore(true);
        }

        setUpArriveToHomeTime();

        saveToFileDatabaseTimes("===========wasSavedArriveToHomeTimeBefore = " + userDailyTimes.getWasSavedArriveToHomeTimeBefore() + "===========");
        if(isArriveToHomeTimeNotNullAndWasntSavedBefore()) {
            saveToFileDatabaseTimes("IN HOME - ArriveToHome - " + userDailyTimes.getArriveToHomeTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            userDailyTimes.setWasSavedArriveToHomeTimeBefore(true);
        }
    }

    public void inWayToWorkOperations() {
        saveToFile("SETUP IS IN WAY TO WORK");
        directionsStatus.setIsInHome(FALSE);
        directionsStatus.setIsInWork(FALSE);
        directionsStatus.setWayToHome(FALSE);
        directionsStatus.setWayToWork(TRUE);

        clearLeaveWorkTime();

        setUpLeaveHomeTime();
    }

    public void inWorkOperations() {
        saveToFile("SETUP IS IN WORK");
        directionsStatus.setIsInHome(FALSE);
        directionsStatus.setIsInWork(TRUE);
        directionsStatus.setWayToHome(FALSE);
        directionsStatus.setWayToWork(FALSE);

        clearLeaveWorkTime();
        clearArriveToHomeTime();

        saveToFileDatabaseTimes("===========wasSavedLeaveHomeTimeBefore = " + userDailyTimes.getWasSavedLeaveHomeTimeBefore() + "===========");
        if(isLeaveHomeTimeNotNullAndWasntSavedBefore()){
            saveToFileDatabaseTimes("IN WORK - LeaveHome - " + userDailyTimes.getLeaveHomeTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            userDailyTimes.setWasSavedLeaveHomeTimeBefore(true);
        }

        setUpStartWorkTime();

        saveToFileDatabaseTimes("===========wasSavedStartWorkTimeBefore = " + userDailyTimes.getWasSavedStartWorkTimeBefore() + "===========");
        if(isStartWorkTimeNotNullAndWasntSavedBefore()){
            saveToFileDatabaseTimes("IN WORK - StartWorkTime - " + userDailyTimes.getStartWorkTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            userDailyTimes.setWasSavedStartWorkTimeBefore(true);
        }
    }

    public void inWayToHomeOperations() {
        saveToFile("SETUP IS IN WAY TO HOME");
        directionsStatus.setIsInHome(FALSE);
        directionsStatus.setIsInWork(FALSE);
        directionsStatus.setWayToHome(TRUE);
        directionsStatus.setWayToWork(FALSE);

        clearLeaveHomeTime();
        clearStartWorkTime();

        setUpLeaveWorkTime();
    }

    public boolean isArriveToHomeTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getArriveToHomeTime() != null && !userDailyTimes.getWasSavedArriveToHomeTimeBefore();
    }

    public boolean isLeaveWorkTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getLeaveWorkTime() != null && !userDailyTimes.getWasSavedLeaveWorkTimeBefore();
    }

    public boolean isStartWorkTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getStartWorkTime() != null && !userDailyTimes.getWasSavedStartWorkTimeBefore();
    }

    public boolean isLeaveHomeTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getLeaveHomeTime() != null && !userDailyTimes.getWasSavedLeaveHomeTimeBefore();
    }

    public void setUpLeaveHomeTime(){
        if(userDailyTimes.getLeaveHomeTime() == null){
            userDailyTimes.setLeaveHomeTime(new LocalDateTime().minusMinutes(PropertiesValues.MINUTES_FOR_RECOGNITION_LEAVED_PLACE));
            saveToFileSetUpTimeFromNull("setUp LeaveHomeTime = " + userDailyTimes.getLeaveHomeTime());
        }
    }

    public void setUpStartWorkTime(){
        if(userDailyTimes.getStartWorkTime() == null){
            userDailyTimes.setStartWorkTime(new LocalDateTime().plusMinutes(PropertiesValues.MINUTES_FOR_WALK_TO_PLACE));
            saveToFileSetUpTimeFromNull("setUp StartWorkTime = " + userDailyTimes.getStartWorkTime());
        }
    }

    public void setUpLeaveWorkTime(){
        if(userDailyTimes.getLeaveWorkTime() == null){
            userDailyTimes.setLeaveWorkTime(new LocalDateTime().minusMinutes(PropertiesValues.MINUTES_FOR_RECOGNITION_LEAVED_PLACE));
            saveToFileSetUpTimeFromNull("setUp LeaveWorkTime = " + userDailyTimes.getLeaveWorkTime());
        }
    }

    public void setUpArriveToHomeTime(){
        if(userDailyTimes.getArriveToHomeTime() == null){
            userDailyTimes.setArriveToHomeTime(new LocalDateTime().plusMinutes(PropertiesValues.MINUTES_FOR_WALK_TO_PLACE));
            saveToFileSetUpTimeFromNull("setUp ArriveToHomeTime = " + userDailyTimes.getArriveToHomeTime());
        }
    }

    public void clearArriveToHomeTime(){
        userDailyTimes.clearArriveToHomeTime();
        userDailyTimes.clearWasSavedArriveToHomeTimeBefore();
    }

    public void clearLeaveWorkTime(){
        userDailyTimes.clearLeaveWorkTime();
        userDailyTimes.clearWasSavedLeaveWorkTimeBefore();
    }

    public void clearLeaveHomeTime(){
        userDailyTimes.clearLeaveHomeTime();
        userDailyTimes.clearWasSavedLeaveHomeTimeBefore();
    }

    public void clearStartWorkTime(){
        userDailyTimes.clearStartWorkTime();
        userDailyTimes.clearWasSavedStartWorkTimeBefore();
    }

    public boolean isStillInWork(double currentDistanceToWorkInMeters) {
        return PropertiesValues.SAFE_LENGTH_AROUND_HOME_AND_WORK_IN_METERS > currentDistanceToWorkInMeters;
    }

    public boolean isStillInHome(double currentDistanceToHomeInMeters) {
        return PropertiesValues.SAFE_LENGTH_AROUND_HOME_AND_WORK_IN_METERS > currentDistanceToHomeInMeters;
    }

    public void calculateFirstDirection(Position currentPosition, Position homePosition, Position workPosition, double currentDistanceToHomeInMeters) {
        if( !directionsStatus.isInHome() && !directionsStatus.isInWork() && !directionsStatus.isInWayToWork() && !directionsStatus.isInWayToHome()){
            saveToFile("setFirstDirection, inHome, inWork, wayTowork, wayToWOrk = FALSE");
            decideIsInHome(currentPosition, homePosition);
            decideIsInWork(currentPosition, workPosition);
            obtainStartCurrentDirection(currentDistanceToHomeInMeters);
        }else{
            saveToFile("HOME: " + directionsStatus.isInHome());
            saveToFile("WORK: " + directionsStatus.isInWork());
            saveToFile("WAY HOME" + directionsStatus.isInWayToHome());
            saveToFile("WAY WORK" + directionsStatus.isInWayToWork());
        }

        previousDistancesToHome.add(currentDistanceToHomeInMeters);
        stayOnlyNewestDecisions(previousDistancesToHome);
    }

    public void obtainStartCurrentDirection(double currentDistanceToHomeInMeters) {
//        saveToFile("WSZYSTKIE FLAGI FALSE, DEFAULT: is in home");
        if( !directionsStatus.isInHome() && !directionsStatus.isInWork() && !directionsStatus.isInWayToWork() && !directionsStatus.isInWayToHome()){

            if(previousDistancesToHome.size() > 1){
                double previousDistanceToHome =  previousDistancesToHome.get(previousDistancesToHome.size() - 1);
                //todo; prztestowac to w unit tescie

                if(previousDistanceToHome > currentDistanceToHomeInMeters){
                    saveToFile("KIERUNEK: SETUP way to home");
                    saveToFile("KIERUNEK: previousDistanceToHome = " + previousDistanceToHome);
                    saveToFile("KIERUNEK: currentDistanceToHomeInMeters = " + currentDistanceToHomeInMeters);
                    saveToFile("KIERUNEK: previousDistanceToHome > currentDistanceToHomeInMeters = " + (previousDistanceToHome > currentDistanceToHomeInMeters));

                    directionsStatus.setIsInHome(FALSE);
                    directionsStatus.setIsInWork(FALSE);
                    directionsStatus.setWayToHome(TRUE);
                    directionsStatus.setWayToWork(FALSE);
                }else{
                    saveToFile("KIERUNEK ELSE: SETUP way to work");
                    saveToFile("KIERUNEK: previousDistanceToHome = " + previousDistanceToHome);
                    saveToFile("KIERUNEK: currentDistanceToHomeInMeters = " + currentDistanceToHomeInMeters);
                    saveToFile("KIERUNEK: previousDistanceToHome > currentDistanceToHomeInMeters = " + (previousDistanceToHome > currentDistanceToHomeInMeters));

                    directionsStatus.setIsInHome(FALSE);
                    directionsStatus.setIsInWork(FALSE);
                    directionsStatus.setWayToHome(FALSE);
                    directionsStatus.setWayToWork(TRUE);
                }
            }else{
                saveToFile("previousDistancesToHome == 0 pomiarow");
            }
        }
    }

    public void stayOnlyNewestDecisions(List<Double> elements) {
        while(elements.size() >= 5){
            Collections.reverse(elements);
            elements.subList(Math.max(elements.size() - 3, 0), elements.size()).clear();
            Collections.reverse(elements);
        }
    }

    public static void saveToFile(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "DirectionWay.txt");
                LocalDateTime currentLocalDateTime = new LocalDateTime();
                //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

                FileOutputStream fop = new FileOutputStream(file, true);
                String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


                fop.write(pointXml.getBytes());
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void saveToFileSetUpTimeFromNull(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "SETUP_TIME_FROM_NULL.txt");
                LocalDateTime currentLocalDateTime = new LocalDateTime();
                //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

                FileOutputStream fop = new FileOutputStream(file, true);
                String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


                fop.write(pointXml.getBytes());
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void saveToFileDirections(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "DIRECTIONS_AFTER_CHANGE.txt");
                LocalDateTime currentLocalDateTime = new LocalDateTime();
                //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

                FileOutputStream fop = new FileOutputStream(file, true);
                String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


                fop.write(pointXml.getBytes());
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveToFileTime(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "TIMES.txt");
                LocalDateTime currentLocalDateTime = new LocalDateTime();
                //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

                FileOutputStream fop = new FileOutputStream(file, true);
                String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


                fop.write(pointXml.getBytes());
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void saveToFileDatabaseTimes(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "DATABASE_TIMES.txt");
                LocalDateTime currentLocalDateTime = new LocalDateTime();
                //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

                FileOutputStream fop = new FileOutputStream(file, true);
                String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


                fop.write(pointXml.getBytes());
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(content);
        }

    }

    public double obtainDistanceBetweenInMeters(Position currentPosition, Position secondPlace){
        double distanceBetween = Distance.designateDistanceBetween(currentPosition, secondPlace);
        return distanceBetween * PropertiesValues.METERS_IN_KILOMETER;
    }

    public int obtainDistanceToWork(Position currentPosition, Position workPlace){
        int distanceToWork = (int) Distance.designateDistanceBetween(currentPosition, workPlace);
        return distanceToWork;
    }

    public void decideIsInHome(Position currentPosition, Position homePosition){
        if(decideIsInPlace(currentPosition, homePosition)){
            directionsStatus.setIsInHome(TRUE);
            directionsStatus.setIsInWork(FALSE);
            directionsStatus.setWayToHome(FALSE);
            directionsStatus.setWayToWork(FALSE);

            saveToFile("\n<<<<<<<<<<<<<<<<  >>>>>>>>>>>>>>>>>>");
            saveToFile("<<<<<<<<<<<<<<<< IS IN HOME DECISION TRUE >>>>>>>>>>>>>>>>>>");
        }else{
            saveToFile("<<<<<<<<<<<<<<<< NOT IN HOME >>>>>>>>>>>>>>>>>>");
            directionsStatus.setIsInHome(FALSE);

        }
    }

    public void decideIsInWork(Position currentPosition, Position workPosition){
        if( !directionsStatus.isInHome() && !directionsStatus.isInWork() && !directionsStatus.isInWayToWork() && !directionsStatus.isInWayToHome()){
            if(decideIsInPlace(currentPosition, workPosition)){
                directionsStatus.setIsInWork(TRUE);
                directionsStatus.setIsInHome(FALSE);
                directionsStatus.setWayToHome(FALSE);
                directionsStatus.setWayToWork(FALSE);

                saveToFile("\n\n<<<<<<<<<<<<<<<<  >>>>>>>>>>>>>>>>>>");
                saveToFile("<<<<<<<<<<<<<<<< IS IN WORK >>>>>>>>>>>>>>>>>>");

            }else{
                saveToFile("<<<<<<<<<<<<<<<< NOT IN WORK >>>>>>>>>>>>>>>>>>");
                directionsStatus.setIsInWork(FALSE);
            }
        }
    }

    public boolean decideIsInPlace(Position currentPosition, Position place) {
        double distanceTo = Distance.designateDistanceBetween(currentPosition, place);

        double distanceBetweens = distanceTo * 1000;

        saveToFile(" distance: " + distanceTo);
        saveToFile(" distanceBetweens( <300?): " + distanceBetweens);

        if(distanceBetweens < HOME_OR_WORK_ZONE){
            Log.i(TAG, "User is in Place");
            saveToFile("User is in Place");
            return true;
        }else{
            return false;
        }
    }

    public List<Double> getDistancesToHomeList() {
        return distancesToHomeList;
    }

    public void setDistancesToHomeList(List<Double> distancesToHomeList) {
        this.distancesToHomeList = distancesToHomeList;
    }

    public Distance getDistanceBetweenHomeAndWork() {
        return distanceBetweenHomeAndWork;
    }

    public void setDistanceBetweenHomeAndWork(Distance distanceBetweenHomeAndWork) {
        this.distanceBetweenHomeAndWork = distanceBetweenHomeAndWork;
    }

    public List<Double> getDistancesToWorkList() {
        return distancesToWorkList;
    }

    public void setDistancesToWorkList(List<Double> distancesToWorkList) {
        this.distancesToWorkList = distancesToWorkList;
    }

    public List<Boolean> getIsInWayToHomePreviousDecisions() {
        return isInWayToHomePreviousDecisions;
    }

    public void setIsInWayToHomePreviousDecisions(List<Boolean> isInWayToHomePreviousDecisions) {
        this.isInWayToHomePreviousDecisions = isInWayToHomePreviousDecisions;
    }

    public List<Boolean> getIsInWayToWorkPreviousDecisions() {
        return isInWayToWorkPreviousDecisions;
    }

    public void setIsInWayToWorkPreviousDecisions(List<Boolean> isInWayToWorkPreviousDecisions) {
        this.isInWayToWorkPreviousDecisions = isInWayToWorkPreviousDecisions;
    }

    public double getPercentageDistanceBtwHomeAndWork() {
        return percentageDistanceBtwHomeAndWork;
    }

    public void setPercentageDistanceBtwHomeAndWork(double percentageDistanceBtwHomeAndWork) {
        this.percentageDistanceBtwHomeAndWork = percentageDistanceBtwHomeAndWork;
    }

    public UserDailyTimes getUserDailyTimes() {
        return userDailyTimes;
    }

    public void setUserDailyTimes(UserDailyTimes userDailyTimes) {
        this.userDailyTimes = userDailyTimes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DirectionsStatus getDirectionsStatus() {
        return directionsStatus;
    }

    public void setDirectionsStatus(DirectionsStatus directionsStatus) {
        this.directionsStatus = directionsStatus;
    }
}
