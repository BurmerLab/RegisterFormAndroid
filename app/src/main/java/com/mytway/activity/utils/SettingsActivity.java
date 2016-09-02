package com.mytway.activity.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.mytway.activity.R;
import com.mytway.properties.SharedPreferencesNames;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, MODE_PRIVATE);
        Boolean isUserLogged = sharedPreferences.getBoolean("isUserLogged", false);

        double workLat = (double) sharedPreferences.getFloat("workLatitude", 0);
        double workLon = (double) sharedPreferences.getFloat("workLongitude", 0);


    }
}
