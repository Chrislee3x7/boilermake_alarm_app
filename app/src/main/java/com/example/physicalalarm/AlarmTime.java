package com.example.physicalalarm;

/**
 * A class to represent one alarm time, in 24 hour time
 */
public class AlarmTime {

    private int hour;
    private int minute;

    public AlarmTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
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

    public int getCombined(){
        return hour*100 + minute;
    }
}
