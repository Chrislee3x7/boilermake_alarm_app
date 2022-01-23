package com.example.physicalalarm;

import androidx.annotation.NonNull;

/**
 * A class to represent one alarm time, in 24 hour time
 */
public class AlarmTime {

    private int hour;
    private int minute;
    private boolean isOn;

    public AlarmTime(int hour, int minute, boolean isOn){
        this.hour = hour;
        this.minute = minute;
        this.isOn = isOn;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getCombined(){
        return hour*100 + minute;
    }

    @Override
    public String toString() {
        String AMPM = "AM";
        if (hour >= 12) {
            AMPM = "PM";
        }
        return hour % 12 + ":" + minute + " " + AMPM;
    }
}
