package com.mytway.validation;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mytway.activity.R;
import com.mytway.utility.Helper;

import java.util.regex.Pattern;

public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final int PASSWORD_LENGTH_REQUIRED = 6;

    // Error Messages
    public static final String REQUIRED_MSG = "Required";
//    private static final String REQUIRED_MSG = Helper.getContext().getResources().getString(R.string.field_required);

    public static final String NOT_ENOUGHT_LENGTH_PASSWORD = "Password length not eought";
//    public static final String NOT_ENOUGHT_LENGTH_PASSWORD = Helper.getContext().getResources().getString(R.string.length_of_registration_password_is_not_enought);

    public static final String WORK_LENGTH_BUTTON_EMPTY = "Work length is empty";
//    public static final String WORK_LENGTH_BUTTON_EMPTY = Helper.getContext().getResources().getString(R.string.work_length_empty);

    public static final String START_WORK_TIME_EMPTY = "Start work time is empty";
//    public static final String START_WORK_TIME_EMPTY = Helper.getContext().getResources().getString(R.string.start_work_time_empty);

    public static final String WORK_TYPE_RADIO_BUTTON_EMPTY = "Work type not selected";
//    public static final String WORK_TYPE_RADIO_BUTTON_EMPTY = Helper.getContext().getResources().getString(R.string.work_type_not_selected);
    public static final String EMAIL_MSG = "invalid email";
//    public static final String EMAIL_MSG = Helper.getContext().getResources().getString(R.string.invalid_email);

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required, String errorMessage) {
        return isValid(editText, EMAIL_REGEX, errorMessage, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(TextView textView, String regex, String errMsg, boolean required) {

        String text = textView.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        textView.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(textView, errMsg) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            textView.setError(errMsg);
            return false;
        };
        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(TextView textView, String errorMessage) {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            textView.setError(errorMessage);
            return false;
        }

        return true;
    }

    public static boolean hasSetStartWorkTime(TextView textView, String errorMessage, String startWorkButtonTitlePattern) {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        if(text.length() == 0 || text.equals(startWorkButtonTitlePattern)){
            textView.setError(errorMessage);
            return false;
        }
        return true;
    }

    public static boolean hasSetLengthWorkTime(TextView textView, String errorMessage, String lengthWorkButtonTitlePattern) {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        if(text.length() == 0 || text.equals(lengthWorkButtonTitlePattern)){
            textView.setError(errorMessage);
            return false;
        }

        return true;
    }

    public static void insertErrorMessageToButton(Button button) {
        button.setError(WORK_LENGTH_BUTTON_EMPTY);
    }

    public static void insertErrorMessageToRadioButtons(RadioButton radioButton1) {
        radioButton1.setError(WORK_LENGTH_BUTTON_EMPTY);
    }

    public static boolean isAnyRadioGroupSelected(RadioGroup... radioGroups) {
        boolean isRadioButtonChecked = false;

        for(RadioGroup radioGroup : radioGroups){
            if(radioGroup.getCheckedRadioButtonId() == -1){
                //radio is no checked in group
                isRadioButtonChecked = false;
            }else{
                isRadioButtonChecked = true;
                break;
            }
        }
        return isRadioButtonChecked;
    }

    public static boolean isStandardStartWorkTimeInserted(Button mSetStartStandardWorkTimeRegisterButton, RadioGroup mRadioGroupStandard) {
        boolean isStandardWorkTimeInserted = false;

        if(mRadioGroupStandard.getCheckedRadioButtonId() != -1){
            //standard type radio is checked in group
            if(Validation.hasText(mSetStartStandardWorkTimeRegisterButton, "TU")){
                return true;
            }else{
                mSetStartStandardWorkTimeRegisterButton.setError(START_WORK_TIME_EMPTY);
            }
        }
        return isStandardWorkTimeInserted;
    }

    // validating password with retype password
    public static boolean isValidPassword(EditText editText, String errorMessage) {
        if (editText.getText().toString() != null && editText.getText().toString().length() > PASSWORD_LENGTH_REQUIRED) {
            return true;
        }
        editText.setError(NOT_ENOUGHT_LENGTH_PASSWORD);
        return false;
    }
}
