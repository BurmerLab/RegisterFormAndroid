package com.mytway.behaviour.pojo;

import android.os.Environment;
import android.util.Log;

import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class DirectionWay {

    private static final String TAG = "DirectionWay";
    private static final int THREE_DISTANCES = 3;
    private static final double HOME_OR_WORK_ZONE = 300.0;
    private static final int METERS_IN_KILOMETER = 1000;
    private Boolean wayToWork = Boolean.FALSE;
    private Boolean wayToHome = Boolean.FALSE;
    private Boolean isInWork = Boolean.FALSE;
    private Boolean isInHome = Boolean.FALSE;

    private Distance distanceBetweenHomeAndWork;
    private List<Double> distancesToHomeList = new LinkedList<>();
    private List<Double> distancesToWorkList = new LinkedList<>();
    private LocalDateTime leaveHomeToGoToWorkTime;
    private LocalDateTime startWorkTime;

    public DirectionWay() {
    }

    public DirectionWay(boolean wayToWork, boolean wayToHome) {
        this.wayToWork = wayToWork;
        this.wayToHome = wayToHome;
    }

    public void decideWhichDirectionIs(Position currentPosition, Session session){
        this.decideDirection(currentPosition, session.getHomePlace(), session.getWorkPlace());
    }

    public static double designateDistanceBetween(Position startPosition, Position endPosition){
        // HAVERSINE FORMULA- to designate distanceBetweenHomeAndWork between points
        double R = 6371; // [km] promien od centrum do powierzchni ziemi
        double dLatitude = (startPosition.getLatitude() - endPosition.getLatitude()) * Math.PI / 180;
        double dLongitude = (startPosition.getLongitude() - endPosition.getLongitude()) * Math.PI / 180;
        double latitudeFirst = startPosition.getLatitude() * Math.PI / 180;
        double latitudeSecond = endPosition.getLatitude() * Math.PI / 180;

        double a = Math.sin(dLatitude / 2) * Math.sin(dLatitude / 2) +
                Math.sin(dLongitude / 2) * Math.sin(dLongitude / 2) *
                Math.cos(latitudeFirst) * Math.cos(latitudeSecond);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }

    //work on the same instances of DirectionWay because in distancesToHomeList is saved previous distances
    public void decideDirection(Position currentPosition, Position homePosition, Position workPosition){
        double currentDistanceToHomeInMeters = obtainDistanceBetweenInMeters(currentPosition, homePosition);
        double currentDistanceToWorkInMeters = obtainDistanceBetweenInMeters(currentPosition, workPosition);

        distancesToHomeList.add(currentDistanceToHomeInMeters);
        distancesToWorkList.add(currentDistanceToWorkInMeters);

        List<Boolean> previousBooleansIsWayToHomeList =
                obtainListOfDirections(distancesToHomeList, currentDistanceToHomeInMeters);

        List<Boolean> previousBooleansIsWayToWorkList =
                obtainListOfDirections(distancesToWorkList, currentDistanceToWorkInMeters);

        double sevenPercentageOfDistanceBetweenHomeAndWorkInMeters = 0;
        if(distanceBetweenHomeAndWork != null && distanceBetweenHomeAndWork.getValueInMeters() != 0){
            sevenPercentageOfDistanceBetweenHomeAndWorkInMeters = distanceBetweenHomeAndWork.obtainSevenPercentFromDistance();
        }

        saveToFile("===================decideDirection()============================");
        saveToFile("currentPosition: " + currentPosition.getLongitude() + " , " + currentPosition.getLatitude());
        saveToFile("currentDistanceToHomeInMeters: " + currentDistanceToHomeInMeters);
        saveToFile("currentDistanceToWorkInMeters: " + currentDistanceToWorkInMeters);
        saveToFile("sevenPercentageOfDistanceBetweenHomeAndWorkInMeters: " + sevenPercentageOfDistanceBetweenHomeAndWorkInMeters);
        saveToFile("===============================================");


        if(sevenPercentageOfDistanceBetweenHomeAndWorkInMeters < currentDistanceToHomeInMeters){
            decideIsMoveWayToHome(previousBooleansIsWayToHomeList);
            decideIsMoveWayToWork(previousBooleansIsWayToWorkList);
        }else{
            wayToHome = Boolean.FALSE;
            wayToWork = Boolean.FALSE;
        }

    }

    public static void saveToFile(String content) {
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

    private void decideIsMoveWayToHome(List<Boolean> previousBooleansIsWayToHomeList) {

        saveToFile(" previousBooleansIsWayToHomeList.size(): " + previousBooleansIsWayToHomeList.size());

        if(previousBooleansIsWayToHomeList.size() >= 2){

            int i = previousBooleansIsWayToHomeList.size() - 1;
            //check last 3 decides, if one is true, then decide that user is in way to home
            if(previousBooleansIsWayToHomeList.get(i) || previousBooleansIsWayToHomeList.get(i - 1)
                    || previousBooleansIsWayToHomeList.get(i - 2)){
                saveToFile(">>> previousBooleansIsWayToHomeList.get(i): " + previousBooleansIsWayToHomeList.get(i));
                saveToFile(">>> previousBooleansIsWayToHomeList.get(i - 1): " + previousBooleansIsWayToHomeList.get(i - 1));
                saveToFile(">>> previousBooleansIsWayToHomeList.get(i - 2): " + previousBooleansIsWayToHomeList.get(i - 2));
                saveToFile("Way to home = true");
                wayToHome = TRUE;
            }else{
                saveToFile("IS NOT IN WAY TO HOME - isDirectionsIsToPointList.add(FALSE)");
                wayToHome = Boolean.FALSE;
                Log.i(TAG, "Not decided because was no two decides (True or False) in a row");
            }
        }else{
            Log.i(TAG, "isWayToHome list is less then three boolean");
        }
    }

    private void decideIsMoveWayToWork(List<Boolean> previousBooleansIsWayToWorkList) {
        saveToFile(" previousBooleansIsWayToWorkList.size(): " + previousBooleansIsWayToWorkList.size());

        if(previousBooleansIsWayToWorkList.size() >= 2){

            int i = previousBooleansIsWayToWorkList.size() - 1;
            //check last 3 decides, if one is true, then decide that user is in way to work
            if(previousBooleansIsWayToWorkList.get(i) || previousBooleansIsWayToWorkList.get(i - 1)
                    || previousBooleansIsWayToWorkList.get(i - 2)){
                saveToFile(">>> previousBooleansIsWayToWorkList.get(i): " + previousBooleansIsWayToWorkList.get(i));
                saveToFile(">>> previousBooleansIsWayToWorkList.get(i - 1): " + previousBooleansIsWayToWorkList.get(i - 1));
                saveToFile(">>> previousBooleansIsWayToWorkList.get(i - 2): " + previousBooleansIsWayToWorkList.get(i - 2));
                saveToFile("Way to work = true");
                wayToWork = TRUE;
            }else{
                saveToFile("IS NOT IN WAY TO work - isDirectionsIsToPointList.add(FALSE)");
                wayToWork = Boolean.FALSE;
                Log.i(TAG, "Not decided because was no two decides (True or False) in a row");
            }
        }else{
            Log.i(TAG, "isWayToWork list is less then three boolean");
        }
    }

    public List<Boolean> obtainListOfDirections(List<Double> previousDistancesList, double currentDistanceToPoint){

        List<Boolean> isDirectionsIsToPointList = new LinkedList<>();

        saveToFile("---------previousDistance List:------- ");
        for(Double previousDistance : previousDistancesList){
            saveToFile("previousDistance: " + previousDistance);
        }
        saveToFile("----------------------------- ");

        saveToFile(">>> currentDistanceToPoint: " + currentDistanceToPoint);

        int i = previousDistancesList.size() - 1;
        if(currentDistanceToPoint < previousDistancesList.get(i)){
            saveToFile(">>> currentDistanceToPoint: " + currentDistanceToPoint);
            saveToFile(">>> previousDistancesList.get(i): " + previousDistancesList.get(i));
            saveToFile("isDirectionsIsToPointList.add(TRUE) ");
            isDirectionsIsToPointList.add(TRUE);
        }else{
            saveToFile("isDirectionsIsToPointList.add(FALSE) ");
            isDirectionsIsToPointList.add(Boolean.FALSE);
        }

        return isDirectionsIsToPointList;
    }


    public double obtainDistanceBetweenInMeters(Position currentPosition, Position secondPlace){
        double distanceBetween = Distance.designateDistanceBetween(currentPosition, secondPlace);
        return distanceBetween * METERS_IN_KILOMETER;
    }

    public int obtainDistanceToWork(Position currentPosition, Position workPlace){
        int distanceToWork = (int) Distance.designateDistanceBetween(currentPosition, workPlace);
        return distanceToWork;
    }

    public void decideIsInHome(Position currentPosition, Position homePosition){
        if(decideIsInPlace(currentPosition, homePosition)){
            setIsInHome(TRUE);
            setIsInWork(Boolean.FALSE);
        }else{
            setIsInHome(Boolean.FALSE);
            setIsInWork(Boolean.FALSE);

            //todo: test it:
            //set time when user leaved home,
            //if leaveHome time is not inserted, then insert current time
            if(leaveHomeToGoToWorkTime == null){
                leaveHomeToGoToWorkTime = new LocalDateTime();
            }
        }
    }

    public void decideIsInWork(Position currentPosition, Position workPosition){
        if(decideIsInPlace(currentPosition, workPosition)){
            setIsInHome(Boolean.FALSE);
            setIsInWork(TRUE);

            //if is no start work time, then insert current because user arrived to work
            if(startWorkTime == null){
                startWorkTime = new LocalDateTime();
            }
        }else{
            setIsInWork(Boolean.FALSE);
        }
    }

    public boolean decideIsInPlace(Position currentPosition, Position place) {
        double distanceToHome = Distance.designateDistanceBetween(currentPosition, place);

        double distanceBetweens = distanceToHome * 1000;

        saveToFile(" distance: " + distanceToHome);
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

    public LocalDateTime getLeaveHomeToGoToWorkTime() {
        return leaveHomeToGoToWorkTime;
    }

    public void setLeaveHomeToGoToWorkTime(LocalDateTime leaveHomeToGoToWorkTime) {
        this.leaveHomeToGoToWorkTime = leaveHomeToGoToWorkTime;
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

    public LocalDateTime getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(LocalDateTime startWorkTime) {
        this.startWorkTime = startWorkTime;
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
}
