package com.mytway.utility.webservice;


import android.os.AsyncTask;
import android.util.Log;

import com.mytway.utility.MytwayWebservice;

import org.json.JSONException;

public class MytwayWebserviceInsertUser extends AsyncTask {

    private static final String TAG = "MytwayWebserviceInsertUser";

    @Override
    protected Integer doInBackground(Object... arg0) {
        MytwayWebservice mytwayWebservice = new MytwayWebservice();
        String userName = (String) arg0[0];
        String jsonMessage = (String) arg0[1];

        Integer userIdAddedInExternalDB = null;

        if(!mytwayWebservice.checkIsUserNameExistInExternalDatabaseByMytwayWebservice(userName)){
            if(!jsonMessage.equals("")){
                try {
                    userIdAddedInExternalDB = mytwayWebservice.insertUserToMytwayWebservice(jsonMessage);
                } catch (JSONException e) {
                    Log.i(TAG, "Problem with inserting user in external DB");
                }
            }else{
                Log.i(TAG, "JsonMessage is empty");
            }
        }
        return userIdAddedInExternalDB;
    }
}
