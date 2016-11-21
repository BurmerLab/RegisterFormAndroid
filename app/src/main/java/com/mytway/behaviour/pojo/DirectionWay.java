package com.mytway.behaviour.pojo;

import android.util.Log;

import com.mytway.pojo.Distance;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import java.util.LinkedList;
import java.util.List;

public class DirectionWay {

    private static final String TAG = "DirectionWay";
    private static final int THREE_DISTANCES = 3;
    private Boolean wayToWork;
    private Boolean wayToHome;
    private Distance distanceBetweenHomeAndWork;
    private List<Double> distancesToHome = new LinkedList<>();


    public DirectionWay() {
    }

    public DirectionWay(boolean wayToWork, boolean wayToHome) {
        this.wayToWork = wayToWork;
        this.wayToHome = wayToHome;
    }

    public void decideWhichDirectionIs(Position currentPosition, Session session){
        this.decideDirection(currentPosition, session.getHomePlace());
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

    //work on the same instances of DirectionWay because in distancesToHome is saved previous distances
    public void decideDirection(Position currentPosition, Position homePosition){
        double currentDistanceToHome = obtainDistanceToHome(currentPosition, homePosition);

        List<Boolean> previousDirectionsIsWayToHomeList =
                obtainListOfDirections(distancesToHome, currentDistanceToHome);

        double sevenPercentageOfDistanceBetweenHomeAndWork = 0;
        if(distanceBetweenHomeAndWork != null && distanceBetweenHomeAndWork.getValueInMeters() != 0){
            sevenPercentageOfDistanceBetweenHomeAndWork = distanceBetweenHomeAndWork.obtainSevenPercentFromDistance();
        }

        if(sevenPercentageOfDistanceBetweenHomeAndWork < currentDistanceToHome){
            if(previousDirectionsIsWayToHomeList.size() >= 3){
                boolean firstPreviousIsWayToHome = previousDirectionsIsWayToHomeList.get(previousDirectionsIsWayToHomeList.size()-1);
                boolean secondPreviousIsWayToHome = previousDirectionsIsWayToHomeList.get(previousDirectionsIsWayToHomeList.size()-2);
                boolean thirdPreviousIsWayToHome = previousDirectionsIsWayToHomeList.get(previousDirectionsIsWayToHomeList.size()-3);

                //if three last booleans is true, then direction is to home, if three last is false, then direction to work
                if(firstPreviousIsWayToHome && secondPreviousIsWayToHome && thirdPreviousIsWayToHome){
                    wayToHome = Boolean.TRUE;
                    wayToWork = Boolean.FALSE;
                }else if(!firstPreviousIsWayToHome && !secondPreviousIsWayToHome && !thirdPreviousIsWayToHome){
                    wayToHome = Boolean.FALSE;
                    wayToWork = Boolean.TRUE;
                }else{
                    wayToHome = Boolean.FALSE;
                    wayToWork = Boolean.FALSE;
                    Log.i(TAG, "Not decided because was no three decides (True or False) in a row");
                }
            }else{
                Log.i(TAG, "isWayToHome list is less then three boolean");
            }
        }
    }

    public List<Boolean> obtainListOfDirections(List<Double> previousDistancesToHomeList, double currentDistanceToHome){

        List<Boolean> isDirectionsIsToHome = new LinkedList<>();

            if(previousDistancesToHomeList.size() >= 1){
                if(currentDistanceToHome < previousDistancesToHomeList.get(0)){
                    isDirectionsIsToHome.add(Boolean.TRUE);
                }else{
                    isDirectionsIsToHome.add(Boolean.FALSE);
                }

                for(int i = 1; i <= previousDistancesToHomeList.size() - 1; i++){
                    if(previousDistancesToHomeList.get(i) < previousDistancesToHomeList.get(i - 1)
                            && currentDistanceToHome < previousDistancesToHomeList.get(i)){
                        isDirectionsIsToHome.add(Boolean.TRUE);
                    }else{
                        isDirectionsIsToHome.add(Boolean.FALSE);
                    }
                }
            }

    return isDirectionsIsToHome;
    }


    public double obtainDistanceToHome(Position currentPosition, Position homePlace){
        double distanceToHome = Distance.designateDistanceBetween(currentPosition, homePlace);
        return distanceToHome;
    }

    public int obtainDistanceToWork(Position currentPosition, Position workPlace){
        int distanceToWork = (int) Distance.designateDistanceBetween(currentPosition, workPlace);
        return distanceToWork;
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

    public List<Double> getDistancesToHome() {
        return distancesToHome;
    }

    public void setDistancesToHome(List<Double> distancesToHome) {
        this.distancesToHome = distancesToHome;
    }

    public Distance getDistanceBetweenHomeAndWork() {
        return distanceBetweenHomeAndWork;
    }

    public void setDistanceBetweenHomeAndWork(Distance distanceBetweenHomeAndWork) {
        this.distanceBetweenHomeAndWork = distanceBetweenHomeAndWork;
    }
}
