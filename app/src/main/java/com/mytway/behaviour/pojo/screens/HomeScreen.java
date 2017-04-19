package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

import java.util.Calendar;

public class HomeScreen implements Screen{
    private static final String TAG = "HomeScreen";

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext, Position currentPosition) throws Exception {
        throw new Exception("Not implemented method prepareScreen in " + TAG);
    }

    @Override
    public void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session,
                              Context mContext, Position currentPosition, LocalDateTime startWorkTime)
            throws Exception {
        String time = Calendar.getInstance().getTime().toString();
        view.setTextViewText(R.id.title, "Home Screen " + time);
        view.setTextViewText(R.id.firstTimeTextView, "Home1");
        view.setTextViewText(R.id.secondTimeTextView, "Home2");
        view.setTextViewText(R.id.thirdTimeTextView, "Home3");
    }
}
