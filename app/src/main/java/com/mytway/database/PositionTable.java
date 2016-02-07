package com.mytway.database;

/**
 * Created by SG0220830 on 1/16/2016.
 */
public class PositionTable {

    // Labels table name
    public static final String TABLE = "Position";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    // property help us to keep data
    public int userId;
    public double latitude;
    public double longitude;
}
