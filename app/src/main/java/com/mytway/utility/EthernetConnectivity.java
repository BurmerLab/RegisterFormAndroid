package com.mytway.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class EthernetConnectivity {

    public static boolean isEthernetOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
//Using:
//        if(!EthernetConnectivity.isEthernetOnline(LoginActivity.this)) {
//            //If no internet, then Disable Login Form
//            mLoginUserName.setEnabled(false);
//            mLoginPassword.setEnabled(false);
//            mLoginButton.setEnabled(false);
//            mLoginButton.setError("NO INTERNET");
//            Toast.makeText(LoginActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
//        }

}
