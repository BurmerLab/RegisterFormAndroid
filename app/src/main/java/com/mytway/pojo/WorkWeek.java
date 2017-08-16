package com.mytway.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkWeek implements Parcelable {

    private Boolean monday = false;
    private Boolean tuesday = false;
    private Boolean wednesday = false;
    private Boolean thursday = false;
    private Boolean friday = false;
    private Boolean saturday = false;
    private Boolean sunday = false;

    public WorkWeek() {
    }

    public WorkWeek(Boolean monday, Boolean tuesday, Boolean wednesday, Boolean thursday, Boolean friday, Boolean saturday, Boolean sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public Boolean getMonday() {
        return monday;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.monday);
        dest.writeValue(this.tuesday);
        dest.writeValue(this.wednesday);
        dest.writeValue(this.thursday);
        dest.writeValue(this.friday);
        dest.writeValue(this.saturday);
        dest.writeValue(this.sunday);
    }

    private WorkWeek(Parcel in) {
        this.monday = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.tuesday = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.wednesday = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.thursday = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.friday = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.saturday = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.sunday = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<WorkWeek> CREATOR = new Parcelable.Creator<WorkWeek>() {
        public WorkWeek createFromParcel(Parcel source) {
            return new WorkWeek(source);
        }

        public WorkWeek[] newArray(int size) {
            return new WorkWeek[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkWeek)) return false;

        WorkWeek workWeek = (WorkWeek) o;

        if (!monday.equals(workWeek.monday)) return false;
        if (!tuesday.equals(workWeek.tuesday)) return false;
        if (!wednesday.equals(workWeek.wednesday)) return false;
        if (!thursday.equals(workWeek.thursday)) return false;
        if (!friday.equals(workWeek.friday)) return false;
        if (!saturday.equals(workWeek.saturday)) return false;
        return sunday.equals(workWeek.sunday);

    }

    @Override
    public int hashCode() {
        int result = monday.hashCode();
        result = 31 * result + tuesday.hashCode();
        result = 31 * result + wednesday.hashCode();
        result = 31 * result + thursday.hashCode();
        result = 31 * result + friday.hashCode();
        result = 31 * result + saturday.hashCode();
        result = 31 * result + sunday.hashCode();
        return result;
    }

    public static WorkWeek createWorkWeekFromString(String workWeekText) {
        WorkWeek workWeek = new WorkWeek();

        for(int x = 0 ; x<= workWeekText.length() -1 ; x++){

            String checked = Character.toString(workWeekText.charAt(x));
            int eachDayNumber = Integer.parseInt(checked);

            if(x == 0 && eachDayNumber == 1){
                workWeek.setMonday(true);
            }else  if(x == 1 && eachDayNumber == 1){
                workWeek.setTuesday(true);
            }else  if(x == 2 && eachDayNumber == 1){
                workWeek.setWednesday(true);
            }else  if(x == 3 && eachDayNumber == 1){
                workWeek.setThursday(true);
            }else  if(x == 4 && eachDayNumber == 1){
                workWeek.setFriday(true);
            }else  if(x == 5 && eachDayNumber == 1){
                workWeek.setSaturday(true);
            }else  if(x == 6 && eachDayNumber == 1) {
                workWeek.setSunday(true);
            }
        }
        return workWeek;
    }

    public boolean checkIsDayEnable(int dayOfWeek){
        if(dayOfWeek <= 7){
            if(dayOfWeek == 1){
                return monday;
            }else if(dayOfWeek == 2){
                return tuesday;
            }else if(dayOfWeek == 3){
                return wednesday;
            }else if(dayOfWeek == 4){
                return thursday;
            }else if(dayOfWeek == 5){
                return friday;
            }else if(dayOfWeek == 6){
                return tuesday;
            }else if(dayOfWeek == 7){
                return sunday;
            }
        }
        return false;
    }
}
