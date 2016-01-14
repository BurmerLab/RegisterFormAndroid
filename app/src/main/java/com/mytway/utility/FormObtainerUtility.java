package com.mytway.utility;

import android.widget.CheckBox;
import android.widget.RadioButton;

import com.mytway.pojo.TypeWork;

public class FormObtainerUtility {

    public static boolean obtainCheckboxStatus(CheckBox checkbox){
        //if checkbox.isChecked return true, then true, else return false
        return checkbox.isChecked() ? true : false;
    }

    public static TypeWork obtainTypeWorkNumber(RadioButton flexTypeWorkCheckbox, RadioButton standardTypeWorkCheckbox){
//        int typeWork;

        if(flexTypeWorkCheckbox.isChecked()) {
//            typeWork = TypeWork.FLEXIBLE_TYPE.getStatusCode();
            return TypeWork.FLEXIBLE_TYPE;
        }
        else if(standardTypeWorkCheckbox.isChecked()){
//            typeWork = TypeWork.STANDARD_TYPE.getStatusCode();
            return TypeWork.STANDARD_TYPE;
        }
        else{
//            typeWork = TypeWork.ERROR_TYPE.getStatusCode();
            return TypeWork.ERROR_TYPE;
        }
    }
}
