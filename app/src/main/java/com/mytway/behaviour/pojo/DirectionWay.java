package com.mytway.behaviour.pojo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DirectionWay {

    private static final String TAG = "DirectionWay";
    private static final int THREE_DISTANCES = 3;
    private static final double HOME_OR_WORK_ZONE = 300.0;
    private Boolean wayToWork = FALSE;
    private Boolean wayToHome = FALSE;
    private Boolean isInWork = FALSE;
    private Boolean isInHome = FALSE;

    private Context context;

    private Distance distanceBetweenHomeAndWork;
    private double percentageDistanceBtwHomeAndWork;

    private List<Double> distancesToHomeList = new LinkedList<>();
    private List<Double> distancesToWorkList = new LinkedList<>();

    private List<Boolean> isInWayToHomePreviousDecisions = new LinkedList<>();
    private List<Boolean> isInWayToWorkPreviousDecisions = new LinkedList<>();

    private List<Double> previousDistancesToHome = new LinkedList<>();
    private List<Double> previousDistancesToWork = new LinkedList<>();

    boolean isSavedArriveToHomeTimeExecuted = false;
    boolean isSavedLeaveHomeTimeExecuted = false;
    boolean isSavedStartWorkTimeExecuted = false;
    boolean isSavedLeaveWorkTimeExecuted = false;

    private UserDailyTimes userDailyTimes;

    public DirectionWay(Context context) {
        setContext(context);
        userDailyTimes =new UserDailyTimes(context);
    }

    public DirectionWay(boolean wayToWork, boolean wayToHome) {
        this.wayToWork = wayToWork;
        this.wayToHome = wayToHome;
    }

    public void setPreviousDistancesToHome(List<Double> previousDistancesToHome) {
        this.previousDistancesToHome = previousDistancesToHome;
    }

    public List<Double> getPreviousDistancesToHome() {
        return previousDistancesToHome;
    }

    public void decideTravelDirectionsAre(Position currentPosition, Session session){
        this.decideDirectionNew(currentPosition, session.getHomePlace(), session.getWorkPlace());
    }

    public void decideDirectionNew(Position currentPosition, Position homePosition, Position workPosition) {
        double currentDistToHomeInM = obtainDistanceBetweenInMeters(currentPosition, homePosition);
        double currentDistToWorkInM = obtainDistanceBetweenInMeters(currentPosition, workPosition);

        setFirstDirections(currentPosition, homePosition, workPosition, currentDistToHomeInM);

        if(isInHome()) {
            //can be isStillInHome or wayToWork
            if(isStillInHome(currentDistToHomeInM)){
                inHomeOperations();
            }else{
                inWayToWorkOperations();
                saveToFileDirections("IS IN WAY TO WORK");
            }
        }else if(isInWayToWork()){
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
        }else if(isInWork()){
            //can be isStillInWork or wayToHome
            if(isStillInWork(currentDistToWorkInM)){
                inWorkOperations();
            }else{
                inWayToHomeOperations();
                saveToFileDirections("IS IN WAY TO HOME");

            }
        }else if(isInWayToHome()){
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
        setIsInHome(TRUE);
        setIsInWork(FALSE);
        setWayToHome(FALSE);
        setWayToWork(FALSE);

        if(isLeaveWorkTimeNotNullAndWasntSavedBefore()){
            saveToFileDatabaseTimes("IN HOME - LeaveWork - " + userDailyTimes.getLeaveWorkTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            clearLeaveWorkTime();
        }

        setUpArriveToHomeTime();

        if(isArriveToHomeTimeNotNullAndWasntSavedBefore()) {
            saveToFileDatabaseTimes("IN HOME - ArriveToHome - " + userDailyTimes.getArriveToHomeTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            isSavedArriveToHomeTimeExecuted = true;
            clearArriveToHomeTime();
        }
    }

    public void inWayToWorkOperations() {
        saveToFile("SETUP IS IN WAY TO WORK");
        setIsInHome(FALSE);
        setIsInWork(FALSE);
        setWayToHome(FALSE);
        setWayToWork(TRUE);

        //setup leave home time
        setUpLeaveHomeTime();
    }

    public void inWorkOperations() {
        saveToFile("SETUP IS IN WORK");
        setIsInHome(FALSE);
        setIsInWork(TRUE);
        setWayToHome(FALSE);
        setWayToWork(FALSE);

        if(isLeaveHomeTimeNotNullAndWasntSavedBefore()){
            saveToFileDatabaseTimes("IN WORK - LeaveHome - " + userDailyTimes.getLeaveHomeTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            isSavedLeaveHomeTimeExecuted = true;
            clearLeaveHomeTime();
        }

        setUpStartWorkTime();

        if(isStartWorkTimeNotNullAndWasntSavedBefore()){
            saveToFileDatabaseTimes("IN WORK - StartWorkTime - " + userDailyTimes.getStartWorkTime().toString(UserDailyTimes.LOCAL_DATE_TIME_TO_STRING_FORMAT));
            isSavedStartWorkTimeExecuted = true;
        }
    }

    public void inWayToHomeOperations() {
        saveToFile("SETUP IS IN WAY TO HOME");
        setIsInHome(FALSE);
        setIsInWork(FALSE);
        setWayToHome(TRUE);
        setWayToWork(FALSE);

        clearStartWorkTime();

        //Setup leaveWorkTime
        setUpLeaveWorkTime();
    }

    public boolean isArriveToHomeTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getArriveToHomeTime() != null && !isSavedArriveToHomeTimeExecuted;
    }

    public boolean isLeaveWorkTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getLeaveWorkTime() != null && !isSavedLeaveWorkTimeExecuted;
    }

    public boolean isStartWorkTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getStartWorkTime() != null && !isSavedStartWorkTimeExecuted;
    }

    public boolean isLeaveHomeTimeNotNullAndWasntSavedBefore() {
        return userDailyTimes.getLeaveHomeTime() != null && !isSavedLeaveHomeTimeExecuted;
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
        isSavedArriveToHomeTimeExecuted = false;
    }

    public void clearLeaveWorkTime(){
        userDailyTimes.clearLeaveWorkTime();
        isSavedLeaveWorkTimeExecuted = false;
    }

    public void clearLeaveHomeTime(){
        userDailyTimes.clearLeaveHomeTime();
        isSavedLeaveHomeTimeExecuted = false;
    }

    public void clearStartWorkTime(){
        userDailyTimes.clearStartWorkTime();
        isSavedStartWorkTimeExecuted = false;
    }

    public boolean isStillInWork(double currentDistanceToWorkInMeters) {
        return PropertiesValues.SAFE_LENGTH_AROUND_HOME_AND_WORK_IN_METERS > currentDistanceToWorkInMeters;
    }

    public boolean isStillInHome(double currentDistanceToHomeInMeters) {
        return PropertiesValues.SAFE_LENGTH_AROUND_HOME_AND_WORK_IN_METERS > currentDistanceToHomeInMeters;
    }

    public void setFirstDirections(Position currentPosition, Position homePosition, Position workPosition, double currentDistanceToHomeInMeters) {
        if( !isInHome() && !isInWork() && !isInWayToWork() && !isInWayToHome()){
            decideIsInHome(currentPosition, homePosition);
            decideIsInWork(currentPosition, workPosition);
            obtainStartCurrentDirection(currentDistanceToHomeInMeters);
        }else{
            saveToFile("HOME: " + isInHome());
            saveToFile("WORK: " + isInWork());
            saveToFile("WAY HOME" + isInWayToHome());
            saveToFile("WAY WORK" + isInWayToWork());
        }
    }

    public void obtainStartCurrentDirection(double currentDistanceToHomeInMeters) {
//        saveToFile("WSZYSTKIE FLAGI FALSE, DEFAULT: is in home");
        if( !isInHome() && !isInWork() && !isInWayToWork() && !isInWayToHome()){
            //Countaing is user in way to home or work
            if(previousDistancesToHome.size() == 0){
                previousDistancesToHome.add(currentDistanceToHomeInMeters);
            }

            if(previousDistancesToHome.size() > 0){
                double previousDistanceToHome =  previousDistancesToHome.get(previousDistancesToHome.size() - 1);
                if(previousDistanceToHome > currentDistanceToHomeInMeters){
                    saveToFile("KIERUNEK: SETUP way to home");
                    setIsInHome(FALSE);
                    setIsInWork(FALSE);
                    setWayToHome(TRUE);
                    setWayToWork(FALSE);
                }else{
                    saveToFile("KIERUNEK ELSE: SETUP way to work");
                    setIsInHome(FALSE);
                    setIsInWork(FALSE);
                    setWayToHome(FALSE);
                    setWayToWork(TRUE);
                }
                previousDistancesToHome.add(currentDistanceToHomeInMeters);
                stayOnlyNewestDecisions(previousDistancesToHome);
            }
        }
    }

    public void stayOnlyNewestDecisions(List<Double> elements) {
        while(elements.size() >= 5){
            elements.subList(Math.max(elements.size() - 3, 0), elements.size()).clear();
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
        }

    }

    private boolean decideIsMoveWayToHomeBasedOnPreviousDecisions(List<Boolean> previousBooleansIsWayToHomeList) {
        saveToFile(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
        saveToFile(" previousBooleansIsWayToHomeList.size(): " + previousBooleansIsWayToHomeList.size());
        saveToFile(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");

        for(Boolean previousDecision : previousBooleansIsWayToHomeList){
            saveToFile("previousDecision HOME: " + previousDecision);
        }

        if(previousBooleansIsWayToHomeList.size() >= 2){
            saveToFile("wayToHome = " + wayToHome);
            return checkIsTrueDecisionIsMoreThenFalseInWayList(previousBooleansIsWayToHomeList);
        }else{
            Log.i(TAG, "isWayToHome list is less then three boolean");
            return false;
        }
    }

    public boolean decideIsMoveWayToWorkBasedOnPreviousDecisions(List<Boolean> previousBooleansIsWayToWorkList) {
        saveToFile(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
        saveToFile(" previousBooleansIsWayToWorkList.size(): " + previousBooleansIsWayToWorkList.size());
        saveToFile(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");

        for(Boolean previousDecision : previousBooleansIsWayToWorkList){
            saveToFile("previousDecision WORK: " + previousDecision);
        }

        if(previousBooleansIsWayToWorkList.size() >= 2){
//            wayToWork = checkIsTrueDecisionIsMoreThenFalseInWayList(previousBooleansIsWayToWorkList);
            saveToFile("wayToWork = " + wayToWork);
            return checkIsTrueDecisionIsMoreThenFalseInWayList(previousBooleansIsWayToWorkList);
        }else{
            Log.i(TAG, "isWayToHome list is less then three boolean");
            return false;
        }
    }

    private Boolean checkIsTrueDecisionIsMoreThenFalseInWayList(List<Boolean> previousBooleansIsWayList) {
        int trueDecision = 0;
        int falseDecision = 0;

        for(boolean decide : previousBooleansIsWayList){
            if(decide){
                trueDecision++;
            }else{
                falseDecision++;
            }
        }
        return trueDecision > falseDecision;
    }

    public Boolean obtainDirection(List<Double> previousDistancesList, double currentDistanceToPoint){

        int i = previousDistancesList.size() - 1;
        saveToFile(">>> currentDistanceToPoint: " + currentDistanceToPoint);
        saveToFile(">>> i: " + i);
        if(currentDistanceToPoint < previousDistancesList.get(i)){
//            saveToFile("Direction Decision: TRUE ");
            return TRUE;
        }else{
//            saveToFile("Direction Decision: FALSE ");
            return FALSE;
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
            setIsInHome(TRUE);
            setIsInWork(FALSE);
            setWayToHome(FALSE);
            setWayToWork(FALSE);

            saveToFile("\n<<<<<<<<<<<<<<<<  >>>>>>>>>>>>>>>>>>");
            saveToFile("<<<<<<<<<<<<<<<< IS IN HOME DECISION TRUE >>>>>>>>>>>>>>>>>>");
        }else{
            saveToFile("<<<<<<<<<<<<<<<< NOT IN HOME >>>>>>>>>>>>>>>>>>");
            setIsInHome(FALSE);

        }
    }

    public void decideIsInWork(Position currentPosition, Position workPosition){
        if( !isInHome() && !isInWork() && !isInWayToWork() && !isInWayToHome()){
            if(decideIsInPlace(currentPosition, workPosition)){
                setIsInWork(TRUE);
                setIsInHome(FALSE);
                setWayToHome(FALSE);
                setWayToWork(FALSE);

                saveToFile("\n\n<<<<<<<<<<<<<<<<  >>>>>>>>>>>>>>>>>>");
                saveToFile("<<<<<<<<<<<<<<<< IS IN WORK >>>>>>>>>>>>>>>>>>");

            }else{
                saveToFile("<<<<<<<<<<<<<<<< NOT IN WORK >>>>>>>>>>>>>>>>>>");
                setIsInWork(FALSE);
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

    public Boolean isWayToWork() {
        return wayToWork;
    }

    public void setWayToWork(boolean wayToWork) {
        this.wayToWork = wayToWork;
    }

    public Boolean isWayToHome() {
        return wayToHome;
    }

    public void setWayToHome(boolean wayToHome) {
        this.wayToHome = wayToHome;
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

    public Boolean getIsInWork() {
        return isInWork;
    }

    public void setIsInWork(Boolean isInWork) {
        this.isInWork = isInWork;
    }

    public Boolean getIsInHome() {
        return isInHome;
    }

    public void setIsInHome(Boolean isInHome) {
        this.isInHome = isInHome;
    }

    public boolean isInHome() {
        return isInHome && !isInWork && !wayToHome && !wayToWork;
    }

    public boolean isInWork() {
        return isInWork && !isInHome && !wayToHome && !wayToWork;
    }

    public boolean isInWayToWork() {
        return wayToWork && !isInWork && !isInHome && !wayToHome;
    }

    public boolean isInWayToHome() {
        return wayToHome && !isInWork && !isInHome && !wayToWork;
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

    public void setWayToWork(Boolean wayToWork) {
        this.wayToWork = wayToWork;
    }

    public void setWayToHome(Boolean wayToHome) {
        this.wayToHome = wayToHome;
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
}
