package com.mytway.widget.utils;


import com.mytway.behaviour.pojo.AProcessingTime;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public class FlexibleRepeatIntervalProcessor {

    private static final int ONE_HOUR = 1;

    public static void calculateSamplingTimeOfWidgetRepeat(Session session){
        Calendar currentTime = Calendar.getInstance();
        LocalDateTime lenghtWorkTime = AProcessingTime.prepareTimeFromStringToCalendar(session.getLengthTimeWork());

        LocalDateTime startWorkTime =  AProcessingTime.prepareTimeFromStringToCalendar(session.getStartStandardTimeWork());
        LocalDateTime endWorkTime =  AProcessingTime.addTimeTo(startWorkTime, lenghtWorkTime.getHourOfDay(), lenghtWorkTime.getMinuteOfHour(), lenghtWorkTime.getSecondOfMinute());
        // |- Start/end work time +1H or -1H
        // C- current time
        // S- start work time,
        // E- end work time
        //-----------|---C----S---C---|------------------------------------|---C-----E-----C----|---------
        if(currentTime.after(startWorkTime.minusHours(ONE_HOUR)) && currentTime.before(startWorkTime.plusHours(ONE_HOUR))
                || (currentTime.after(endWorkTime.minusHours(ONE_HOUR)) && currentTime.before(endWorkTime.plusHours(ONE_HOUR)))) {
            //set to small sampling - 30s
            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.OFTEN_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
        }else{
            //set to normal sampling - 5m
            PropertiesValues.INTERVAL_TO_REPEAT_UPDATE_WIDGET = PropertiesValues.RARELY_INTERVAL_REPEATS_TO_UPDATE_WIDGET;
        }
    }

}
