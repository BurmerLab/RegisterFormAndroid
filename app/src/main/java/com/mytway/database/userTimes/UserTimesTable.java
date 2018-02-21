package com.mytway.database.userTimes;

public class UserTimesTable {
    // Labels table name
    public static final String TABLE = "UserTimes";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String CREATION_DATE = "creation_date";
    public static final String TIME_STATUS = "time_status";
    public static final String TIME = "time";

    public int userTimesId;
    public String userName;
    public String creationDate;
    public String timeStatus;
    public String time;

    public String createJson(){
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"userName\" : ").append("\"").append(userName).append("\", ");
        json.append("\"email\" : ").append("\"").append(creationDate).append("\", ");
        json.append("\"password\" : ").append("\"").append(timeStatus).append("\", ");
        json.append("\"typeWork\" : ").append("\"").append(time).append("\", ");
        json.append("}");
        return json.toString();
    }

}
