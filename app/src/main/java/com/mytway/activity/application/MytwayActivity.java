package com.mytway.activity.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.pojo.User;

public class MytwayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mytway);
        Intent intent = getIntent();
        final User user = intent.getParcelableExtra("user");

        Toast.makeText(MytwayActivity.this,
                "User: " + user.getUserName() +
                " email: " + user.getEmail() +
                " pass: " + user.getPassword() +
                " type: " + user.getTypeWork().getStatusCode() +
                " start: " + user.getStartStandardTimeWork() +
                " WorkLat: " + user.getWorkPlace().getLatitude() +
                " HomeLat: " + user.getHomePlace().getLatitude() +
                " Days: " + user.getWorkWeek().getFriday()

                , Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mytway, menu);
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
