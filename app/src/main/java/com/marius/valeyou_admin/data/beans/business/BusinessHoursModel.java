package com.marius.valeyou_admin.data.beans.business;

public class BusinessHoursModel {
String day;
String fromTime;
String toTime;
boolean off;


    public BusinessHoursModel(String day, String fromTime, String toTime) {
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public BusinessHoursModel(){

    }


    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}
