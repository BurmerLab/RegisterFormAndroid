package com.mytway.activity.registerformactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mytway.activity.HandShakeActivity;
import com.mytway.activity.R;
import com.mytway.activity.application.MytwayActivity;
import com.mytway.utility.SaveSharedPreference;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        int nr = prefs.getInt("nr", 0);

        Toast.makeText(StartActivity.this, "text: " + restoredText + " nr: " + nr, Toast.LENGTH_SHORT).show();
        if (restoredText != null){
            int selectionStart = prefs.getInt("selection-start", -1);
            int selectionEnd = prefs.getInt("selection-end", -1);
        }

            Intent intent = new Intent(getApplicationContext(), HandShakeActivity.class);
            startActivity(intent);

//        if(SaveSharedPreference.getUserName(StartActivity.this).length() == 0){
//            // call Login Activity
//            Intent intent = new Intent(getApplicationContext(), HandShakeActivity.class);
//            startActivity(intent);
//        }
//        else
//        {
//            // Call Next Activity
//            Intent intent = new Intent(getApplicationContext(), MytwayActivity.class);
//            startActivity(intent);
//        }
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
