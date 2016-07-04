package com.mytway.behaviour;

import com.mytway.pojo.Position;
import com.mytway.pojo.User;

public class GeolocalizationProcessor {

    UserBehaviour userBehaviour;
    String message;

    public void processGeolocalization(UserBehaviour userBehaviour){

        User user = userBehaviour.getUser();
        Position homePlace = user.getHomePlace();
        Position workPlace = user.getWorkPlace();
    }

//    private double calcuateDistance(Point nodeFirst, Point nodeSecond){
//        // HAVERSINE FORMULA- do wyznaczania odleglosci
//        double R = 6371; // [km] promien od centrum do powierzchni ziemi
//        double dLat = (nodeFirst.latitude - nodeSecond.latitude) * Math.PI / 180;
//        double dLon = (nodeFirst.longitude - nodeSecond.longitude) * Math.PI / 180;
//        double lat1 = nodeFirst.latitude * Math.PI / 180;
//        double lat2 = nodeSecond.latitude * Math.PI / 180;
//
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = R * c;
//        return distance;
//    }

    //to samo dla drogi do domu zrobic
//    private boolean isUserStartedRideToWork(Position currentPosition, Position homePosition){
//        //A* sprawdzajacy czy odleglosc pomiedzy aktualna pozycja a punktem
//
//    }



}
