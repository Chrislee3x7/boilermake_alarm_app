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
