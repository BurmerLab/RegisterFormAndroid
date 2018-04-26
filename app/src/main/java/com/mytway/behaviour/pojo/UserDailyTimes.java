package com.mytway.behaviour.pojo;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mytway.properties.PropertiesValues;
import com.mytway.properties.SharedPreferencesNames;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class UserDailyTimes {

    public static final String LOCAL_DATE_TIME_TO_STRING_FORMAT = "yyyy-MM-dd HH:mm";

    private static final String TAG = "UserDailyTimes";
    public static final String LEAVE_HOME_TIME = "leaveHomeTime";
    public static final String START_WORK_TIME = "startWorkTime";
    public static final String LEAVE_WORK_TIME = "leaveWorkTime";
    public static final String ARRIVE_TO_HOME_TIME = "arriveToHomeTime";

    public static final String WAS_SAVED_ARRIVE_TO_HOME_TIME_BEFORE = "wasSavedArriveToHomeTimeBefore";
    public static final String WAS_SAVED_LEAVE_HOME_TIME_BEFORE = "wasSavedLeaveHomeTimeBefore";
    public static final String WAS_SAVED_START_WORK_TIME_BEFORE = "wasSavedStartWorkTimeBefore";
    public static final String WAS_SAVED_LEAVE_WORK_TIME_BEFORE = "wasSavedLeaveWorkTimeBefore";

    private LocalDateTime leaveHomeTime;
    private LocalDateTime startWorkTime;
    private LocalDateTime leaveWorkTime;
    private LocalDateTime arriveToHomeTime;

    boolean wasSavedArriveToHomeTimeBefore = false;
    boolean wasSavedLeaveHomeTimeBefore = false;
    boolean wasSavedStartWorkTimeBefore = false;
    boolean wasSavedLeaveWorkTimeBefore = false;

    private SharedPreferences sharedPreferences;

    public UserDailyTimes(Context context) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            //do nothing
        }else{
            sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, 1);
            getLeaveHomeTime();
            getArriveToHomeTime();
            getLeaveWorkTime();
            getStartWorkTime();
            getWasSavedArriveToHomeTimeBefore();
            getWasSavedLeaveHomeTimeBefore();
            getWasSavedLeaveWorkTimeBefore();
            getWasSavedLeaveWorkTimeBefore();
        }
    }


    //-----------------------------------------------------FLAGS-------------------------------------
    //------- wasSavedLeaveWorkTimeBefore ---------------
    public void setWasSavedLeaveWorkTimeBefore(boolean wasSavedLeaveWorkTimeBefore) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.wasSavedLeaveWorkTimeBefore = wasSavedLeaveWorkTimeBefore;
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_LEAVE_WORK_TIME_BEFORE, String.valueOf(wasSavedLeaveWorkTimeBefore)).commit();
        }
    }

    public boolean getWasSavedLeaveWorkTimeBefore() {
        boolean wasSavedLeaveWorkTimeBefore;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wasSavedLeaveWorkTimeBefore = this.wasSavedLeaveWorkTimeBefore;
        }else{
            String wasSavedLeaveWorkTimeBeforeString = sharedPreferences.getString(WAS_SAVED_LEAVE_WORK_TIME_BEFORE, "");
            if(wasSavedLeaveWorkTimeBeforeString.equals("")){
                wasSavedLeaveWorkTimeBefore = false;
            }else{
                this.wasSavedLeaveWorkTimeBefore = Boolean.valueOf(wasSavedLeaveWorkTimeBeforeString);
                wasSavedLeaveWorkTimeBefore = Boolean.valueOf(wasSavedLeaveWorkTimeBeforeString);
            }
        }
        return wasSavedLeaveWorkTimeBefore;
    }

    public void clearWasSavedLeaveWorkTimeBefore() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearWasSavedLeaveWorkTimeBefore");
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_LEAVE_WORK_TIME_BEFORE, "").commit();
        }
    }

    //------- wasSavedStartWorkTimeBefore ---------------
    public void setWasSavedStartWorkTimeBefore(boolean wasSavedStartWorkTimeBefore) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.wasSavedStartWorkTimeBefore = wasSavedStartWorkTimeBefore;
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_START_WORK_TIME_BEFORE, String.valueOf(wasSavedStartWorkTimeBefore)).commit();
        }
    }

    public boolean getWasSavedStartWorkTimeBefore() {
        boolean wasSavedStartWorkTimeBefore;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wasSavedStartWorkTimeBefore = this.wasSavedStartWorkTimeBefore;
        }else{
            String wasSavedStartWorkTimeBeforeString = sharedPreferences.getString(WAS_SAVED_START_WORK_TIME_BEFORE, "");
            if(wasSavedStartWorkTimeBeforeString.equals("")){
                wasSavedStartWorkTimeBefore = false;
            }else{
                this.wasSavedStartWorkTimeBefore = Boolean.valueOf(wasSavedStartWorkTimeBeforeString);
                wasSavedStartWorkTimeBefore = Boolean.valueOf(wasSavedStartWorkTimeBeforeString);
            }
        }
        return wasSavedStartWorkTimeBefore;
    }

    public void clearWasSavedStartWorkTimeBefore() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearWasSavedStartWorkTimeBefore");
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_START_WORK_TIME_BEFORE, "").commit();
        }
    }

    //------- wasSavedLeaveHomeTimeBefore ---------------
    public void setWasSavedLeaveHomeTimeBefore(boolean wasSavedLeaveHomeTimeBefore) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.wasSavedLeaveHomeTimeBefore = wasSavedLeaveHomeTimeBefore;
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_LEAVE_HOME_TIME_BEFORE, String.valueOf(wasSavedLeaveHomeTimeBefore)).commit();
        }
    }

    public boolean getWasSavedLeaveHomeTimeBefore() {
        boolean wasSavedLeaveHomeTimeBefore;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wasSavedLeaveHomeTimeBefore = this.wasSavedLeaveHomeTimeBefore;
        }else{
            String wasSavedLeaveHomeTimeBeforeString = sharedPreferences.getString(WAS_SAVED_LEAVE_HOME_TIME_BEFORE, "");
            if(wasSavedLeaveHomeTimeBeforeString.equals("")){
                wasSavedLeaveHomeTimeBefore = false;
            }else{
                this.wasSavedLeaveHomeTimeBefore = Boolean.valueOf(wasSavedLeaveHomeTimeBeforeString);
                wasSavedLeaveHomeTimeBefore = Boolean.valueOf(wasSavedLeaveHomeTimeBeforeString);
            }
        }
        return wasSavedLeaveHomeTimeBefore;
    }

    public void clearWasSavedLeaveHomeTimeBefore() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearWasSavedLeaveHomeTimeBefore");
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_LEAVE_HOME_TIME_BEFORE, "").commit();
        }
    }

    //------- wasSavedArriveToHomeTimeBefore ---------------
    public void setWasSavedArriveToHomeTimeBefore(boolean wasSavedArriveToHomeTimeBefore) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.wasSavedArriveToHomeTimeBefore = wasSavedArriveToHomeTimeBefore;
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_ARRIVE_TO_HOME_TIME_BEFORE,
                    String.valueOf(wasSavedArriveToHomeTimeBefore))
                    .commit();
        }
    }

    public boolean getWasSavedArriveToHomeTimeBefore() {
        boolean wasSavedArriveToHomeTimeBefore;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wasSavedArriveToHomeTimeBefore = this.wasSavedArriveToHomeTimeBefore;
        }else{
            String wasSavedArriveToHomeTimeBeforeString = sharedPreferences.getString(WAS_SAVED_ARRIVE_TO_HOME_TIME_BEFORE, "");
            if(wasSavedArriveToHomeTimeBeforeString.equals("")){
                wasSavedArriveToHomeTimeBefore = false;
            }else{
                this.wasSavedArriveToHomeTimeBefore = Boolean.valueOf(wasSavedArriveToHomeTimeBeforeString);
                wasSavedArriveToHomeTimeBefore = Boolean.valueOf(wasSavedArriveToHomeTimeBeforeString);
            }
        }
        return wasSavedArriveToHomeTimeBefore;
    }

    public void clearWasSavedArriveToHomeTimeBefore() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearWasSavedArriveToHomeTimeBefore");
        }else{
            sharedPreferences.edit().putString(WAS_SAVED_ARRIVE_TO_HOME_TIME_BEFORE, "").commit();
        }
    }
    //------------------------------------------------------------------------------------------



    //--------------------------------------TIMES----------------------------------------------------
    //------- leaveHomeTime ---------------
    public void setLeaveHomeTime(LocalDateTime leaveHomeTime) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.leaveHomeTime = leaveHomeTime;
        }else{
            sharedPreferences.edit().putString(LEAVE_HOME_TIME, leaveHomeTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
        }
    }

    public LocalDateTime getLeaveHomeTime() {
        LocalDateTime leaveHomeTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            leaveHomeTime = this.leaveHomeTime;
        }else{
            String leaveHomeTimeString = sharedPreferences.getString(LEAVE_HOME_TIME, "");
            if(leaveHomeTimeString.equals("")){
                leaveHomeTime = null;
            }else{
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
                leaveHomeTime = LocalDateTime.parse(leaveHomeTimeString, format);
                this.leaveHomeTime = leaveHomeTime;
            }

        }
        return leaveHomeTime;
    }

    public void clearLeaveHomeTime() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.d(TAG, "clearLeaveHomeTime");
        }else{
            sharedPreferences.edit().putString(LEAVE_HOME_TIME, "").commit();
        }
    }

    //------- startWorkTime ---------------
    public void setStartWorkTime(LocalDateTime startWorkTime) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.startWorkTime = startWorkTime;
        }else{
            sharedPreferences.edit().putString(START_WORK_TIME, startWorkTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
        }
    }

    public LocalDateTime getStartWorkTime() {
        LocalDateTime startWorkTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            startWorkTime = this.startWorkTime;
        }else{
            String startWorkTimeString = sharedPreferences.getString(START_WORK_TIME, "");
            if(startWorkTimeString.equals("")){
                startWorkTime = null;
            }else{
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
                startWorkTime = LocalDateTime.parse(startWorkTimeString, format);
                this.startWorkTime = startWorkTime;
            }
        }
        return startWorkTime;
    }

    public void clearStartWorkTime() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.i(TAG, "clearStartWorkTime");
        }else{
            sharedPreferences.edit().putString(START_WORK_TIME, "").commit();
        }
    }

    //------- leaveWorkTime ---------------
    public void setLeaveWorkTime(LocalDateTime leaveWorkTime) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.leaveWorkTime = leaveWorkTime;
        }else{
            sharedPreferences.edit().putString(LEAVE_WORK_TIME, leaveWorkTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
        }
    }

    public LocalDateTime getLeaveWorkTime() {
        LocalDateTime leaveWorkTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            leaveWorkTime = this.leaveWorkTime;
        }else{
            String leaveWorkTimeString = sharedPreferences.getString(LEAVE_WORK_TIME, "");
            if(leaveWorkTimeString.equals("")){
                leaveWorkTime = null;
            }else{
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
                leaveWorkTime = LocalDateTime.parse(leaveWorkTimeString, format);
                this.leaveWorkTime = leaveWorkTime;
            }
        }
        return leaveWorkTime;
    }

    public void clearLeaveWorkTime() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.i(TAG, "clearLeaveWorkTime");
        }else{
            sharedPreferences.edit().putString(LEAVE_WORK_TIME, "").commit();
        }
    }

    //------- arriveToHomeTime ---------------
    public void setArriveToHomeTime(LocalDateTime arriveToHomeTime) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            this.arriveToHomeTime = arriveToHomeTime;
        }else{
            sharedPreferences.edit().putString(ARRIVE_TO_HOME_TIME, arriveToHomeTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
        }
    }

    public LocalDateTime getArriveToHomeTime() {
        LocalDateTime arriveToHomeTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            arriveToHomeTime = this.arriveToHomeTime;
        }else{
            String arriveToHomeTimeString = sharedPreferences.getString(ARRIVE_TO_HOME_TIME, "");
            if(arriveToHomeTimeString.equals("")){
                arriveToHomeTime = null;
            }else{
                DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
                arriveToHomeTime = LocalDateTime.parse(arriveToHomeTimeString, format);
                this.arriveToHomeTime = arriveToHomeTime;
            }
        }
        return arriveToHomeTime;
    }

    public void clearArriveToHomeTime() {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            Log.i(TAG, "clearArriveToHomeTime");
        }else{
            sharedPreferences.edit().putString(ARRIVE_TO_HOME_TIME, "").commit();
        }
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}
