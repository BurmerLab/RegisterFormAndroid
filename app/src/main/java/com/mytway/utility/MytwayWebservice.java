package com.mytway.utility;

import android.util.Log;

import com.mytway.database.UserTable;
import com.mytway.pojo.Distance;
import com.mytway.pojo.Duration;
import com.mytway.pojo.GoogleMapsDirectionJson;
import com.mytway.pojo.Legs;
import com.mytway.pojo.Position;
import com.mytway.properties.PropertiesValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class MytwayWebservice {
    private static final String TAG = "MytwayWebservice";
    private final static String MYTWAY_WEBSERVICE_ADDRESS = "http://michalburmz5.nazwa.pl/MytwayWebServiceApplication/rest/";
    private final static String GOOGLE_MAPS_DIRECTION_WEB_SERVICE = "http://maps.googleapis.com/maps/api/directions/json?";

    public void insertUserToMytwayWebservice(String jsonMessage){
        try {
            JSONObject jsonObject = new JSONObject(jsonMessage);
            System.out.println(jsonObject);

            try {
//                URL url = new URL("http://localhost:8084/MytwayWebServiceApplication/rest/database/insertUser");
                URL url = new URL(MYTWAY_WEBSERVICE_ADDRESS + "database/insertUser");

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(PropertiesValues.WEBSERVICE_CONNECTION_TIMEOUT);
                connection.setReadTimeout(PropertiesValues.WEBSERVICE_READ_TIMEOUT);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while (in.readLine() != null) {
                    System.out.println("");
                }
                Log.i(TAG, "Mytway REST Service Invoked Successfully..");
                in.close();
            } catch (Exception e) {
                Log.i(TAG, "Mytway Error while calling Crunchify REST Service", e);

                System.out.println(e);
            }

        } catch (Exception e) {
            Log.i(TAG, "Mytway Error: ", e);
            e.printStackTrace();
        }
    }

    public boolean checkIsUserNameExistInExternalDatabaseByMytwayWebservice(String userName) throws JSONException {
        String result = "";
        try {
            String userNameForJson = "{userName : " + userName + "}";
            JSONObject jsonObject = new JSONObject(userNameForJson);
            System.out.println(jsonObject);

            try {
                URL url = new URL(MYTWAY_WEBSERVICE_ADDRESS + "database/checkUserName");

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(PropertiesValues.WEBSERVICE_CONNECTION_TIMEOUT);
                connection.setReadTimeout(PropertiesValues.WEBSERVICE_READ_TIMEOUT);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                //Poprawny sposob odbierania Response od web Servicu
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;

                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                    result = output;
                }
                System.out.println("\nMytway REST Service Invoked Successfully..");
                br.close();
            } catch (Exception e) {
                System.out.println("\nError while calling Mytway REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonResult = new JSONObject(result);
        boolean isUserNameExistInExternalDatabase = jsonResult.getBoolean("isUserNameExistInTable");

        return isUserNameExistInExternalDatabase;
    }

    public boolean checkIsPasswordIsCorrectInExternalDatabaseByMytwayWebservice(String userName, String userPassword) throws JSONException {
        String result = "";
        try {
            String jsonRequest = "{userName : " + userName + ", password : " + userPassword + "}";
            JSONObject jsonObject = new JSONObject(jsonRequest);
            System.out.println(jsonObject);

            try {
                URL url = new URL(MYTWAY_WEBSERVICE_ADDRESS + "database/checkUserAndPassword");

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(PropertiesValues.WEBSERVICE_CONNECTION_TIMEOUT);
                connection.setReadTimeout(PropertiesValues.WEBSERVICE_READ_TIMEOUT);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;

                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                    result = output;
                }
                System.out.println("\n Mytway REST Service Invoked Successfully..");
                br.close();
            } catch (Exception e) {
                System.out.println("\n Error while calling Mytway REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonResult = new JSONObject(result);
        boolean isUserNameAndPasswordCorrect = jsonResult.getBoolean("isUserNameAndPasswordCorrect");

        return isUserNameAndPasswordCorrect;
    }

    public UserTable getUserFromExternalDatabaseByMytwayWebservice(String userName, String userPassword) throws JSONException {
        String result = "";
        UserTable userTable = new UserTable();
        try {
            String jsonRequest = "{userName : " + userName + ", password : " + userPassword + "}";
            JSONObject jsonObject = new JSONObject(jsonRequest);
            System.out.println(jsonObject);

            try {
                URL url = new URL(MYTWAY_WEBSERVICE_ADDRESS + "database/getUser");

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(PropertiesValues.WEBSERVICE_CONNECTION_TIMEOUT);
                connection.setReadTimeout(PropertiesValues.WEBSERVICE_READ_TIMEOUT);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonObject.toString());
                out.close();

                //Poprawny sposob odbierania Response od web Servicu
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;

                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                    result = output;
                }
                System.out.println("\n Mytway REST Service Invoked Successfully..");
                br.close();
            } catch (Exception e) {
                System.out.println("\n Error while calling Mytway REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonResult = new JSONObject(result);
        userTable.userName = jsonResult.getString("userName");
        userTable.email = jsonResult.getString("email");
        userTable.typeWork = jsonResult.getInt("typeWork");
        userTable.lengthTimeWork = jsonResult.getString("lengthTimeWork");
        if(jsonResult.length() >= 10){
            userTable.startStandardTimeWork = jsonResult.getString("startStandardTimeWork");
        }
        userTable.workPlaceLatitude = jsonResult.getDouble("workPlaceLatitude");
        userTable.workPlaceLongitude = jsonResult.getDouble("workPlaceLongitude");
        userTable.homePlaceLatitude = jsonResult.getDouble("homePlaceLatitude");
        userTable.homePlaceLongitude = jsonResult.getDouble("homePlaceLongitude");
        userTable.workWeek = jsonResult.getString("workWeek");

        return userTable;
    }

    //Webservice to get travel time between two points( i.e home - work)
    public GoogleMapsDirectionJson getTravelTimeBetweenPlaces(Position startPosition, Position endPosition) throws JSONException {
        String result = "";

        GoogleMapsDirectionJson googleMapsDirection = new GoogleMapsDirectionJson();

        try {
            String jsonRequest = GOOGLE_MAPS_DIRECTION_WEB_SERVICE
                    + "origin="
                    + startPosition.getLatitude() + "," + startPosition.getLongitude()
                    + "&destination="
                    + endPosition.getLatitude() + "," + endPosition.getLongitude()
                    + "&sensor=true&units=metric";
            System.out.println("Address: " + jsonRequest);
            Log.i(TAG, "Address TAG: " + jsonRequest);
            try {
                URL url = new URL(jsonRequest);

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(PropertiesValues.WEBSERVICE_CONNECTION_TIMEOUT);
                connection.setReadTimeout(PropertiesValues.WEBSERVICE_READ_TIMEOUT);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;

                while ((output = br.readLine()) != null) {
//                    System.out.println(output);
                    result = result + output;
                }
                System.out.println("\n Mytway REST Service Invoked Successfully..");
                br.close();
            } catch (Exception e) {
                System.out.println("\n Error while calling Mytway REST Service");
                System.out.println(e);
            }

        } catch (Exception e) {
            Log.i(TAG, "Problem with web service: ", e);
            e.printStackTrace();
        }

        JSONObject jsonResult = new JSONObject(result);

        JSONObject objRoute = jsonResult.getJSONArray(GoogleMapsDirectionJson.TAG_ROUTES).getJSONObject(0);
        JSONObject legsJson = objRoute.getJSONArray(GoogleMapsDirectionJson.TAG_LEGS).getJSONObject(0);
        JSONObject jsonDuration = legsJson.getJSONObject(GoogleMapsDirectionJson.TAG_DURATION);
        String durationText = jsonDuration.getString(GoogleMapsDirectionJson.TAG_DURATION_TEXT);
        int durationValue = jsonDuration.getInt(GoogleMapsDirectionJson.TAG_DURATION_VALUE);
        Duration duration = new Duration(durationText, durationValue);

        JSONObject jsonDistance = legsJson.getJSONObject(GoogleMapsDirectionJson.TAG_DISTANCE);
        String distanceText = jsonDistance.getString(GoogleMapsDirectionJson.TAG_DISTANCE_TEXT);
        double distanceValue = jsonDistance.getDouble(GoogleMapsDirectionJson.TAG_DISTANCE_VALUE);
        Distance distance = new Distance(distanceText, distanceValue);

        Legs legs = new Legs();
        legs.setDistance(distance);
        legs.setDuration(duration);

        googleMapsDirection.setLegs(legs);
//        googleMapsDirection.setLatitude(jsonResult.getDouble(GoogleMapsDirectionJson.TAG_LAT));
//        googleMapsDirection.setLongitude(jsonResult.getDouble(GoogleMapsDirectionJson.TAG_LNG));
//        googleMapsDirection.setRoutes(jsonResult.getString(GoogleMapsDirectionJson.TAG_ROUTES));
//        googleMapsDirection.setLegs(jsonResult.getString(GoogleMapsDirectionJson.TAG_LEGS));
//        googleMapsDirection.setSteps(jsonResult.getString(GoogleMapsDirectionJson.TAG_LEGS));
//        googleMapsDirection.setStartLocation(jsonResult.getString(GoogleMapsDirectionJson.TAG_START));
//        googleMapsDirection.setEndLocation(jsonResult.getString(GoogleMapsDirectionJson.TAG_END));

        return googleMapsDirection;
    }

}
