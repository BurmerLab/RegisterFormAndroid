package com.mytway.behaviour.pojo;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mytway.properties.PropertiesValues;
import com.mytway.properties.SharedPreferencesNames;

import static java.lang.Boolean.FALSE;

public class DirectionsStatus {

    private static final String WAY_TO_WORK = "wayToWork";
    private static final String WAY_TO_HOME = "wayToHome";
    private static final String IS_IN_WORK = "isInWork";
    private static final String IS_IN_HOME = "isInHome";
    private static final String TAG = "DirectionStatus";

    private SharedPreferences sharedPreferences;

    private Boolean wayToWork = FALSE;
    private Boolean wayToHome = FALSE;
    private Boolean isInWork = FALSE;
    private Boolean isInHome = FALSE;

    public DirectionsStatus(Context context) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            //do nothing
        }else{
            sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, 1);
            isWayToWork();
            isWayToHome();
            getIsInWork();
            getIsInHome();
        }
    }

    //------- way to work ---------------
    public void setWayToWork(Boolean wayToWork) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.wayToWork = wayToWork;
        }else{
            sharedPreferences.edit().putString(WAY_TO_WORK, Boolean.toString(wayToWork)).commit();
        }
    }

    public Boolean isWayToWork() {
        Boolean wayToWork;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wayToWork = this.wayToWork;
        }else{
            String wayToWorkString = sharedPreferences.getString(WAY_TO_WORK, "");
            if(wayToWorkString.equals("")){
                wayToWork = false;
            }else{
                wayToWork = Boolean.valueOf(wayToWorkString);
                this.wayToWork = wayToWork;
            }
        }
        return wayToWork;
    }

    public void clearIsWayToWork() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearIsWayToWork");
        }else{
            sharedPreferences.edit().putString(WAY_TO_WORK, "").commit();
        }
    }

    //------- inWayToHome ---------------
    public void setWayToHome(Boolean wayToHome) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.wayToHome = wayToHome;
        }else{
            sharedPreferences.edit().putString(WAY_TO_HOME, Boolean.toString(wayToHome)).commit();
        }
    }

    public Boolean isWayToHome() {
        Boolean wayToHome;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wayToHome = this.wayToHome;
        }else{
            String wayToHomeString = sharedPreferences.getString(WAY_TO_HOME, "");
            if(wayToHomeString.equals("")){
                wayToHome = false;
            }else{
                wayToHome = Boolean.valueOf(wayToHomeString);
                this.wayToHome = wayToHome;
            }
        }
        return wayToHome;
    }

    public void clearIsWayToHome() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearIsWayToHome");
        }else{
            sharedPreferences.edit().putString(WAY_TO_HOME, "").commit();
        }
    }


//------- is in work ---------------
    public void setIsInWork(Boolean isInWork) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.isInWork = isInWork;
        }else{
            sharedPreferences.edit().putString(IS_IN_WORK, Boolean.toString(isInWork)).commit();
        }
    }

    public Boolean getIsInWork() {
        Boolean isInWork;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            isInWork = this.isInWork;
        }else{
            String isInWorkString = sharedPreferences.getString(IS_IN_WORK, "");
            if(isInWorkString.equals("")){
                isInWork = false;
            }else{
                isInWork = Boolean.valueOf(isInWorkString);
                this.isInWork = isInWork;
            }
        }
        return isInWork;
    }

    public void clearIsInWork() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearIsInWork");
        }else{
            sharedPreferences.edit().putString(IS_IN_WORK, "").commit();
        }
    }


    //------- is in Home ---------------
    public void setIsInHome(Boolean isInHome) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.isInHome = isInHome;
        }else{
            sharedPreferences.edit().putString(IS_IN_HOME, Boolean.toString(isInHome)).commit();
        }
    }

    public Boolean getIsInHome() {
        Boolean isInHome;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            isInHome = this.isInHome;
        }else{
            String isInHomeString = sharedPreferences.getString(IS_IN_HOME, "");
            if(isInHomeString.equals("")){
                isInHome = false;
            }else{
                isInHome = Boolean.valueOf(isInHomeString);
                this.isInHome = isInHome;
            }
        }
        return isInHome;
    }

    public void clearIsInHome() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearIsInHome");
        }else{
            sharedPreferences.edit().putString(IS_IN_HOME, "").commit();
        }
    }

    public boolean isInHome() {
        return getIsInHome();
    }

    public boolean isInWork() {
        return getIsInWork();
    }

    public boolean isInWayToWork() {
        return isWayToWork();
    }

    public boolean isInWayToHome() {
        return isWayToHome();
    }

}
