package com.mytway.activity.application;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.pojo.User;

import java.util.ArrayList;

public class MytwayActivity extends Activity {
    final int APPWIDGET_HOST_ID = 2048;
    final int REQUEST_PICK_APPWIDGET = 0;

    LinearLayout myLayout;

    AppWidgetManager appWidgetManager;
    AppWidgetHost appWidgetHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytway);
        myLayout = (LinearLayout) findViewById(R.id.mainLayout);

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        startActivity(intent);

    }

}
