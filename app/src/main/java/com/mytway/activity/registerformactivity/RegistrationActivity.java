package com.mytway.activity.registerformactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.view.View.OnClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.pojo.User;
import com.mytway.pojo.WorkWeek;
import com.mytway.pojo.registration.CheckboxModel;
import com.mytway.pojo.registration.CustomAdapter;
import com.mytway.properties.Properties;
import com.mytway.validation.Validation;

public class RegistrationActivity extends Activity {

    private EditText mUserName;
    private EditText mEmail;
    private EditText mUserPassword;
    private RadioButton mFlexibleTypeWorkRadioButton;
    private RadioButton mStandardTypeWorkRadioButton;
    private Button mRegisterButton;
    private Button mWorkTimeLengthButton;
    private Button mStartStandardWorkTimeRegisterButton;
    private Button mWorkDaysInWeek;
    private RadioGroup mRadioGroupStandard;
    private RadioGroup mRadioGroupFlexible;

    ListView mWorkDaysInWeeklistView;
    public CheckboxModel[] checkboxModelItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form_registration);

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("text", "dupa");
        editor.putInt("nr", 2);
        editor.apply();
//        editor.commit();

//        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//        String restoredText = prefs.getString("text", null);
//        if (restoredText != null){
//            int selectionStart = prefs.getInt("selection-start", -1);
//            int selectionEnd = prefs.getInt("selection-end", -1);
//        }

        //initialize
        mUserName = (EditText) findViewById(R.id.userNameRegisteredEditText);
        mEmail = (EditText) findViewById(R.id.emailRegisteredEditText);
        mUserPassword = (EditText) findViewById(R.id.passwordRegisteredEditText);
        mRegisterButton = (Button) findViewById(R.id.registeredButton);
        mWorkTimeLengthButton = (Button) findViewById(R.id.length_work_hours_register_button);
        mFlexibleTypeWorkRadioButton = (RadioButton) findViewById(R.id.flexibleTypeWorkRegisterRadioButton);
        mStandardTypeWorkRadioButton = (RadioButton) findViewById(R.id.standardTypeWorkRegisterRadioButton);
        mStartStandardWorkTimeRegisterButton = (Button) findViewById(R.id.startStandardWorkTimeRegisterButton);
        mRadioGroupStandard = (RadioGroup) findViewById(R.id.idRadio_group1_standard_type_work);
        mRadioGroupFlexible = (RadioGroup) findViewById(R.id.idRadio_group2_flexible_type_work);
        mWorkDaysInWeek = (Button) findViewById(R.id.work_days_in_week);
        mWorkDaysInWeeklistView = (ListView) findViewById(R.id.work_days_in_week_list);

        mStartStandardWorkTimeRegisterButton.setVisibility(View.GONE);

        checkboxModelItems = new CheckboxModel[7];
        final String[] daysInWeek = getResources().getStringArray(R.array.work_days_in_week);

        checkboxModelItems[0] = new CheckboxModel(daysInWeek[0], 1);
        checkboxModelItems[1] = new CheckboxModel(daysInWeek[1], 1);
        checkboxModelItems[2] = new CheckboxModel(daysInWeek[2], 1);
        checkboxModelItems[3] = new CheckboxModel(daysInWeek[3], 1);
        checkboxModelItems[4] = new CheckboxModel(daysInWeek[4], 1);
        checkboxModelItems[5] = new CheckboxModel(daysInWeek[5], 0);
        checkboxModelItems[6] = new CheckboxModel(daysInWeek[6], 0);
// --------------------------------------------------------------------------
        formRegisterAutoValidation();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (checkValidation()) {
                if (true) {
                    User user = new User();
//                    user.setUserName(mUserName.getText().toString());
//                    user.setPassword(mUserPassword.getText().toString());
//                    user.setEmail(mEmail.getText().toString());
//                    user.setTypeWork(FormObtainerUtility.obtainTypeWorkNumber(mFlexibleTypeWorkRadioButton, mStandardTypeWorkRadioButton));
//                    user.setLengthTimeWork(mWorkTimeLengthButton.getText().toString());
//                    user.setStartStandardTimeWork(mStartStandardWorkTimeRegisterButton.getText().toString());
//
//                    WorkWeek workWeek = new WorkWeek();
//                    int count = mWorkDaysInWeeklistView.getAdapter().getCount();
//                    for (int x = 0; x <= count - 1; x++) {
//                        populateWorkWeek(workWeek, x);
//                    }
//                    user.setWorkWeek(workWeek);
//
////                    Toast.makeText(RegistrationActivity.this, "WorkWeek: isFriday? " + workWeek.getFriday(), Toast.LENGTH_SHORT).show();
//
                    Intent intent = new Intent(RegistrationActivity.this, WorkPlaceRegisterActivity.class);
//                    intent.putExtra("user", user);
//                    intent.putExtra("week", workWeek);
//                    if (user.getStartStandardTimeWork() != null) {
//                        intent.putExtra("UserStartStandardTimeWork", user.getStartStandardTimeWork());
//                    }

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });

        mWorkDaysInWeek.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {

               final CustomAdapter adapter = new CustomAdapter(RegistrationActivity.this, checkboxModelItems);
               mWorkDaysInWeeklistView.setAdapter(adapter);

               AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegistrationActivity.this);
