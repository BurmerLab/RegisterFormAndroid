package com.mytway.behaviour.pojo.screens;

import android.content.Context;

import com.mytway.behaviour.pojo.DirectionWay;
import com.mytway.pojo.Position;
import com.mytway.utility.Session;

import org.joda.time.LocalDateTime;

public interface Screen {

    void prepareScreen(DirectionWay directionWay, Session session, Context mContext,
                       Position currentPosition) throws Exception;

    void prepareScreen(DirectionWay directionWay, Session session, Context mContext,
                       Position currentPosition, LocalDateTime startWorkTime) throws Exception;


}
