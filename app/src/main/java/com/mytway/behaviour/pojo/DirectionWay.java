package com.mytway.behaviour.pojo;

import android.util.Log;

import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.util.LinkedList;
import java.util.List;

public class DirectionWay {

    private static final String TAG = "DirectionWay";
    private static final int THREE_DISTANCES = 3;
    private static final double HOME_OR_WORK_ZONE = 300.0;
    private static final int METERS_IN_KILOMETER = 1000;
    private Boolean wayToWork;
    private Boolean wayToHome;
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

        List<Boolean> previousBooleansIsWayToHomeList =
                obtainListOfDirections(distancesToHomeList, currentDistanceToHomeInMeters);

        List<Boolean> previousBooleansIsWayToWorkList =
                obtainListOfDirections(distancesToWorkList, currentDistanceToWorkInMeters);

        double sevenPercentageOfDistanceBetweenHomeAndWorkInMeters = 0;
        if(distanceBetweenHomeAndWork != null && distanceBetweenHomeAndWork.getValueInMeters() != 0){
            sevenPercentageOfDistanceBetweenHomeAndWorkInMeters = distanceBetweenHomeAndWork.obtainSevenPercentFromDistance();
        }

        if(sevenPercentageOfDistanceBetweenHomeAndWorkInMeters < currentDistanceToHomeInMeters){
            decideIsMoveWayToHome(previousBooleansIsWayToHomeList);
            decideIsMoveWayToWork(previousBooleansIsWayToWorkList);
        }else{
            wayToHome = Boolean.FALSE;
            wayToWork = Boolean.FALSE;
        }
    }

    private void decideIsMoveWayToHome(List<Boolean> previousBooleansIsWayToHomeList) {
        if(previousBooleansIsWayToHomeList.size() >= 2){
            boolean firstPreviousIsWayToHome = previousBooleansIsWayToHomeList.get(previousBooleansIsWayToHomeList.size()-1);
            boolean secondPreviousIsWayToHome = previousBooleansIsWayToHomeList.get(previousBooleansIsWayToHomeList.size()-2);

            //if two last booleans is true, then direction is to home, if three last is false, then direction to work
            if(firstPreviousIsWayToHome && secondPreviousIsWayToHome){
                wayToHome = Boolean.TRUE;
                //todo: is it need to add leaveWorkToGoToHomeTime?
            }else{
                wayToHome = Boolean.FALSE;
                Log.i(TAG, "Not decided because was no two decides (True or False) in a row");
            }
        }else{
            Log.i(TAG, "isWayToHome list is less then three boolean");
        }
    }

    private void decideIsMoveWayToWork(List<Boolean> previousBooleansIsWayToWorkList) {
        if(previousBooleansIsWayToWorkList.size() >= 2){
            boolean firstPreviousIsWayToWork = previousBooleansIsWayToWorkList.get(previousBooleansIsWayToWorkList.size()-1);
            boolean secondPreviousIsWayToWork = previousBooleansIsWayToWorkList.get(previousBooleansIsWayToWorkList.size() - 2);

            //if two last booleans is true, then direction is to home, if three last is false, then direction to work
            if(firstPreviousIsWayToWork && secondPreviousIsWayToWork){
                wayToWork = Boolean.TRUE;
                setLeaveHomeToGoToWorkTime(new LocalDateTime());
            }else{
                wayToWork = Boolean.FALSE;
                Log.i(TAG, "Not decided because was no two decides (True or False) in a row");
            }
        }else{
            Log.i(TAG, "isWayToHome list is less then three boolean");
        }
    }

    public List<Boolean> obtainListOfDirections(List<Double> previousDistancesList, double currentDistanceToHome){

        List<Boolean> isDirectionsIsToHomeList = new LinkedList<>();

            if(previousDistancesList.size() >= 1){
                if(currentDistanceToHome < previousDistancesList.get(0)){
                    isDirectionsIsToHomeList.add(Boolean.TRUE);
                }else{
                    isDirectionsIsToHomeList.add(Boolean.FALSE);
                }

                for(int i = 1; i <= previousDistancesList.size() - 1; i++){
                    if(previousDistancesList.get(i) < previousDistancesList.get(i - 1)
                            && currentDistanceToHome < previousDistancesList.get(i)){
                        isDirectionsIsToHomeList.add(Boolean.TRUE);
                    }else{
                        isDirectionsIsToHomeList.add(Boolean.FALSE);
                    }
                }
            }

            previousDistancesList.add(currentDistanceToHome);

    return isDirectionsIsToHomeList;
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
            setIsInHome(Boolean.TRUE);
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
            setIsInWork(Boolean.TRUE);

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

        if(distanceBetweens < HOME_OR_WORK_ZONE){
            Log.i(TAG, "User is in Place");
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
