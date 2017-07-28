package com.mytway.properties;

public class PropertiesValues {

    public static final boolean MOCK_APP_TO_TESTS = false;


    public final static int HOURS_WORK_LENGTH_TIME_REGISTRATION = 8;
    public final static int MINUTS_WORK_LENGTH_TIME_REGISTRATION = 0;
    public final static int HOURS_START_WORK_TIME_REGISTRATION = 8;
    public final static int MINUTES_START_WORK_TIME_REGISTRATION = 0;
    public static final int WEBSERVICE_READ_TIMEOUT = 20000; //20 sec
    public static final int WEBSERVICE_CONNECTION_TIMEOUT = 20000; //20 sec.

    public static final int FULL_HOURS_DAY = 24; //20 sec.

    public static int PASSWORD_LENGTH_REQUIRED = 6;

    public static int WORK_AND_HOME_PLACE_MAP_ZOOM_LEVEL = 16;

    public static final int INTERVAL_TO_REPEAT_SERVICE_METHOD_IN_SECONDS = 1000 * 60;//1000 * 30 = 30sek

    public static final String HOUR_CHARACTER = "h";

    public static final String MINUTES_CHARACTER = "m";


    public static String ANY_LETTERS_REGEXP = "([a-z])|([A-Z])";

    public static float SIZE_OF_LITTLE_LETTERS_IN_LEFT_TIME = 0.4f;
}
