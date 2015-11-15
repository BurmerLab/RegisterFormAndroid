package com.example.michal.registerformactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseClassName;

@ParseClassName("HomePageActivity")
public class HomePageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userName = extras.getString("UserName");
            String userPassword = extras.getString("UserPassword");
            String userEmail = extras.getString("UserEmail");
            Integer userTypeWork = extras.getInt("UserTypeWork");
            if (userName != null && userPassword != null && userEmail != null && userTypeWork != null) {
                Toast.makeText(this, "TOAST2", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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
