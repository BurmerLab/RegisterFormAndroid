package com.mytway.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;
import com.mytway.properties.SharedPreferencesNames;

public class Session {

    public String userName;
    public String password;
    public String email;
    public Boolean isUserLogged;
    public int typeWork;
    public String lengthTimeWork;
    public String startStandardTimeWork;
    public String workWeek;
    public String homeLatitude;
    public String homeLongitude;
    public String workLatitude;
    public String workLongitude;

    public Position homePlace;
    public Position workPlace;

    public String wayDistance;
    public String wayDuration;
    public String fullTimeTravelHomeToWork;

    private SharedPreferences sharedPreferences;

    public Session() {
    }

    public Session createMockedSession(String userName){
        Session session = null;
        if(userName.equals("standard")){
            session = sessionStandardUserMock();

        }else if (userName.equals("flexible")) {
            session = sessionFlexibleUserMock();
        }

        return session;
    }

    public Session sessionStandardUserMock() {
        Session session = new Session();
        session.userName = "mike";
        session.password = "password";
        session.email = "mike@mytway.com";
        session.isUserLogged = Boolean.TRUE;
        session.typeWork = 2;
        session.lengthTimeWork = "8:0";
        session.startStandardTimeWork = "8:0";
        session.workWeek = "1111100";
        session.workLatitude = "50.047441199625069";
        session.workLongitude = "19.962572269141674";
        session.homeLatitude = "50.056408699999999";
        session.homeLongitude = "20.8951514";
        return session;
    }

    public Session sessionFlexibleUserMock() {
        Session session = new Session();
        session.userName = "mike";
        session.password = "password";
        session.email = "mike@mytway.com";
        session.isUserLogged = Boolean.TRUE;
        session.typeWork = 1;
        session.lengthTimeWork = "8:0";
        session.workWeek = "1111100";
        session.workLatitude = "50.047441199625069";
        session.workLongitude = "19.962572269141674";
        session.homeLatitude = "50.056408699999999";
        session.homeLongitude = "20.8951514";
        return session;
    }

