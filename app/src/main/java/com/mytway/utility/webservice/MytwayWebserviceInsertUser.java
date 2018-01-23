package com.mytway.utility.webservice;


import android.os.AsyncTask;
import android.util.Log;

import com.mytway.utility.MytwayWebservice;

public class MytwayWebserviceInsertUser extends AsyncTask {

    private static final String TAG = "MytwayWebserviceInsertUser";

    @Override
    protected Object doInBackground(Object... arg0) {
        MytwayWebservice mytwayWebservice = new MytwayWebservice();
        String userName = (String) arg0[0];
        String jsonMessage = (String) arg0[1];

        if(!mytwayWebservice.checkIsUserNameExistInExternalDatabaseByMytwayWebservice(userName)){
            if(!jsonMessage.equals("")){
                mytwayWebservice.insertUserToMytwayWebservice(jsonMessage);
            }else{
                Log.i(TAG, "JsonMessage is empty");
            }
        }
        return true;
    }
}
