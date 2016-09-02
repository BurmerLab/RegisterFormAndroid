package com.mytway.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mytway.pojo.Position;
import com.mytway.properties.SharedPreferencesNames;

public class Session {

    private String userName;
    private Boolean isUserLogged;
    private int typeWork;
    private String lengthTimeWork;
    private String startStandardTimeWork;
    private String workWeek;
    private String homeLatitude;
    private String homeLongitude;
    private String workLatitude;
    private String workLongitude;

    private Position homePlace;
    private Position workPlace;


    private SharedPreferences sharedPreferences;

    public Session() {
    }

    public Session(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, 1);
        getUserName();
        isUserLogged();
        getTypeWork();
        getLengthTimeWork();
        getStartStandardTimeWork();
        getWorkWeek();
        getHomeLatitude();
        getHomeLongitude();
        getWorkLatitude();
        getWorkLongitude();
    }

    //------- UserName ---------------
    public void setUserName(String userName) {
        sharedPreferences.edit().putString("userName", userName).commit();
    }

    public String getUserName() {
        String userName = sharedPreferences.getString("userName","");
        this.userName = userName;
        return userName;
    }

    //------- isUserLogged ---------------
    public void setIsUserLogged(Boolean isUserLogged) {
        sharedPreferences.edit().putBoolean("isUserLogged", isUserLogged).commit();
    }

    public Boolean isUserLogged() {
        Boolean isUserLogged = sharedPreferences.getBoolean("isUserLogged", false);
        this.isUserLogged = isUserLogged;
        return isUserLogged;
    }

    //------- TypeWork ---------------
    public void setTypeWork(int typeWork) {
        sharedPreferences.edit().putInt("typeWork", typeWork).commit();
    }

    public int getTypeWork() {
        int typeWork = sharedPreferences.getInt("typeWork", 0);
        this.typeWork = typeWork;
        return typeWork;
    }

    //------- lengthTimeWork ---------------
    public void setLengthTimeWork(String lengthTimeWork) {
        sharedPreferences.edit().putString("lengthTimeWork", lengthTimeWork).commit();
    }

    public String getLengthTimeWork() {
        String lengthTimeWork = sharedPreferences.getString("lengthTimeWork", "");
        this.lengthTimeWork = lengthTimeWork;
        return lengthTimeWork;
    }

    //------- startStandardTimeWork ---------------
    public void setStartStandardTimeWork(String startStandardTimeWork) {
        sharedPreferences.edit().putString("startStandardTimeWork", startStandardTimeWork).commit();
    }

    public String getStartStandardTimeWork() {
        String startStandardTimeWork = sharedPreferences.getString("startStandardTimeWork", "");
        this.startStandardTimeWork = startStandardTimeWork;
        return startStandardTimeWork;
    }

    //------- workWeek ---------------
    public void setWorkWeek(String workWeek) {
        sharedPreferences.edit().putString("workWeek", workWeek).commit();
    }

    public String getWorkWeek() {
        String workWeek = sharedPreferences.getString("workWeek", "");
        this.workWeek = workWeek;
        return workWeek;
    }

    //------- homeLatitude ---------------
    public void setHomeLatitude(String homeLatitude) {
        sharedPreferences.edit().putString("homeLatitude", homeLatitude).commit();
    }

    public String getHomeLatitude() {
        String homeLatitude = sharedPreferences.getString("homeLatitude", "");
        this.homeLatitude = homeLatitude;
        return homeLatitude;
    }

    //------- homeLongitude ---------------
    public void setHomeLongitude(String homeLongitude) {
        sharedPreferences.edit().putString("homeLongitude", homeLongitude).commit();
    }

    public String getHomeLongitude() {
        String homeLongitude = sharedPreferences.getString("homeLongitude", "");
        this.homeLongitude = homeLongitude;
        return homeLongitude;
    }

    //------- workLatitude ---------------
    public void setWorkLatitude(String workLatitude) {
        sharedPreferences.edit().putString("workLatitude", workLatitude).commit();
    }

    public String getWorkLatitude() {
        String workLatitude = sharedPreferences.getString("workLatitude", "");
        this.workLatitude = workLatitude;
        return workLatitude;
    }

    //------- workLongitude ---------------
    public void setWorkLongitude(String workLongitude) {
        sharedPreferences.edit().putString("workLongitude", workLongitude).commit();
    }

    public String getWorkLongitude() {
        String workLongitude = sharedPreferences.getString("workLongitude", "");
        this.workLongitude = workLongitude;
        return workLongitude;
    }

    public SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public double parseStringToDouble(String value){
        double parsedValue = Double.parseDouble(value);
        return parsedValue;
    }


    public Position getHomePlace() {
        if(getHomeLatitude() != null && getHomeLongitude() != null){
            Position position = new Position(parseStringToDouble(getHomeLatitude()), parseStringToDouble(getHomeLongitude()));
            this.homePlace = position;
        }
        return homePlace;
    }

    public void setHomePlace(Position homePlace) {
        this.homePlace = homePlace;
    }

    public Position getWorkPlace() {
        if(getWorkLatitude() != null && getWorkLongitude() != null){
            Position position = new Position(parseStringToDouble(getWorkLatitude()), parseStringToDouble(getWorkLongitude()));
            this.workPlace = position;
        }
        return workPlace;
    }

    public void setWorkPlace(Position workPlace) {
        this.workPlace = workPlace;
    }

}

/*How to use:
*
* private Session session;//global variable
session = new Session(cntx); //in oncreate
//and now we set sharedpreference then use this like

session.setUserName("USERNAME");
more: http://stackoverflow.com/questions/20678669/how-to-maintain-session-in-android
*/