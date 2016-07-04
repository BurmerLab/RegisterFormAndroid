package com.mytway.activity.utils;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.utility.permission.PermissionUtil;

public class NoPermissionsActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_permissions);

//        Intent intent = getIntent();
//        String[] permissions = intent.getStringArrayExtra("MissingPermissions");
//        String permission = intent.getStringExtra("MissingPermissions");

        ActivityCompat.requestPermissions(NoPermissionsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE_LOCATION);

//        PermissionUtil.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION,
//                getApplicationContext(),
//                NoPermissionsActivity.this,
//                getApplicationContext().getString(R.string.localization_will_help_with_choose_your_work_place));//Localization will help with choose your work place

        this.finish();
    }

}
