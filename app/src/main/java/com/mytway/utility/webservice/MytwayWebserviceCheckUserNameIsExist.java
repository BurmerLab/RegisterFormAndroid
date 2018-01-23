package com.mytway.utility.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.mytway.utility.MytwayWebservice;

public class MytwayWebserviceCheckUserNameIsExist extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "MytwayWebserviceCheckUserNameIsExist";

    @Override
    protected Boolean doInBackground(String... arg0) {
        Boolean webServiceResult = null;
        String userName = arg0[0];
        MytwayWebservice mytwayWebservice = new MytwayWebservice();
        if(userName != null){
            webServiceResult  = mytwayWebservice.checkIsUserNameExistInExternalDatabaseByMytwayWebservice(userName);
        }else{
            Log.i(TAG, "UserName is empty, mytway can't check duplicate in external database");
        }
        return webServiceResult;
    }
}
