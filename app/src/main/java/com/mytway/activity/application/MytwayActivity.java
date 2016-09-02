package com.mytway.activity.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mytway.activity.HandShakeActivity;
import com.mytway.activity.R;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.properties.SharedPreferencesNames;
import com.mytway.utility.Session;

public class MytwayActivity extends Activity {

    private Button mSignOutButton;
    private Button mRefreshButton;
    private Button mDeleteDbButton;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytway);
        session = new Session(getApplicationContext());

        mSignOutButton = (Button) findViewById(R.id.signOutButton);
        mRefreshButton = (Button) findViewById(R.id.refreshButton);
        mDeleteDbButton = (Button) findViewById(R.id.deleteDbButton);

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
                UserTable userTable = new UserTable();
                userRepo.deleteAllFromUser();
              }
        });




        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, MODE_PRIVATE);
        Boolean isUserLogged = sharedPreferences.getBoolean("isUserLogged", false);
        String userName = sharedPreferences.getString("userName", "");

//        double workLat = (double) sharedPreferences.getFloat("workLatitude", 2);
//        double workLon = (double) sharedPreferences.getFloat("workLongitude", 2);

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
}
