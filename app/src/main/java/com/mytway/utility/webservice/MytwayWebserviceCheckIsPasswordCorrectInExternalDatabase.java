package com.mytway.utility.webservice;


import android.os.AsyncTask;
import android.util.Log;

import com.mytway.utility.MytwayWebservice;

import org.json.JSONException;

public class MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase";

    @Override
    protected Boolean doInBackground(String... arg0) {
        Boolean webServiceResult = new Boolean(false);
        String userName = arg0[0];
        String userPassword = arg0[1];

        MytwayWebservice mytwayWebservice = new MytwayWebservice();
        if(userName != null){
            try {
                webServiceResult  = mytwayWebservice.checkIsPasswordIsCorrectInExternalDatabaseByMytwayWebservice(userName, userPassword);
            } catch (JSONException e) {
                Log.i(TAG, "Problem with checking user name in external database", e);
                e.printStackTrace();
            }
        }else{
            Log.i(TAG, "UserName is empty, mytway can't check duplicate in external database");
        }
        return webServiceResult;
    }
}