    public Session(Context context) {
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            //do nothing
        }else{
            sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, 1);
            getUserName();
            getPassword();
            getEmail();
            isUserLogged();
            getTypeWork();
            getLengthTimeWork();
            getStartStandardTimeWork();
            getWorkWeek();
            getHomeLatitude();
            getHomeLongitude();
            getWorkLatitude();
            getWorkLongitude();
            getWayDistance();
            getWayDuration();
        }
    }

    //------- UserName ---------------
    public void setUserName(String userName) {
        sharedPreferences.edit().putString("userName", userName).commit();
    }

    public String getUserName() {
        String userName;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            userName = "mike";
        }else{
            userName = sharedPreferences.getString("userName","");
            this.userName = userName;
        }
        return userName;
    }

    //------- Password ---------------
    public void setPassword(String password) {
        sharedPreferences.edit().putString("password", password).commit();
    }

    public String getPassword() {
        String password;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            password = "password";
        }else{
            password = sharedPreferences.getString("password","");
            this.password = password;
        }
        return password;
    }

    //------- Email ---------------
    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).commit();
    }

    public String getEmail() {
        String email;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            email = "mike@mytway.com";
        }else{
            email = sharedPreferences.getString("email","");
            this.email = email;
        }
        return email;
    }

    //------- isUserLogged ---------------
    public void setIsUserLogged(Boolean isUserLogged) {
        sharedPreferences.edit().putBoolean("isUserLogged", isUserLogged).commit();
    }

    public Boolean isUserLogged() {
        Boolean isUserLogged;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            isUserLogged = true;
        }else{
            isUserLogged = sharedPreferences.getBoolean("isUserLogged", false);
            this.isUserLogged = isUserLogged;
        }
        return isUserLogged;
    }

    //------- TypeWork ---------------
    public void setTypeWork(int typeWork) {
        sharedPreferences.edit().putInt("typeWork", typeWork).commit();
    }

    public int getTypeWork() {
        int typeWork;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            typeWork = this.typeWork;
        }else{
            typeWork = sharedPreferences.getInt("typeWork", 0);
            this.typeWork = typeWork;
        }

        return typeWork;
    }

    //------- lengthTimeWork ---------------
    public void setLengthTimeWork(String lengthTimeWork) {
        sharedPreferences.edit().putString("lengthTimeWork", lengthTimeWork).commit();
    }

    public String getLengthTimeWork() {
        String lengthTimeWork;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            lengthTimeWork = "8:0";
        }else{
            lengthTimeWork = sharedPreferences.getString("lengthTimeWork", "");
            this.lengthTimeWork = lengthTimeWork;
        }
        return lengthTimeWork;
    }

    //------- startStandardTimeWork ---------------
    public void setStartStandardTimeWork(String startStandardTimeWork) {
        sharedPreferences.edit().putString("startStandardTimeWork", startStandardTimeWork).commit();
    }

    public String getStartStandardTimeWork() {
        String startStandardTimeWork;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            startStandardTimeWork = "8:0";
        }else{
            startStandardTimeWork = sharedPreferences.getString("startStandardTimeWork", "");
            this.startStandardTimeWork = startStandardTimeWork;
        }

        return startStandardTimeWork;
    }

    //------- workWeek ---------------
    public void setWorkWeek(String workWeek) {
        sharedPreferences.edit().putString("workWeek", workWeek).commit();
    }

    public String getWorkWeek() {
        String workWeek;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            workWeek = "1111100";
        }else{
            workWeek = sharedPreferences.getString("workWeek", "");
            this.workWeek = workWeek;
        }
        return workWeek;
    }

    //------- homeLatitude ---------------
    public void setHomeLatitude(String homeLatitude) {
        sharedPreferences.edit().putString("homeLatitude", homeLatitude).commit();
    }

    public String getHomeLatitude() {
        String homeLatitude;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            homeLatitude = "50.056408699999999";
        }else{
            homeLatitude = sharedPreferences.getString("homeLatitude", "0.0");
            this.homeLatitude = homeLatitude;
        }

        return homeLatitude;
    }

    //------- homeLongitude ---------------
    public void setHomeLongitude(String homeLongitude) {
        sharedPreferences.edit().putString("homeLongitude", homeLongitude).commit();
    }

    public String getHomeLongitude() {
        String homeLongitude;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            homeLongitude = "20.8951514";
        }else{
            homeLongitude = sharedPreferences.getString("homeLongitude", "0.0");
            this.homeLongitude = homeLongitude;
        }
        return homeLongitude;
    }

    //------- workLatitude ---------------
    public void setWorkLatitude(String workLatitude) {
        sharedPreferences.edit().putString("workLatitude", workLatitude).commit();
    }

    public String getWorkLatitude() {
        String workLatitude;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            workLatitude = "50.047441199625069";
        }else{
            workLatitude = sharedPreferences.getString("workLatitude", "0.0");
            this.workLatitude = workLatitude;
        }

        return workLatitude;
    }

    //------- workLongitude ---------------
    public void setWorkLongitude(String workLongitude) {
        sharedPreferences.edit().putString("workLongitude", workLongitude).commit();
    }

    public String getWorkLongitude() {
        String workLongitude;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            workLongitude = "19.962572269141674";
        }else{
            workLongitude = sharedPreferences.getString("workLongitude", "0.0");
            this.workLongitude = workLongitude;
        }

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

    //------- WayDistance ---------------
    public void setWayDistance(String wayDistance) {
        sharedPreferences.edit().putString("wayDistance", wayDistance.toString()).commit();
    }

    public String getWayDistance() {
        String wayDistance;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wayDistance = this.wayDistance;
        }else{
            wayDistance = sharedPreferences.getString("wayDistance", "0");
            this.wayDistance = wayDistance;
        }
        return wayDistance;
    }

    //------- WayDuration ---------------
    public void setWayDuration(String wayDuration) {
        sharedPreferences.edit().putString("wayDuration", wayDuration).commit();
    }

    public String getWayDuration() {
        String wayDuration;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            wayDuration = this.wayDuration;
        }else{
            wayDuration = sharedPreferences.getString("wayDuration", "0");
            this.wayDuration = wayDuration;
        }
        return wayDuration;
    }

    //------- fullTimeTravelHomeToWork ---------------
    public void setFullTimeTravelHomeToWork(Context context) {
        TravelTime travelTime = new TravelTime();

        Position homePosition = new Position(Double.parseDouble(homeLatitude), Double.parseDouble(homeLongitude));
        Position workPosition = new Position(Double.parseDouble(workLatitude), Double.parseDouble(workLongitude));

        GoogleMapsDirectionJson googleMapsDirectionJson = travelTime.getTravelTimeBetweenTwoPositions(context, homePosition, workPosition);
        String fullTimeTravelHomeToWork = "" + googleMapsDirectionJson.getLegs().getDuration().getValue();

        sharedPreferences.edit().putString("fullTimeTravelHomeToWork", fullTimeTravelHomeToWork).commit();
    }

    public String getFullTimeTravelHomeToWork() {
        String fullTimeTravelHomeToWork;
        if(PropertiesValues.MOCK_APP_TO_TESTS){
            fullTimeTravelHomeToWork = this.fullTimeTravelHomeToWork;
        }else{
            fullTimeTravelHomeToWork = sharedPreferences.getString("fullTimeTravelHomeToWork", "0");
            this.fullTimeTravelHomeToWork = fullTimeTravelHomeToWork;
        }
        return fullTimeTravelHomeToWork;
    }



}

/*How to use:
*
* private Session createMockedSession;//global variable
createMockedSession = new Session(cntx); //in oncreate
//and now we set sharedpreference then use this like

createMockedSession.setUserName("USERNAME");
more: http://stackoverflow.com/questions/20678669/how-to-maintain-session-in-android
*/