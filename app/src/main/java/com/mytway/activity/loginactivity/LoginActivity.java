package com.mytway.activity.loginactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.mytway.properties.SharedPreferencesNames;
import com.mytway.validation.Validation;

public class LoginActivity extends Activity {

    private EditText mLoginUserName;
    private EditText mLoginPassword;

    private Button mLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form_login);

        //initialize
        mLoginUserName = (EditText) findViewById(R.id.loginUserName);
        mLoginPassword = (EditText) findViewById(R.id.loginPassword);
        mLoginButton = (Button) findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    mLoginUserName = (EditText) findViewById(R.id.loginUserName);
                    mLoginPassword = (EditText) findViewById(R.id.loginPassword);

                    User loginUser = new User();
                    loginUser.setUserName(mLoginUserName.getText().toString());
                    loginUser.setPassword(mLoginPassword.getText().toString());

                    UserRepo userRepo = new UserRepo(LoginActivity.this);
                    UserTable userTable = userRepo.getUserByUserName(loginUser.getUserName());
                    if(userTable != null && userTable.userName !=null){
                        if(loginUser.getUserName().equals(userTable.userName) &&  loginUser.getPassword().equals(userTable.password))
                            Toast.makeText(LoginActivity.this, "Znaleziono uzytkownika: " + userTable.userName, Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, MODE_PRIVATE).edit();
                            editor.putBoolean("isUserLogged", true);
                            editor.putString("userName", userTable.userName);
                            editor.putInt("typeWork", userTable.typeWork);
                            editor.putString("lengthTimeWork", userTable.lengthTimeWork);
                            editor.putString("startStandardTimeWork", userTable.startStandardTimeWork);
                            editor.putString("workWeek", userTable.workWeek);
                            editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MytwayActivity.class);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }


                    }else{
                        Toast.makeText(LoginActivity.this, "Nie znaleziono lub bledne haslo" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkValidation() {
        boolean validationResult = true;
        if (!Validation.hasText(mLoginUserName, getString(R.string.field_required))) validationResult = false;
        if (!Validation.isValidPassword(mLoginPassword, getString(R.string.length_of_registration_password_is_not_enought))) validationResult = false;

        return validationResult;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
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
