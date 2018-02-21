package com.mytway.database.userLocalizations;

public class UserLocalizationsTable {
    // Labels table name
    public static final String TABLE = "UserTimes";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String CREATION_DATE = "creation_date";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String TIME_STATUS = "time_status";

    public int localizationId;
    public String userName;
    public String creationDate;
    public String latitude;
    public String longitude;
    public String timeStatus;

    public String createJson(){
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"userName\" : ").append("\"").append(userName).append("\", ");
        json.append("\"email\" : ").append("\"").append(creationDate).append("\", ");
        json.append("\"password\" : ").append("\"").append(latitude).append("\", ");
        json.append("\"password\" : ").append("\"").append(longitude).append("\", ");
        json.append("\"password\" : ").append("\"").append(timeStatus).append("\", ");
        json.append("}");
        return json.toString();
    }

}
