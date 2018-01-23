package com.mytway.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.mytway.activity.application.MytwayActivity;
import com.mytway.properties.SharedPreferencesNames;

import java.util.Calendar;

public class Utility {

    public static void moveToActivity(Context context, String destinationClassActivity) {
        Intent intent = null;
        try {
            intent = new Intent(context, Class.forName(destinationClassActivity));
        } catch (ClassNotFoundException e) {
            Log.i("Problem with move to activity: ", e +"");
            e.printStackTrace();
        }
        context.startActivity(intent);
    }

    public static boolean shouldDoRefreshInThisDay(Context context) {
        Calendar cal = Calendar.getInstance();
        int currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR);

        boolean DO_NOT_REFRESH = false;
        boolean REFRESH = true;

        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.APP_INFO, 0);
        int dayOfYear = sharedPreferences.getInt("dayOfYearRefreshScheduledProcess", 0);

        if(dayOfYear != currentDayOfYear){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("dayOfYearRefreshScheduledProcess", currentDayOfYear);
            editor.commit();

            return REFRESH;
        }
        return DO_NOT_REFRESH;
    }
}
