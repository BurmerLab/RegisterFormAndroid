package com.mytway.behaviour;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;
import com.mytway.geolocalization.MytwayGeolocalizationService;
import com.mytway.utility.permission.PermissionUtil;

public class RideProcessor extends FragmentActivity {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    public String CURRENT_TIME = "07:20";
    private UserBehaviour userBehaviour;


    public String rideProcess(Context context){
        if(CURRENT_TIME.equals("07:20")){
            return "07:20";
        }else{
            return "09:00";
        }
    }
    String result = "";

    public String geolocalizationText(Context context, Activity activity){
        if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
                && PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {
            result = setUpMap(context);
        }else{
            PermissionUtil.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    PERMISSION_REQUEST_CODE_LOCATION,
                    context,
                    activity,
                    "Localization will help with choose your work place");
        }

//
        return result;
    }

    private String setUpMap(Context contex) {

        MytwayGeolocalizationService geolocalization = new MytwayGeolocalizationService(contex);
        Double latitudeLocalization = geolocalization.getLatitude();
        Double longitudeLocalization = geolocalization.getLongitude();

        if (ActivityCompat.checkSelfPermission(contex, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(contex, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "LIPA";
        }

        final LatLng latLng = new LatLng(latitudeLocalization, longitudeLocalization);

        return latLng.toString();
    }



//    public String rideProcess(UserBehaviour userBehaviour){
//        User user = userBehaviour.getUser();
//
//        if(CURRENT_TIME.equals("07:20")){
//            return "07:20";
//        }else{
//            return "09:00";
//        }
//    }

    public UserBehaviour getUserBehaviour() {
        return userBehaviour;
    }

    public void setUserBehaviour(UserBehaviour userBehaviour) {
        this.userBehaviour = userBehaviour;
    }
}
