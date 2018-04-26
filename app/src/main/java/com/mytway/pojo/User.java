package com.mytway.pojo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;

import junit.framework.Assert;

public class User implements Parcelable {

    private int userId;
    private String userName;
    private String email;
    private String password;
    private TypeWork typeWork; //1- flex, 2- standard
    private String lengthTimeWork;
    private String startStandardTimeWork;
    private Position workPlace;
    private Position homePlace;
    private WorkWeek workWeek;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypeWork getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(TypeWork typeWork) {
        this.typeWork = typeWork;
    }

    public String getLengthTimeWork() {
        return lengthTimeWork;
    }

    public void setLengthTimeWork(String lengthTimeWork) {
        this.lengthTimeWork = lengthTimeWork;
    }

    public String getStartStandardTimeWork() {
        return startStandardTimeWork;
    }

    public void setStartStandardTimeWork(String startStandardTimeWork) {
        this.startStandardTimeWork = startStandardTimeWork;
    }

    public Position getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(Position workPlace) {
        this.workPlace = workPlace;
    }

    public Position getHomePlace() {
        return homePlace;
    }

    public void setHomePlace(Position homePlace) {
        this.homePlace = homePlace;
    }

    public WorkWeek getWorkWeek() {
        return workWeek;
    }

    public void setWorkWeek(WorkWeek workWeek) {
        this.workWeek = workWeek;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeInt(this.typeWork == null ? -1 : this.typeWork.ordinal());
        dest.writeString(this.lengthTimeWork);
        dest.writeString(this.startStandardTimeWork);
        dest.writeParcelable(this.workPlace, 0);
        dest.writeParcelable(this.homePlace, 0);
        dest.writeParcelable(this.workWeek, 0);
    }

    public User() {
    }

    private User(Parcel in) {
        this.userName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        int tmpTypeWork = in.readInt();
        this.typeWork = tmpTypeWork == -1 ? null : TypeWork.values()[tmpTypeWork];
        this.lengthTimeWork = in.readString();
        this.startStandardTimeWork = in.readString();
        this.workPlace = in.readParcelable(Position.class.getClassLoader());
        this.homePlace = in.readParcelable(Position.class.getClassLoader());
        this.workWeek = in.readParcelable(WorkWeek.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String decodeWorkWeekToString(WorkWeek workWeek){
        StringBuilder result = new StringBuilder();

        if(workWeek.getMonday()) result.append(1);
        else result.append(0);

        if(workWeek.getTuesday()) result.append(1);
        else result.append(0);

        if(workWeek.getWednesday()) result.append(1);
        else result.append(0);

        if(workWeek.getThursday()) result.append(1);
        else result.append(0);

        if(workWeek.getFriday()) result.append(1);
        else result.append(0);

        if(workWeek.getSaturday()) result.append(1);
        else result.append(0);

        if(workWeek.getSunday()) result.append(1);
        else result.append(0);

        return result.toString();
    }

    public String obtainTimeFromTitleString(String originalText){

        String[] time = originalText.replaceAll("\\s","").split(":", originalText.length());

        String result = time[1] +":"+ time[2];
        return result;

    }

    public User(String userName, String email, String password, TypeWork typeWork, String lengthTimeWork, String startStandardTimeWork, Position workPlace, Position homePlace, WorkWeek workWeek) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.typeWork = typeWork;
        this.lengthTimeWork = lengthTimeWork;
        this.startStandardTimeWork = startStandardTimeWork;
        this.workPlace = workPlace;
        this.homePlace = homePlace;
        this.workWeek = workWeek;
    }
}
