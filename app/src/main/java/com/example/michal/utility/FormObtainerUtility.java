package com.example.michal.utility;

import android.widget.CheckBox;

import com.example.michal.pojo.TypeWork;

public class FormObtainerUtility {

    public static boolean obtainCheckboxStatus(CheckBox checkbox){
        //if checkbox.isChecked return true, then true, else return false
        return checkbox.isChecked() ? true : false;
    }

    public static int obtainTypeWorkNumber(CheckBox flexTypeWorkCheckbox, CheckBox standardTypeWorkCheckbox){
        int typeWorkNumber;

        int typeWorkStatus;

        if(flexTypeWorkCheckbox.isChecked()) {
            typeWorkNumber = TypeWork.FLEXIBLE.getStatusCode();
        }
        else if(standardTypeWorkCheckbox.isChecked()){
            typeWorkNumber = TypeWork.STANDARD.getStatusCode();
        }
        else{
            typeWorkNumber = TypeWork.ERROR_TYPE.getStatusCode();
        }

        return typeWorkNumber;
    }


}
