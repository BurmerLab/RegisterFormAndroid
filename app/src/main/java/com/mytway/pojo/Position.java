package com.mytway.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.utility.Session;

public class Position implements Parcelable {

    private static final double HOME_OR_WORK_ZONE = 0;
    private Double latitude;
    private Double longitude;

    public void decideIsHomeOrWorkPosition(Position currentPosition, Session session){
        double travelToHomeDistance = Distance.designateDistanceBetween(currentPosition, session.getHomePlace());
        double travelToWorkDistance = Distance.designateDistanceBetween(currentPosition, session.getWorkPlace());

//        if(travelToHomeDistance <= HOME_OR_WORK_ZONE)
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    public Position() {
    }

    public Position(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Position(Parcel in) {
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        public Position createFromParcel(Parcel source) {
            return new Position(source);
        }

        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position position = (Position) o;

        if (!latitude.equals(position.latitude)) return false;
        return longitude.equals(position.longitude);

    }

    @Override
    public int hashCode() {
        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        return result;
    }
}
