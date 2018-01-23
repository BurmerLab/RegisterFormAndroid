package com.mytway.utility.webservice;


import android.os.AsyncTask;
import android.util.Log;

import com.mytway.utility.MytwayWebservice;

public class MytwayWebserviceUpdateUser extends AsyncTask {

    private static final String TAG = "MytwayWebserviceUpdateUser";

    @Override
    protected Object doInBackground(Object... arg0) {
        MytwayWebservice mytwayWebservice = new MytwayWebservice();
        String userName = (String) arg0[0];
        String jsonMessage = (String) arg0[1];

        if(mytwayWebservice.checkIsUserNameExistInExternalDatabaseByMytwayWebservice(userName)){
            if(!jsonMessage.equals("")){
                mytwayWebservice.updateUserToMytwayWebservice(jsonMessage);
            }else{
                Log.i(TAG, "JsonMessage is empty");
            }
        }
        return true;
    }
}
