package com.mytway.utility.webservice;


import android.os.AsyncTask;
import android.util.Log;

import com.mytway.database.UserTable;
import com.mytway.utility.MytwayWebservice;

import org.json.JSONException;

public class MytwayWebserviceGetUserFromExternalDatabase extends AsyncTask<String, Void, UserTable> {

    private static final String TAG = "MytwayWebserviceGetUserFromExternalDatabase";

    @Override
    protected UserTable doInBackground(String... arg0) {
        UserTable userTable = new UserTable();
        String userName = arg0[0];
        String userPassword = arg0[1];

        MytwayWebservice mytwayWebservice = new MytwayWebservice();
        if(userName != null){
            try {
                userTable  = mytwayWebservice.getUserFromExternalDatabaseByMytwayWebservice(userName, userPassword);
            } catch (JSONException e) {
                Log.i(TAG, "Problem with getting user paramters from external database", e);
                e.printStackTrace();
            }
        }else{
            Log.i(TAG, "UserName is empty, mytway can't check duplicate in external database");
        }
        return userTable;
    }
}
