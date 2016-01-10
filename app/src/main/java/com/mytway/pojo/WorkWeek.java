package com.mytway.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkWeek implements Parcelable {

    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    private Boolean sunday;


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
}
