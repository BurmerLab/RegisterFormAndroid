package com.example.michal.activity.registerformactivity;

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

import java.util.Calendar;

import android.view.View.OnClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.michal.activity.R;
import com.example.michal.pojo.User;
import com.example.michal.utility.FormObtainerUtility;

public class RegistrationActivity extends Activity {

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mUserPassword;
    protected RadioButton mFlexibleTypeWorkRadioButton;
    protected RadioButton mStandardTypeWorkRadioButton;
    protected Button mRegisterButton;
    protected Button mWorkTimeLengthButton;
    private Button mSetStartStandardWorkTimeRegisterButton;
    private RadioGroup mRadioGroupStandard;
    private RadioGroup mRadioGroupFlexible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form_registration);

        //initialize
        mUsername = (EditText) findViewById(R.id.userNameRegisteredEditText);
        mEmail = (EditText) findViewById(R.id.emailRegisteredEditText);
        mUserPassword = (EditText) findViewById(R.id.passwordRegisteredEditText);
        mRegisterButton = (Button) findViewById(R.id.registeredButton);
        mWorkTimeLengthButton = (Button) findViewById(R.id.length_work_hours_register_button);
        mFlexibleTypeWorkRadioButton = (RadioButton) findViewById(R.id.flexibleTypeWorkRegisterRadioButton);
        mStandardTypeWorkRadioButton = (RadioButton) findViewById(R.id.standardTypeWorkRegisterRadioButton);
        mSetStartStandardWorkTimeRegisterButton = (Button) findViewById(R.id.startStandardWorkTimeRegisterButton);
        mRadioGroupStandard = (RadioGroup) findViewById(R.id.idRadio_group1_standard_type_work);
        mRadioGroupFlexible = (RadioGroup) findViewById(R.id.idRadio_group2_flexible_type_work);

        //listen to register button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                User user = new User();
                user.setUserName(mUsername.getText().toString());
                user.setPassword(mUserPassword.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setTypeWork(FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkRadioButton, mStandardTypeWorkRadioButton));
                user.setLengthTimeWork(mWorkTimeLengthButton.getText().toString());
                user.setStartTimeWork(mSetStartStandardWorkTimeRegisterButton.getText().toString());

                Toast.makeText(RegistrationActivity.this, "TOAST!, checbox status: " +
                        FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkRadioButton, mStandardTypeWorkRadioButton), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RegistrationActivity.this, WorkMapRegisterActivity.class);
                intent.putExtra("user", user);
//                intent.putExtra("UserName", user.getUserName());
//                intent.putExtra("UserPassword", user.getPassword());
//                intent.putExtra("UserEmail", user.getEmail());
//                intent.putExtra("UserTypeWork", user.getTypeWork());
//                intent.putExtra("UserLengthTimeWork", user.getLengthTimeWork());
                if(user.getStartStandardTimeWork() != null){
                    intent.putExtra("UserStartStandardTimeWork", user.getStartStandardTimeWork());
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        //http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
//        Once you have your objects implement Parcelable it's just a matter of putting them into your Intents with putExtra():
//
//        Intent i = new Intent();
//        i.putExtra("name_of_extra", myParcelableObject);
//
//        Then you can pull them back out with getParcelableExtra():
//
//        Intent i = getIntent();
//        MyParcelable myParcelableObject = (MyParcelable) i.getParcelableExtra("name_of_extra");
//
//        If your Object Class implements Parcelable and Serializable then make sure you do cast to one of the following:
//
//        i.putExtra((Parcelable) myParcelableObject);
//        i.putExtra((Serializable) myParcelableObject);

        mWorkTimeLengthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mWorkLengthTimePicker;
                mWorkLengthTimePicker = new TimePickerDialog(RegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mWorkTimeLengthButton.setText(getString(R.string.work_length) + " " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mWorkLengthTimePicker.setTitle(getString(R.string.length_work_hours_register_text));
                mWorkLengthTimePicker.show();
            }
        });


        mSetStartStandardWorkTimeRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mStartWorkTimePicker;
                mStartWorkTimePicker = new TimePickerDialog(RegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mSetStartStandardWorkTimeRegisterButton.setText(getString(R.string.your_start_work_time) + " " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mStartWorkTimePicker.setTitle("Select Time");
                mStartWorkTimePicker.show();
            }
        });

        mStandardTypeWorkRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroupFlexible.clearCheck();
                ((RadioButton) mRadioGroupFlexible.findViewById(R.id.flexibleTypeWorkRegisterRadioButton)).setChecked(false);
                mSetStartStandardWorkTimeRegisterButton.setVisibility(View.VISIBLE);
                mSetStartStandardWorkTimeRegisterButton.setEnabled(true);
            }
        });

        mFlexibleTypeWorkRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroupStandard.clearCheck();
                ((RadioButton) mRadioGroupStandard.findViewById(R.id.standardTypeWorkRegisterRadioButton)).setChecked(false);
                mSetStartStandardWorkTimeRegisterButton.setVisibility(View.GONE);
            }
        });

    }



}