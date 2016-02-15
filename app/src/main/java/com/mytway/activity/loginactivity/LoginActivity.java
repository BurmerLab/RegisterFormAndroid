package com.mytway.activity.loginactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.database.UserRepo;
import com.mytway.database.UserTable;
import com.mytway.pojo.User;

//autologing: http://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state
//musi miec dostep do internetu tylko podczas pierwszego zalogowania potem bedzie autologging
//
public class LoginActivity extends Activity {

    private EditText mLoginUserName;
    private EditText mLoginPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        mLoginButton = (Button) findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLoginUserName = (EditText) findViewById(R.id.usernameLogin);
                mLoginPassword = (EditText) findViewById(R.id.loginPassword);

                User user = new User();
                user.setUserName(mLoginUserName.getText().toString());
                user.setPassword(mLoginPassword.getText().toString());

                UserRepo userRepo = new UserRepo(LoginActivity.this);
                UserTable userTable = userRepo.getUserByUserName(user.getUserName());
                if(userTable != null){
                    Toast.makeText(LoginActivity.this, "Znaleziono uzytkownika: " + userTable.userName, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Nie znaleziono" , Toast.LENGTH_SHORT).show();
                }
            }
        });
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
