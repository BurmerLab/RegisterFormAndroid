package com.mytway.properties;

public class PropertiesValues {

    public static final int METERS_IN_KILOMETER = 1000;


    //Registration
    //registrationFunction - REGISTER_NEW_USER or UPDATE_USER
    public static final String PROCESSING_ACCOUNT = "processingAccount";
    public static final String REGISTER_NEW_USER = "REGISTER_NEW_USER";
    public static final String UPDATE_USER = "UPDATE_USER";
    //------------------------

    public static boolean MOCK_APP_TO_TESTS = false;
    //safe zone around home or work
    public static final double SAFE_LENGTH_AROUND_HOME_AND_WORK_IN_METERS = 250.0;

    public static boolean SAVE_TO_FILE_ENABLE = true;

    public final static int HOURS_WORK_LENGTH_TIME_REGISTRATION = 8;
    public final static int MINUTS_WORK_LENGTH_TIME_REGISTRATION = 0;
    public final static int HOURS_START_WORK_TIME_REGISTRATION = 8;
    public final static int MINUTES_START_WORK_TIME_REGISTRATION = 0;
    public static final int WEBSERVICE_READ_TIMEOUT = 20000; //20 sec
    public static final int WEBSERVICE_CONNECTION_TIMEOUT = 20000; //20 sec.
    public static final double THREE_HOUNDRED_METERS = 300d;

    public static final int FULL_HOURS_DAY = 24; //20 sec.

    public static int PASSWORD_LENGTH_REQUIRED = 6;

    public static int WORK_AND_HOME_PLACE_MAP_ZOOM_LEVEL = 16;

    //widget sampling update
    public static int INTERVAL_TO_REPEAT_UPDATE_WIDGET = 1000 * 60;//1000 * 30 = 30sek
    public static int OFTEN_INTERVAL_REPEATS_TO_UPDATE_WIDGET = 1000 * 30;//30 sek
    public static int RARELY_INTERVAL_REPEATS_TO_UPDATE_WIDGET = 1000 * 3600;// 1 hour
    public static String INTERVAL_TYPE = "DEFAULT";

    public static final int HOUR_BEFORE_DEPARTURE_TO_START_REPEATING_WIDGET = 1;//ONE_HOUR

    public static final String HOUR_CHARACTER = "h";

    public static final String MINUTES_CHARACTER = "m";


    public static String ANY_LETTERS_REGEXP = "([a-z])|([A-Z])";

    public static float SIZE_OF_LITTLE_LETTERS_IN_LEFT_TIME = 0.4f;

    public static final int SECONDS_IN_FOUR_HOURS = 14400;
    public static final int MINUTES_IN_HOUR = 60;
    public static final int SECONDS_IN_HOUR = 60;

    public static  final int PERCENTAGE_DISTANCE_AROUND_PLACE = 2;

    //redirectingFrom screens
    public final static String UPDATE_ACCOUNT_INTENT = "UPDATE_ACCOUNT_INTENT";
    public static final String UPDATE_ACCOUNT_BUTTON_TEXT = "UPDATE ACCOUNT";


}
