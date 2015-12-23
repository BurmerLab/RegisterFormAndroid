package com.example.michal.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String userName;
    private String email;
    private String password;
    private int typeWork; //1- flex, 2- standard
    private String lengthTimeWork;
    private String startStandardTimeWork;

    public int getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(int typeWork) {
        this.typeWork = typeWork;
    }

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

    public void setLengthTimeWork(String lengthTimeWork) {
        this.lengthTimeWork = lengthTimeWork;
    }

    public String getLengthTimeWork() {
        return lengthTimeWork;
    }

    public void setStartTimeWork(String startTimeWork) {
        this.startStandardTimeWork = startTimeWork;
    }

    public String getStartStandardTimeWork() {
        return startStandardTimeWork;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(typeWork);
        dest.writeString(lengthTimeWork);
        dest.writeString(startStandardTimeWork);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User(Parcel in) {
        setUserName(in.readString());
        setEmail(in.readString());
        setPassword(in.readString());
        setTypeWork(in.readInt());
        setLengthTimeWork(in.readString());
        setStartTimeWork(in.readString());
    }

    public User() {
    }
}
