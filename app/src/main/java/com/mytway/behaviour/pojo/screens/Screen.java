package com.mytway.behaviour.pojo.screens;

import android.content.Context;
import android.widget.RemoteViews;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

public interface Screen {

    void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext,
                       Position currentPosition) throws Exception;

    void prepareScreen(RemoteViews view, DirectionWay directionWay, Session session, Context mContext,
                       Position currentPosition, LocalDateTime startWorkTime) throws Exception;


}
