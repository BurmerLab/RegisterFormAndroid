package com.example.michal.registerformactivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;

import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.michal.pojo.User;
import com.example.michal.utility.FormObtainerUtility;


public class MainActivity extends Activity {

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mUserPassword;
    protected RadioButton mFlexibleTypeWorkRadioButton;
    protected RadioButton mStandardTypeWorkRadioButton;
    protected Button mRegisterButton;
    private Spinner spinner1, spinner2;
    private TextView mSetStartStandardWorkTimeRegisterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //initialize
        mUsername = (EditText) findViewById(R.id.userNameRegisteredEditText);
        mEmail = (EditText) findViewById(R.id.emailRegisteredEditText);
        mUserPassword = (EditText) findViewById(R.id.passwordRegisteredEditText);
        mRegisterButton = (Button) findViewById(R.id.registeredButton);
        mFlexibleTypeWorkRadioButton = (RadioButton) findViewById(R.id.flexibleTypeWorkRegisterRadioButton);
        mStandardTypeWorkRadioButton = (RadioButton) findViewById(R.id.standardTypeWorkRegisterRadioButton);
        mSetStartStandardWorkTimeRegisterTextView = (TextView) findViewById(R.id.startStandardWorkTimeRegisterTextView);
        final RadioGroup radioGroupStandard = (RadioGroup) findViewById(R.id.idRadio_group1_standard_type_work);
        final RadioGroup radioGroupFlexible = (RadioGroup) findViewById(R.id.idRadio_group2_flexible_type_work);

        //listen to register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                User user = new User();
                user.setUserName(mUsername.getText().toString());
                user.setPassword(mUserPassword.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setTypeWork(FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkRadioButton, mStandardTypeWorkRadioButton));

                Toast.makeText(MainActivity.this, "TOAST!, checbox status: " +
                        FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkRadioButton, mStandardTypeWorkRadioButton), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, WorkMapRegisterActivity.class);
                intent.putExtra("UserName", user.getUserName());
                intent.putExtra("UserPassword", user.getPassword());
                intent.putExtra("UserEmail", user.getEmail());
                intent.putExtra("UserTypeWork", user.getTypeWork());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }

        });

        mSetStartStandardWorkTimeRegisterTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mSetStartStandardWorkTimeRegisterTextView.setText(getString(R.string.your_start_work_time) + " " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        mStandardTypeWorkRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupFlexible.clearCheck();
                ((RadioButton) radioGroupFlexible.findViewById(R.id.flexibleTypeWorkRegisterRadioButton)).setChecked(false);
                mSetStartStandardWorkTimeRegisterTextView.setEnabled(true);
            }
        });

        mFlexibleTypeWorkRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupStandard.clearCheck();
                ((RadioButton) radioGroupStandard.findViewById(R.id.standardTypeWorkRegisterRadioButton)).setChecked(false);
            }
        });

    }



}