package com.mytway.utility.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PermissionUtil {

    public static void requestPermission(String strPermission, int perCode,Context context ,Activity activity, String requestMessage){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, strPermission)){
            Toast.makeText(context.getApplicationContext(), requestMessage, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(activity,new String[]{strPermission},perCode);
        }
    }

    public static boolean checkPermission(String strPermission ,Context context){
        int result = ContextCompat.checkSelfPermission(context, strPermission);

        return result == PackageManager.PERMISSION_GRANTED;
    }

}
