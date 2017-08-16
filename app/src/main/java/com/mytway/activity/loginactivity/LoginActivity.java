package com.mytway.activity.loginactivity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.activity.application.MytwayActivity;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.pojo.User;
import com.mytway.utility.EthernetConnectivity;
import com.mytway.utility.MytwayWebservice;
import com.mytway.utility.Session;
import com.mytway.validation.Validation;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    private Session session;

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
        mLoginUserName = (EditText) findViewById(R.id.loginUserName);
        mLoginPassword = (EditText) findViewById(R.id.loginPassword);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isUserPasswordCorrectInExternalDatabase(mLoginUserName, mLoginPassword)){

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

    public boolean isUserPasswordCorrectInExternalDatabase(TextView textViewUserName, TextView textViewPassword) {
        Boolean isExternalPasswordCorrect = false;
        Boolean isLocalPasswordCorrect = false;
        Boolean isPasswordsCorrect = false;
        UserTable userTable = new UserTable();
        UserRepo userRepository = new UserRepo(LoginActivity.this);

        String userName = textViewUserName.getText().toString();
        String userPassword = textViewPassword.getText().toString();

        if(EthernetConnectivity.isEthernetOnline(LoginActivity.this)){
            MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase webServiceCheckIsPasswordIsCorrect = new MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase();
            MytwayWebserviceGetUserFromExternalDatabase webServiceGetUser = new MytwayWebserviceGetUserFromExternalDatabase();
            try {
                userTable = webServiceGetUser.execute(userName, userPassword).get();
                userTable.password = userPassword;
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with getting user paramters from external databases ", e);
                e.printStackTrace();
            }

            try {
                isExternalPasswordCorrect = webServiceCheckIsPasswordIsCorrect.execute(userName, userPassword).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "Problem with obtaining isPasswordCorrectInExternalDatabase ", e);
                e.printStackTrace();
            }

            if(!isExternalPasswordCorrect){
                Log.i(TAG, "External Password is not correct");
                return false;
            }

            if(userRepository.isUserExistInLocalDatabase(userName)){
                isLocalPasswordCorrect = userRepository.isUserNameAndPasswordIsCorrect(userName, userPassword);

                if(isExternalPasswordCorrect && isLocalPasswordCorrect){
                    //user exist in local database and in external database
                    isPasswordsCorrect = true;
                }else{
                    //override local user
                    userRepository.update(userTable);
                    isPasswordsCorrect = true;
                }

            }else{
                Log.i(TAG,"External and Local Password is not equals, I will insert external user parameters to local user database");


                int insertResult = userRepository.insert(userTable);
                Log.i(TAG, "Added user to local DataBase because not existed, datas I got from external Database");
                Toast.makeText(LoginActivity.this, "Dodalem uzytkownika do lokalnej DB bo nie istnial, dane pobralem z external DB", Toast.LENGTH_SHORT).show();

                if(insertResult > 0) {
                    Log.i(TAG , "After adding to local database new user, insert result return value grater than zero");
                    isPasswordsCorrect = true;
                }else{
                    Log.i(TAG, "After adding to local database new user, insert result return value lesser than zero- it seems to be problem");
                }
            }
        }

        return isPasswordsCorrect;
    }

    private class MytwayWebserviceCheckIsPasswordCorrectInExternalDatabase extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean webServiceResult = null;
            String userName = arg0[0];
            String userPassword = arg0[1];

            MytwayWebservice mytwayWebservice = new MytwayWebservice();
            if(userName != null){
                try {
                    webServiceResult  = mytwayWebservice.checkIsPasswordIsCorrectInExternalDatabaseByMytwayWebservice(userName, userPassword);
                } catch (JSONException e) {
                    Log.i(TAG, "Problem with checking user name in external database", e);
                    e.printStackTrace();
                }
            }else{
                Log.i(TAG, "UserName is empty, mytway can't check duplicate in external database");
            }
            return webServiceResult;
        }
    }

    private class MytwayWebserviceGetUserFromExternalDatabase extends AsyncTask<String, Void, UserTable> {

        @Override
        protected UserTable doInBackground(String... arg0) {
            UserTable userTable = new UserTable();
            String userName = arg0[0];
            String userPassword = arg0[1];

            MytwayWebservice mytwayWebservice = new MytwayWebservice();
            if(userName != null){
                try {
                    userTable  = mytwayWebservice.getUserFromExternalDatabaseByMytwayWebservice(userName, userPassword);
                } catch (JSONException e) {
                    Log.i(TAG, "Problem with getting user paramters from external database", e);
                    e.printStackTrace();
                }
            }else{
                Log.i(TAG, "UserName is empty, mytway can't check duplicate in external database");
            }
            return userTable;
        }
    }

}
