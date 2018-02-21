package com.mytway.behaviour.pojo;


import android.content.Context;
import android.content.SharedPreferences;

import com.mytway.properties.PropertiesValues;
import com.mytway.properties.SharedPreferencesNames;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class UserDailyTimes {

    public static final String LOCAL_DATE_TIME_TO_STRING_FORMAT = "yyyy-MM-dd HH:mm";

    private static final String LEAVE_HOME_TIME = "leaveHomeTime";
    private static final String START_WORK_TIME = "startWorkTime";
    private static final String LEAVE_WORK_TIME = "leaveWorkTime";
    private static final String ARRIVE_TO_HOME_TIME = "arriveToHomeTime";

    private LocalDateTime currentTime = new LocalDateTime();
    private LocalDateTime leaveHomeTime;
    private LocalDateTime startWorkTime;
    private LocalDateTime leaveWorkTime;
    private LocalDateTime arriveToHomeTime;

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
        }
    }


    //------- leaveHomeTime ---------------
    public void setLeaveHomeTime(LocalDateTime leaveHomeTime) {
        sharedPreferences.edit().putString(LEAVE_HOME_TIME, leaveHomeTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
    }

    public LocalDateTime getLeaveHomeTime() {
        LocalDateTime leaveHomeTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            leaveHomeTime = new LocalDateTime(2017,11,28,7,35);
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
        sharedPreferences.edit().putString(LEAVE_HOME_TIME, "").commit();
    }

    //------- startWorkTime ---------------
    public void setStartWorkTime(LocalDateTime startWorkTime) {
        sharedPreferences.edit().putString(START_WORK_TIME, startWorkTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
    }

    public LocalDateTime getStartWorkTime() {
        LocalDateTime startWorkTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            startWorkTime = new LocalDateTime(2017,11,28,7,35);
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
        sharedPreferences.edit().putString(START_WORK_TIME, "").commit();
    }

    //------- leaveWorkTime ---------------
    public void setLeaveWorkTime(LocalDateTime leaveWorkTime) {
        sharedPreferences.edit().putString(LEAVE_WORK_TIME, leaveWorkTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
    }

    public LocalDateTime getLeaveWorkTime() {
        LocalDateTime leaveWorkTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            leaveWorkTime = new LocalDateTime(2017,11,28,7,35);
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
        sharedPreferences.edit().putString(LEAVE_WORK_TIME, "").commit();
    }

    //------- arriveToHomeTime ---------------
    public void setArriveToHomeTime(LocalDateTime arriveToHomeTime) {
        sharedPreferences.edit().putString(ARRIVE_TO_HOME_TIME, arriveToHomeTime.toString(LOCAL_DATE_TIME_TO_STRING_FORMAT)).commit();
    }

    public LocalDateTime getArriveToHomeTime() {
        LocalDateTime arriveToHomeTime;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            arriveToHomeTime = new LocalDateTime(2017,11,28,7,35);
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
        sharedPreferences.edit().putString(ARRIVE_TO_HOME_TIME, "").commit();
    }

}
