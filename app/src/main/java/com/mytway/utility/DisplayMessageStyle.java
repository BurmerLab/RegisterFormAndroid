package com.mytway.utility;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.properties.PropertiesValues;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayMessageStyle {


    public static void displayLeftTime(RemoteViews view, int idElement, String messageTime) {
        Pattern pattern = Pattern.compile(PropertiesValues.ANY_LETTERS_REGEXP);
        Matcher matcher = pattern.matcher(messageTime);
        SpannableString time =  new SpannableString(messageTime);

        // Check all occurrences
        while (matcher.find()) {
//            System.out.print("Start index: " + matcher.start());
//            System.out.print(" End index: " + matcher.end());
//            System.out.println(" Found: " + matcher.group());

            time.setSpan(new RelativeSizeSpan(PropertiesValues.SIZE_OF_LITTLE_LETTERS_IN_LEFT_TIME), matcher.start(), matcher.end(), 0); // set size
            time.setSpan(new ForegroundColorSpan(Color.WHITE), matcher.start(), matcher.end(), 0);// set color
        }
        view.setTextViewText(idElement, time);
    }
}
