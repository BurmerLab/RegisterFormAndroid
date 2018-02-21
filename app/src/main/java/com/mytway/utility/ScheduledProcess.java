package com.mytway.utility;


import android.content.Context;
import android.content.SharedPreferences;

import com.mytway.database.UserTable;
import com.mytway.geolocalization.MytwayGeolocalizationService;
import com.mytway.properties.SharedPreferencesNames;
import com.mytway.utility.webservice.WebServiceUtility;

public class ScheduledProcess {

    public static final String EMPTY = "EMPTY";
    public static final String INSERT_ACCOUNT_USER_TO_EXTERNAL_DB = "INSERT_ACCOUNT_USER_TO_EXTERNAL_DB";
    public static final String UPDATE_ACCOUNT_USER_IN_EXTERNAL_DB = "UPDATE_ACCOUNT_USER_IN_EXTERNAL_DB";

    private SharedPreferences sharedPreferences;

    public void runScheduledProcess(Context context, UserTable userTable, String jsonUserData){


        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if(!sharedPreferences.getString(INSERT_ACCOUNT_USER_TO_EXTERNAL_DB, EMPTY).equals(EMPTY)){
            //INSERT TO EXTERNAL DB
            MytwayGeolocalizationService.saveToFile("INSERT = true","scheduled.txt");
            if(WebServiceUtility.insertUserAccountToExternalDatabase(context, userTable, jsonUserData)){
                MytwayGeolocalizationService.saveToFile("INSERT ZROBIONY, usuwam z shared preferences","scheduled.txt");
                sharedPreferences.edit().putString(INSERT_ACCOUNT_USER_TO_EXTERNAL_DB, EMPTY).commit();
            }

        }else if(sharedPreferences.getString(UPDATE_ACCOUNT_USER_IN_EXTERNAL_DB, EMPTY).equals(EMPTY)){
            //UPDATE TO EXTERNAL DB
            MytwayGeolocalizationService.saveToFile("UPDATE sharedPrefs znaleziony = true","scheduled.txt");
            if(WebServiceUtility.updateUserAccountInExternalDatabase(context, userTable, jsonUserData)){
                MytwayGeolocalizationService.saveToFile("UPDATE ZROBIONY, usuwam z shared preferences","scheduled.txt");
                sharedPreferences.edit().putString(UPDATE_ACCOUNT_USER_IN_EXTERNAL_DB, EMPTY).commit();
            }
        }
    }

}
