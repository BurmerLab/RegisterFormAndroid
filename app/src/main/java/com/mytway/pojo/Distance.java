package com.mytway.pojo;

public class Distance {

    private static final int KILOMETER_IN_METERS = 1000;
    private String text = "";
    private double valueInMeters = 0; //kilometers (double)
    private double valueInKilometers = 0;
    private final static int SEVEN_PERCENTAGE = 1;

    public Distance(String text, double valueInMeters) {
        this.text = text;
        this.valueInMeters = valueInMeters;
        this.valueInKilometers = valueInMeters / KILOMETER_IN_METERS;
    }

    public Distance() {
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

    public double obtainSevenPercentFromDistance(){
        double sevenPercentageFromDistance = (SEVEN_PERCENTAGE * valueInMeters) / 100;
        return sevenPercentageFromDistance;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getValueInMeters() {
        return valueInMeters;
    }

    public void setValueInMeters(double valueInMeters) {
        this.valueInMeters = valueInMeters;
    }
}
