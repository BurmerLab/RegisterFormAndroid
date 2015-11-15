package com.example.michal.registerformactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michal.pojo.User;
import com.example.michal.utility.FormObtainerUtility;

public class MainActivity extends Activity {

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mUserPassword;
    protected CheckBox mFlexibleTypeWorkCheckbox;
    protected CheckBox mStandardTypeWorkCheckbox;
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
        mFlexibleTypeWorkCheckbox = (CheckBox) findViewById(R.id.flexibleTypeWorkRegisteredCheckbox);
        mStandardTypeWorkCheckbox = (CheckBox) findViewById(R.id.standardTypeWorkRegisteredCheckbox);

        makeDisableCheckboxes();

        //listen to register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                User user = new User();
                user.setUserName(mUsername.getText().toString());
                user.setPassword(mUserPassword.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setTypeWork(FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkCheckbox, mStandardTypeWorkCheckbox));

                //wyswietla sie toast
                Toast.makeText(MainActivity.this, "TOAST!, checbox status: " + FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkCheckbox, mStandardTypeWorkCheckbox), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                intent.putExtra("UserName", user.getUserName());
                intent.putExtra("UserPassword", user.getPassword());
                intent.putExtra("UserEmail", user.getEmail());
                intent.putExtra("UserTypeWork", user.getTypeWork());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }

        });
    }

    private void makeDisableCheckboxes() {
        mFlexibleTypeWorkCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    mStandardTypeWorkCheckbox.setEnabled(false);
                }else{
                    mStandardTypeWorkCheckbox.setEnabled(true);
                }
            }
        });

        mStandardTypeWorkCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    mFlexibleTypeWorkCheckbox.setEnabled(false);
                }else{
                    mFlexibleTypeWorkCheckbox.setEnabled(true);
                }
            }
        });
    }


}