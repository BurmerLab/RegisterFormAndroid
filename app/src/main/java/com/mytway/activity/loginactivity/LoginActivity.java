package com.mytway.activity.loginactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.activity.application.MytwayActivity;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.pojo.User;
import com.mytway.utility.Session;
import com.mytway.utility.webservice.WebServiceUtility;
import com.mytway.validation.Validation;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    private Session session;
    private SwipeRefreshLayout swipeRefreshLayout;

    private EditText mLoginUserName;
    private EditText mLoginPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form_login);

        session = new Session(getApplicationContext());

        //initialize
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(R.color.application_button_color, R.color.application_background_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                },1000);
            }
        });

        mLoginUserName = (EditText) findViewById(R.id.loginUserName);
        mLoginPassword = (EditText) findViewById(R.id.loginPassword);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(WebServiceUtility.isUserPasswordCorrectInExternalDatabase(getApplicationContext(), mLoginUserName, mLoginPassword)){

                    if (checkValidation()) {
                        mLoginUserName = (EditText) findViewById(R.id.loginUserName);
                        mLoginPassword = (EditText) findViewById(R.id.loginPassword);

                        User loginUser = new User();
                        loginUser.setUserName(mLoginUserName.getText().toString());
                        loginUser.setPassword(mLoginPassword.getText().toString());

                        UserRepo userRepo = new UserRepo(LoginActivity.this);
                        UserTable userTable = userRepo.getUserByUserName(loginUser.getUserName());

                        Toast.makeText(LoginActivity.this, "Uzytlkownik: " + userTable.workPlaceLongitude , Toast.LENGTH_SHORT).show();

                        if(userTable != null && userTable.userName !=null){
                            if(loginUser.getUserName().equals(userTable.userName) &&  loginUser.getPassword().equals(userTable.password))
                                Toast.makeText(LoginActivity.this, "Znaleziono uzytkownika: " + userTable.userName, Toast.LENGTH_SHORT).show();

                            session.setIsUserLogged(true);
                            session.setUserName(userTable.userName);
                            session.setPassword(userTable.password);
                            session.setEmail(userTable.email);
                            session.setTypeWork(userTable.typeWork);
                            session.setLengthTimeWork(userTable.lengthTimeWork);
                            session.setStartStandardTimeWork(userTable.startStandardTimeWork);
                            session.setWorkWeek(userTable.workWeek);
                            session.setHomeLatitude("" + userTable.homePlaceLatitude);
                            session.setHomeLongitude("" + userTable.homePlaceLongitude);
                            session.setWorkLatitude("" + userTable.workPlaceLatitude);
                            session.setWorkLongitude("" + userTable.workPlaceLongitude);
                            session.setWayDistance("" + userTable.wayDistance);
                            session.setWayDuration("" + userTable.wayDuration);
                            Session sessionTwo = new Session(LoginActivity.this);

                            Intent intent = new Intent(LoginActivity.this, MytwayActivity.class);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Nie znaleziono lub bledne haslo" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    mLoginPassword.setError(getResources().getString(R.string.password_is_not_correct));
                }

            }
        });
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()) {
                mLoginUserName.setEnabled(true);
                mLoginPassword.setEnabled(true);
                mLoginButton.setEnabled(true);
            }else{
                //If no internet connection, then Disable Login Form
                mLoginUserName.setEnabled(false);
                mLoginPassword.setEnabled(false);
                mLoginButton.setEnabled(false);
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean checkValidation() {
        boolean validationResult = true;
        if (!Validation.hasText(mLoginUserName, getString(R.string.field_required))) validationResult = false;
        if (!Validation.isValidPassword(mLoginPassword, getString(R.string.length_of_registration_password_is_not_enought))) validationResult = false;

        return validationResult;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
