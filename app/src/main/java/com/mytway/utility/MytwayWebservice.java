package com.mytway.utility;

import android.util.Log;
import android.widget.Toast;

import com.mytway.database.UserTable;

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
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

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
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

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
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

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
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

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
        userTable.startStandardTimeWork = jsonResult.getString("startStandardTimeWork");
        userTable.workPlaceLongitude = jsonResult.getDouble("workPlaceLatitude");
        userTable.workPlaceLatitude = jsonResult.getDouble("workPlaceLongitude");
        userTable.homePlaceLatitude = jsonResult.getDouble("homePlaceLatitude");
        userTable.homePlaceLongitude = jsonResult.getDouble("homePlaceLongitude");
        userTable.workWeek = jsonResult.getString("workWeek");

        return userTable;
    }

}
