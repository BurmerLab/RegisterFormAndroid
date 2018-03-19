package com.mytway.widget.utils;

import android.os.Environment;

import com.mytway.behaviour.pojo.AProcessingTime;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.Session;
import com.mytway.utility.TravelTime;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class StandardRepeatIntervalProcessor {
    private static final int ONE_HOUR = 1;

    public static void calculateSamplingTimeOfWidgetRepeatForStandardUser(Session session, TravelTime travelTime, DirectionWay directionWay){
        LocalDateTime lenghtWorkTime = AProcessingTime.prepareTimeFromStringToCalendar(session.getLengthTimeWork());

        LocalDateTime currentTime = new LocalDateTime()
                .withYear(1970)
                .withMonthOfYear(1)
                .withDayOfMonth(1)
                .withHourOfDay(LocalDateTime.now().getHourOfDay())
                .withMinuteOfHour(LocalDateTime.now().getMinuteOfHour());

        LocalDateTime startWorkTime =  AProcessingTime.prepareTimeFromStringToCalendar(session.getStartStandardTimeWork());
        //startWorkTime - travelTime = timeLeaveHome
        LocalDateTime timeLeaveHome = AProcessingTime.subtractTimeTo(startWorkTime,
                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

        //endWorkTime + travelTime = timeArriveHome
        LocalDateTime endWorkTime =  AProcessingTime.addTimeTo(startWorkTime, lenghtWorkTime.getHourOfDay(),
                lenghtWorkTime.getMinuteOfHour(),
                lenghtWorkTime.getSecondOfMinute());

        LocalDateTime timeArriveHome = AProcessingTime.addTimeTo(endWorkTime,
                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getHour(),
                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getMinutes(),
                travelTime.getGoogleMapsDirectionJson().getLegs().getDuration().getSeconds());

        saveToFileIntervals("-----------------------------------");
        saveToFileIntervals("startWorkTime: " + startWorkTime);
        saveToFileIntervals("timeLeaveHome: " + timeLeaveHome);
        saveToFileIntervals("endWorkTime: " + endWorkTime);
        saveToFileIntervals("timeArriveHome: " + timeArriveHome);
        saveToFileIntervals("-----------------------------------");

        // |- timeLeaveHome/timeArriveHome time +1H or -1H
        // C- current time
        // S- start work time,
        // E- end work time
        //-----------|---C----S---C---|------------------------------------|---C-----E-----C----|---------

        if((directionWay.getDirectionsStatus().isInWayToWork() || directionWay.getDirectionsStatus().isInWayToHome()) ||
                (currentTime.isAfter(timeLeaveHome.minusHours(ONE_HOUR)) && currentTime.isBefore(startWorkTime.plusHours(ONE_HOUR)))
                || (currentTime.isAfter(timeArriveHome.minusHours(ONE_HOUR)) && currentTime.isBefore(timeArriveHome.plusHours(ONE_HOUR)))
                ) {
            //set to small sampling - 30s
            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.OFTEN_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
            PropertiesValues.INTERVAL_TYPE = "OFTEN";
            saveToFileIntervals("-----------------------------------");
            saveToFileIntervals("startWorkTime: " + startWorkTime);
            saveToFileIntervals("timeLeaveHome: " + timeLeaveHome);
            saveToFileIntervals("endWorkTime: " + endWorkTime);
            saveToFileIntervals("timeArriveHome: " + timeArriveHome);
            saveToFileIntervals("OFTEN");
            saveToFileIntervals("-----------------------------------");
        }else{
            //set to normal sampling - 5m
            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.RARELY_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
            PropertiesValues.INTERVAL_TYPE = "RARELY";
            saveToFileIntervals("-----------------------------------");
            saveToFileIntervals("startWorkTime: " + startWorkTime);
            saveToFileIntervals("timeLeaveHome: " + timeLeaveHome);
            saveToFileIntervals("endWorkTime: " + endWorkTime);
            saveToFileIntervals("timeArriveHome: " + timeArriveHome);
            saveToFileIntervals("RARELY");
            saveToFileIntervals("-----------------------------------");
        }
    }

    public static void saveToFileIntervals(String content) {
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");

            if(!dir.exists()){
                dir.mkdirs();
            }

            File file = new File(dir, "INTERVALS.txt");
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
