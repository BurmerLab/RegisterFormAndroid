package com.example.michal.registerformactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

@ParseClassName("MainActivity")
public class MainActivity extends Activity {

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mUserPassword;
    protected Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize
        mUsername = (EditText) findViewById(R.id.userNameRegisteredEditText);
        mEmail = (EditText) findViewById(R.id.emailRegisteredEditText);
        mUserPassword = (EditText) findViewById(R.id.passwordRegisteredEditText);
        mRegisterButton = (Button) findViewById(R.id.registeredButton);

        //listen to register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    Parse.initialize(MainActivity.this, "Pap2Fu3JiEXiUNj3wW6ZYNot1A35mlnBa6Kg4bLq", "iVMk9gLHcFZCVW0TeLpxqpu5QhsQBVowsBUY20ZT");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ParseUser user = new ParseUser();
                user.setUsername(mUsername.getText().toString());
                user.setPassword(mUserPassword.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //user signed up succesfull
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //take user to homepage

//                            Intent takeUserHome = new Intent(MainActivity.this, HomePageActivity.class);
//                            startActivity(takeUserHome);
                        } else {
                            //there was an error signing up user
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }
}