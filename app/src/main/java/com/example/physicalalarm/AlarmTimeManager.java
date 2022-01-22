package com.example.physicalalarm;

import java.util.ArrayList;

/**
 * A class to help with managing alarm times
 */
public class AlarmTimeManager {

    private ArrayList<AlarmTime> alarmTimes = new ArrayList<AlarmTime>();

    public AlarmTimeManager(){

        // TODO implement file reading to create AlarmTime objects and add to the alarmTimes ArrayList

    }

    public ArrayList<AlarmTime> getAlarmTimes() {
        return alarmTimes;
    }

    public ArrayList<AlarmTime> getAlarmTimes12to3() {
        ArrayList<AlarmTime> newArray = new ArrayList<AlarmTime>();
        for(AlarmTime a : alarmTimes){
            if((a.getHour() >= 12 && a.getHour() <= 14) || (a.getHour() >= 0 && a.getHour() <= 2)){
                newArray.add(a);
            }
        }
        quickSort(newArray, 0, newArray.size()-1);
        return newArray;
    }

    public ArrayList<AlarmTime> getAlarmTimes3to6() {
        ArrayList<AlarmTime> newArray = new ArrayList<AlarmTime>();
        for(AlarmTime a : alarmTimes){
            if((a.getHour() >= 15 && a.getHour() <= 17) || (a.getHour() >= 3 && a.getHour() <= 5)){
                newArray.add(a);
            }
        }
        quickSort(newArray, 0, newArray.size()-1);
        return newArray;
    }

    public ArrayList<AlarmTime> getAlarmTimes6to9() {
        ArrayList<AlarmTime> newArray = new ArrayList<AlarmTime>();
        for(AlarmTime a : alarmTimes){
            if((a.getHour() >= 18 && a.getHour() <= 20) || (a.getHour() >= 6 && a.getHour() <= 8)){
                newArray.add(a);
            }
        }
        quickSort(newArray, 0, newArray.size()-1);
        return newArray;
    }

    public ArrayList<AlarmTime> getAlarmTimes9to12() {
        ArrayList<AlarmTime> newArray = new ArrayList<AlarmTime>();
        for(AlarmTime a : alarmTimes){
            if((a.getHour() >= 21 && a.getHour() <= 23) || (a.getHour() >= 9 && a.getHour() <= 11)){
                newArray.add(a);
            }
        }
        quickSort(newArray, 0, newArray.size()-1);
        return newArray;
    }

    public void sortAlarmTimes(){
        quickSort(alarmTimes, 0, alarmTimes.size()-1);
    }

    public void addAlarmTime(AlarmTime a){
        alarmTimes.add(a);
    }

    public void removeAlarmTime(AlarmTime a){
        alarmTimes.remove(a);
    }

    public void updateAlarmTimesFile(){

        // TODO implement file writing to update .txt file in AppData
    }

    private void quickSort(ArrayList<AlarmTime> arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex-1);
            quickSort(arr, partitionIndex+1, end);
        }
    }

    private int partition(ArrayList<AlarmTime> arr, int begin, int end) {
        int pivot = arr.get(end).getCombined();
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (arr.get(j).getCombined() <= pivot) {
                i++;

                AlarmTime swapTemp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, swapTemp);
            }
        }

        AlarmTime swapTemp = arr.get(i+1);
        arr.set(i+1, arr.get(end));
        arr.set(end, swapTemp);

        return i+1;
    }
}
