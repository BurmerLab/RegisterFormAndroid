package com.mytway.widget;

import android.Manifest;

import com.mytway.utility.permission.PermissionUtil;

import junit.framework.TestCase;

public class MyWidgetProviderTest extends TestCase {

    public void testObtainMissingPermissionsShort() throws Exception {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String[] missingPermissions = new String[permissions.length];

        String[] expectedPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        boolean noPermission = false;

        for(int i = 0; i < permissions.length; i++ ){

            if(true){
                missingPermissions[i] = permissions[i];
            }
        }


    }
}