//               builderSingle.setIcon(R.drawable.common_signin_btn_icon_disabled_focus_dark);
               builderSingle.setTitle(getResources().getString(R.string.select_work_week));

               builderSingle.setNegativeButton(getResources().getString(R.string.cancel_button),
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       });

               builderSingle.setPositiveButton(getResources().getString(R.string.ok_button),
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                           }
                       });

               builderSingle.setAdapter(adapter, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
//                               String strName = arrayAdapter.getItem(which);
                       CheckboxModel strName = adapter.getItem(which);

                       AlertDialog.Builder builderInner = new AlertDialog.Builder(RegistrationActivity.this);
                       builderInner.setMessage(strName.getName());
                       builderInner.setTitle("Your Selected Item is");
                       builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       });
                       builderInner.show();
                   }
               });
               builderSingle.show();


           }
       });

        mWorkTimeLengthButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Properties.HOURS_WORK_LENGTH_TIME_REGISTRATION;
                int minute = Properties.MINUTS_WORK_LENGTH_TIME_REGISTRATION;

                TimePickerDialog mWorkLengthTimePicker = new TimePickerDialog(RegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mWorkTimeLengthButton.setText(getString(R.string.work_length) + " " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mWorkLengthTimePicker.setTitle(getString(R.string.length_work_hours_register_text));
                mWorkLengthTimePicker.show();
            }
        });



        mStartStandardWorkTimeRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Properties.HOURS_START_WORK_TIME_REGISTRATION;
                int minute = Properties.MINUTES_START_WORK_TIME_REGISTRATION;

                TimePickerDialog mStartWorkTimePicker;
                mStartWorkTimePicker = new TimePickerDialog(RegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mStartStandardWorkTimeRegisterButton.setText(getString(R.string.your_start_work_time) + " " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mStartWorkTimePicker.setTitle(getString(R.string.start_work_time_register_text));
                mStartWorkTimePicker.show();
            }
        });

        mStandardTypeWorkRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroupFlexible.clearCheck();
                ((RadioButton) mRadioGroupFlexible.findViewById(R.id.flexibleTypeWorkRegisterRadioButton)).setChecked(false);
                mStartStandardWorkTimeRegisterButton.setVisibility(View.VISIBLE);
                mStartStandardWorkTimeRegisterButton.setEnabled(true);
                mStandardTypeWorkRadioButton.setError(null);
                mFlexibleTypeWorkRadioButton.setError(null);
            }
        });

        mFlexibleTypeWorkRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroupStandard.clearCheck();
                ((RadioButton) mRadioGroupStandard.findViewById(R.id.standardTypeWorkRegisterRadioButton)).setChecked(false);
                mStartStandardWorkTimeRegisterButton.setVisibility(View.GONE);
                mStandardTypeWorkRadioButton.setError(null);
                mFlexibleTypeWorkRadioButton.setError(null);
            }
        });



    }

    private WorkWeek populateWorkWeek(WorkWeek workWeek, int itemNumber) {
        CheckboxModel checkboxModel = (CheckboxModel) mWorkDaysInWeeklistView.getAdapter().getItem(itemNumber);
        if(itemNumber == 0 && checkboxModel.getValue() == 1){
            workWeek.setMonday(true);
        }else  if(itemNumber == 1 && checkboxModel.getValue() == 1){
            workWeek.setTuesday(true);
        }else  if(itemNumber == 2 && checkboxModel.getValue() == 1){
            workWeek.setWednesday(true);
        }else  if(itemNumber == 3 && checkboxModel.getValue() == 1){
            workWeek.setThursday(true);
        }else  if(itemNumber == 4 && checkboxModel.getValue() == 1){
            workWeek.setFriday(true);
        }else  if(itemNumber == 5 && checkboxModel.getValue() == 1){
            workWeek.setSaturday(true);
        }else  if(itemNumber == 6 && checkboxModel.getValue() == 1){
            workWeek.setSunday(true);
        }

        return workWeek;
    }

    //http://techlovejump.com/android-listview-with-checkbox/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }




    //  Validation Automatic
    private void formRegisterAutoValidation() {

        mUserName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(mUserName, getString(R.string.field_required));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(mEmail, true, getString(R.string.invalid_email));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        mUserPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isValidPassword(mUserPassword, getString(R.string.length_of_registration_password_is_not_enought));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    private boolean checkValidation() {
        boolean validationResult = true;

        if (!Validation.hasText(mUserName, getString(R.string.field_required))) validationResult = false;
        if (!Validation.isEmailAddress(mEmail, true, getString(R.string.invalid_email))) validationResult = false;
        if (!Validation.isValidPassword(mUserPassword, getString(R.string.length_of_registration_password_is_not_enought))) validationResult = false;
        if (!Validation.hasSetLengthWorkTime(mWorkTimeLengthButton, getString(R.string.field_required), getString(R.string.length_work_hours_register_text))){
            validationResult = false;
        }
        if (!Validation.hasText(mStartStandardWorkTimeRegisterButton, getString(R.string.start_work_time_empty))) validationResult = false;

        if (!Validation.isAnyRadioGroupSelected(mRadioGroupFlexible, mRadioGroupStandard)){
            validationResult = false;
            mFlexibleTypeWorkRadioButton.setError(getString(R.string.work_type_not_selected));
            mStandardTypeWorkRadioButton.setError(getString(R.string.work_type_not_selected));
        }else{
            if(mFlexibleTypeWorkRadioButton.isChecked()){
                Toast.makeText(RegistrationActivity.this, "FLEXIBLE CHOSEN", Toast.LENGTH_SHORT).show();

            }else if(mStandardTypeWorkRadioButton.isChecked()){
                Toast.makeText(RegistrationActivity.this, "STANDARD CHOSEN", Toast.LENGTH_SHORT).show();

                if(!Validation.hasSetStartWorkTime(mStartStandardWorkTimeRegisterButton, getString(R.string.start_work_time_empty), getString(R.string.start_work_time))){
                    validationResult = false;
                }
            }
        }

        if(!Validation.hasSelectedWorkDays(mWorkDaysInWeeklistView)) {
            validationResult = false;
            mWorkDaysInWeek.setError(getString(R.string.work_days_not_selected));
        }

        return validationResult;
    }

}