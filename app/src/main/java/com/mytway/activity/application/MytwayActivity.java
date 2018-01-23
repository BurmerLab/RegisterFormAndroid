package com.mytway.activity.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mytway.activity.HandShakeActivity;
import com.mytway.activity.R;
import com.mytway.activity.registerformactivity.RegistrationActivity;
import com.mytway.database.UserRepo;

import com.mytway.properties.PropertiesValues;
import com.mytway.properties.SharedPreferencesNames;
import com.mytway.utility.Session;

public class MytwayActivity extends Activity {

    private Button mSignOutButton;
    private Button mRefreshButton;
    private Button mDeleteDbButton;
    private Button mDropTableDbButton;
    private Session session;
    private ListView listView;
    private ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytway);

        session = new Session(getApplicationContext());

        mSignOutButton = (Button) findViewById(R.id.signOutButton);
        mRefreshButton = (Button) findViewById(R.id.refreshButton);
        mDeleteDbButton = (Button) findViewById(R.id.deleteDbButton);
        mDropTableDbButton = (Button) findViewById(R.id.dropTableDbButton);

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View view) {
                    session.setIsUserLogged(false);

                    Intent intent = new Intent(MytwayActivity.this, HandShakeActivity.class);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
              }
        });

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View view) {

                    Intent intent = new Intent(MytwayActivity.this, MytwayActivity.class);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
              }
        });

        mDeleteDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View view) {
                UserRepo userRepo = new UserRepo(MytwayActivity.this);
                userRepo.deleteAllFromUser();
              }
        });

        mDropTableDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View view) {
                UserRepo userRepo = new UserRepo(MytwayActivity.this);
                userRepo.dropTableUser("User");
              }
        });




        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, MODE_PRIVATE);
        Boolean isUserLogged = sharedPreferences.getBoolean("isUserLogged", false);
        String userName = sharedPreferences.getString("userName", "");

//        Toast.makeText(MytwayActivity.this, "Work: " + workLat, Toast.LENGTH_SHORT).show();

        TextView userTextView = (TextView) findViewById(R.id.textView7);
        userTextView.setText("user: " + session.getUserName());

        TextView isLoggedTextView = (TextView) findViewById(R.id.textView8);
        isLoggedTextView.setText("isLogged: " + session.isUserLogged());

        TextView typeWorkTextView = (TextView) findViewById(R.id.textView9);
        typeWorkTextView.setText("TypeWork" + session.getTypeWork());

        TextView lengthTimeWorkTextView = (TextView) findViewById(R.id.textView10);
        lengthTimeWorkTextView.setText("length time work: " + session.getLengthTimeWork());

        TextView startStandardTimeWorkTextView = (TextView) findViewById(R.id.textView11);
        startStandardTimeWorkTextView.setText("start standard time work " + session.getStartStandardTimeWork());

        TextView WorkWeekTextView = (TextView) findViewById(R.id.textView12);
        WorkWeekTextView.setText("Work week: " + session.getWorkWeek());
//Session
        TextView homeLatitudeTextView = (TextView) findViewById(R.id.textView13);
        homeLatitudeTextView.setText("homeLatitudeTextView: " + session.getHomeLatitude());

        TextView homeLongitudeTextView = (TextView) findViewById(R.id.textView14);
        homeLongitudeTextView.setText("homeLongitudeTextView: " + session.getHomeLongitude());

        TextView workLatitudeTextView = (TextView) findViewById(R.id.textView15);
        workLatitudeTextView.setText("workLatitudeTextView: " + session.getWorkLatitude());

        TextView workLongitudeTextView = (TextView) findViewById(R.id.textView16);
        workLongitudeTextView.setText("workLongitudeTextView: " + session.getWorkLongitude());

        TextView distanceToWork = (TextView) findViewById(R.id.textView17);
        distanceToWork.setText("distance to work: " + session.getWayDistance());

        TextView durationTimeToWork = (TextView) findViewById(R.id.textView18);
        durationTimeToWork.setText("duration to work: " + session.getWayDuration());

        Session sessionLast = new Session(MytwayActivity.this);


//        Shared
//        TextView homeLatitudeTextView = (TextView) findViewById(R.id.textView13);
//        homeLatitudeTextView.setText("homeLatitudeTextView: " + sharedPreferences.getFloat("homeLatitude", 2F));
//
//        TextView homeLongitudeTextView = (TextView) findViewById(R.id.textView14);
//        homeLongitudeTextView.setText("homeLongitudeTextView: " + sharedPreferences.getFloat("homeLongitude", 2F));
//
//        TextView workLatitudeTextView = (TextView) findViewById(R.id.textView15);
//        workLatitudeTextView.setText("workLatitudeTextView: " + sharedPreferences.getFloat("workLatitude", 2F));
//
//        TextView workLongitudeTextView = (TextView) findViewById(R.id.textView16);
//        workLongitudeTextView.setText("workLongitudeTextView: " + sharedPreferences.getFloat("workLongitude", 2F));

    }


//    ------------------THREE DOTS MENU--------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_three_dots, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch(itemId){
            case R.id.homeScreen:
                Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MytwayActivity.class);
                this.startActivity(intent);
                break;
            case R.id.updateUserScreen:
                Toast.makeText(this, "update user clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, RegistrationActivity.class);
                intent.putExtra(PropertiesValues.UPDATE_ACCOUNT_INTENT, PropertiesValues.UPDATE_ACCOUNT_INTENT);
                this.startActivity(intent);
                break;
//            case R.id.homeScreen:
//                Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

//    --------------------------------------------------------------------------
}
