package com.mytway.utility.webservice;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.activity.loginactivity.LoginActivity;
import com.mytway.activity.registerformactivity.RegistrationActivity;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.utility.EthernetConnectivity;

import java.util.concurrent.ExecutionException;

public class WebServiceUtility {

    private static final String TAG = "WebServiceUtility";

    public static boolean isUserPasswordCorrectInExternalDatabase(Context context, TextView textViewUserName, TextView textViewPassword) {
        Boolean isExternalPasswordCorrect = false;
        Boolean isLocalPasswordCorrect = false;
        Boolean isPasswordsCorrect = false;
        UserTable userTable = new UserTable();
        UserRepo userRepository = new UserRepo(context);

        String userName = textViewUserName.getText().toString();
        String userPassword = textViewPassword.getText().toString();

        if(EthernetConnectivity.isEthernetOnline(context)){
            MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase webServiceCheckIsPasswordIsCorrect = new MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase();
            MytwayWebserviceGetUserFromExternalDatabase webServiceGetUser = new MytwayWebserviceGetUserFromExternalDatabase();
            try {
                userTable = webServiceGetUser.execute(userName, userPassword).get();
                userTable.password = userPassword;
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with getting user parameters from external databases ", e);
                e.printStackTrace();
            }

            try {
                isExternalPasswordCorrect = webServiceCheckIsPasswordIsCorrect.execute(userName, userPassword).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with obtaining isPasswordCorrectInExternalDatabase ", e);
                e.printStackTrace();
            }

            if(!isExternalPasswordCorrect){
                Log.i(TAG, "External Password is not correct");
                return false;
            }

            if(userRepository.isUserExistInLocalDatabase(userName)){
                isLocalPasswordCorrect = userRepository.isUserNameAndPasswordIsCorrect(userName, userPassword);

                if(isExternalPasswordCorrect && isLocalPasswordCorrect){
                    //user exist in local database and in external database
                    isPasswordsCorrect = true;
                }else{
                    //override local user
                    userRepository.update(userTable);
                    isPasswordsCorrect = true;
                }

            }else{
                Log.i(TAG,"External and Local Password is not equals, I will insert external user parameters to local user database");


                int insertResult = userRepository.insert(userTable);
                Log.i(TAG, "Added user to local DataBase because not existed, datas I got from external Database");
                Toast.makeText(context, "Dodalem uzytkownika do lokalnej DB bo nie istnial, dane pobralem z external DB", Toast.LENGTH_SHORT).show();

                if(insertResult > 0) {
                    Log.i(TAG , "After adding to local database new user, insert result return value grater than zero");
                    isPasswordsCorrect = true;
                }else{
                    Log.i(TAG, "After adding to local database new user, insert result return value lesser than zero- it seems to be problem");
                }
            }
        }

        return isPasswordsCorrect;
    }

    public static boolean isUserNameExistInExternalDatabase(Context context, String userName) {
        Boolean isUserNameExistInExternalDatabase = null;

        if(EthernetConnectivity.isEthernetOnline(context)){
            MytwayWebserviceCheckUserNameIsExist webService = new MytwayWebserviceCheckUserNameIsExist();
            try {
                isUserNameExistInExternalDatabase = webService.execute(userName).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with obtaining isUserNameExistInExternalDatabase ", e);
                e.printStackTrace();
            }
        }else{
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            //Nima neta, dodac do kolejki do wyslania jak tylko net zostanie wlaczony
        }
        Toast.makeText(context, "Result: " + isUserNameExistInExternalDatabase, Toast.LENGTH_SHORT).show();
        return isUserNameExistInExternalDatabase;
    }

    //todo: pozmieniac na zwracanie int, wszrdzie tam gdzie jest uzwyana ta metoda
    public static int insertUserAccountToExternalDatabase(Context context, UserTable userTable, String jsonUserData) {

        Integer userIdAddedInExternalDB = null;
        if(EthernetConnectivity.isEthernetOnline(context)){
            Toast.makeText(context, "Insert to External DB", Toast.LENGTH_SHORT).show();
            MytwayWebserviceInsertUser mytwayWebserviceInsertUser = new MytwayWebserviceInsertUser();

            try {
                userIdAddedInExternalDB = (Integer) mytwayWebserviceInsertUser.execute(userTable.userName, jsonUserData).get();
            } catch (Exception e) {
                Log.i(TAG, "Problem with inserting User to External Database");
            }
        }
        return userIdAddedInExternalDB;
    }

    public static boolean updateUserAccountInExternalDatabase(Context context, UserTable userTable, String jsonUserData) {
        if(EthernetConnectivity.isEthernetOnline(context)){
            Toast.makeText(context, "Update to External DB", Toast.LENGTH_SHORT).show();
            new MytwayWebserviceUpdateUser().execute(userTable.userName, jsonUserData);
            return true;
        }else{
            Log.i(TAG, "Internet is not connected, problem with updating user account in external database. ");
        }
        return false;
    }
}
