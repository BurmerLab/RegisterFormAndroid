package com.mytway.utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.pojo.Distance;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.properties.PropertiesValues;
import com.mytway.properties.SharedPreferencesNames;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class TravelTimeGogDirectionRequest {

    //https://stackoverflow.com/questions/8615543/running-task-periodicalyonce-a-day-once-a-week

    private boolean shouldDoItInThisWeek(Context context) {
        Calendar cal = Calendar.getInstance();
//        int currentWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        boolean DO_NOT_REFRESH = false;
        boolean REFRESH = true;

        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesNames.APP_INFO, 0);
//        int weekOfYear = sharedPreferences.getInt("weekOfYearRefrTravelTime", 0);
        int dayOfMonth = sharedPreferences.getInt("dayOfMonthRefrTravelTime", 0);

        if(dayOfMonth != currentDayOfMonth){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("dayOfMonthRefrTravelTime", currentDayOfMonth);
            editor.commit();

            saveToFileRefresh("REFRESH = TRUE");

            return REFRESH;
        }
        saveToFileRefresh("REFRESH = FALSE");
        return DO_NOT_REFRESH;
    }

    public void refreshTravelTimeBasedOnGogDirections(Context context, Session session){

        if(shouldDoItInThisWeek(context)){

            TravelTime travelTime = new TravelTime();
            GoogleMapsDirectionJson gMapsDirection =
                    travelTime.getTravelTimeBetweenTwoPositions(context, session.getHomePlace(), session.getWorkPlace());
            int travelToWorkDuration;
            Double travelToWorkDistance;

            if(gMapsDirection != null){
                travelToWorkDuration = gMapsDirection.getLegs().getDuration().getValue();
                travelToWorkDistance = Distance.designateDistanceBetween(session.getHomePlace(), session.getWorkPlace());
                saveToFileRefresh("DURATION: " + travelToWorkDuration);
                saveToFileRefresh("DISTANCE: " + travelToWorkDistance);
            }else{
                travelToWorkDuration = 0;
                travelToWorkDistance = 0d;
            }

            UserRepo userRepo = new UserRepo(context);
            UserTable userTable = userRepo.getUserByUserName(session.getUserName());

            saveToFileRefresh("Founded user: " + userTable.userName);
            userTable.wayDistance = travelToWorkDistance;
            userTable.wayDuration = travelToWorkDuration;

            saveToFileRefresh("distance: " + userTable.wayDistance);
            saveToFileRefresh("duration: " + userTable.wayDuration);

            session.setWayDistance(travelToWorkDistance.toString());
            session.setWayDuration("" + travelToWorkDuration);
            userRepo.update(userTable);
            saveToFileRefresh("UPDATED!!!\n");
        }else{
            saveToFileRefresh("NOT UPDATED");
        }
    }

    public static void saveToFileRefresh(String content) {
        if(PropertiesValues.SAVE_TO_FILE_ENABLE){
            try{
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

                if(!dir.exists()){
                    dir.mkdirs();
                }

                File file = new File(dir, "REFRESH_GOG.txt");
                LocalDateTime currentLocalDateTime = new LocalDateTime();
                //currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa")

                FileOutputStream fop = new FileOutputStream(file, true);
                String pointXml = "\n" + currentLocalDateTime.toString("dd-MM-yyyy hh:mm:ss aa") + ": " + content;


                fop.write(pointXml.getBytes());
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
