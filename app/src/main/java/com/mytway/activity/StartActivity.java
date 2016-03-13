package com.mytway.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mytway.activity.application.MytwayActivity;
import com.mytway.database.UserRepo;
import com.mytway.properties.SharedPreferencesNames;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        Retrieve sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, MODE_PRIVATE);
//        Boolean isUserLogged = sharedPreferences.getBoolean("isUserLogged", false);
        Boolean isUserLogged = false;

        if(isUserLogged){
            // Call Mytway activity
            Intent intent = new Intent(getApplicationContext(), MytwayActivity.class);
            startActivity(intent);
        }
        else{
            // call Login or register Activity
            Intent intent = new Intent(getApplicationContext(), HandShakeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
