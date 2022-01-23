package com.example.physicalalarm;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class to help with managing alarm times
 */
public class AlarmTimeManager {

    /**
     * This ArrayList contains all AlarmTime objects
     */
    private ArrayList<AlarmTime> alarmTimes = new ArrayList<AlarmTime>();

    /**
     * Context to be used with some methods
     */
    private Context context;

    /**
     * This constructor will make the AlarmTimeManager object, and read from the alarmtimes.txt file to populate
     * the AlarmTime ArrayList with AlarmTime objects.
     * @param context
     */
    public AlarmTimeManager(Context context){

        this.context = context;
        try {
            BufferedReader br = new BufferedReader(new FileReader("alarmtimes.txt"));
            String line = br.readLine();
            while(line!=null){
                String[] data = line.split(",");
                AlarmTime at = new AlarmTime(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Boolean.parseBoolean(data[2]));
                alarmTimes.add(at);
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException e) {
            File file = new File(context.getFilesDir(), "alarmtimes.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * This method returns the AlarmTime ArrayList
     * @return
     */
    public ArrayList<AlarmTime> getAlarmTimes() {
        return alarmTimes;
    }

    /**
     * This method returns the AlarmTimes that fall between 12 and 3 AM/PM
     * @return
     */
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

    /**
     * This method returns the AlarmTimes that fall between 3 and 6 AM/PM
     * @return
     */
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

    /**
     * This method returns the AlarmTimes that fall between 6 and 9 AM/PM
     * @return
     */
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

    /**
     * This method returns the AlarmTimes that fall between 9 and 12 AM/PM
     * @return
     */
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

    /**
     * This method will sort the current AlarmTime ArrayList
     */
    public void sortAlarmTimes(){
        quickSort(alarmTimes, 0, alarmTimes.size()-1);
    }

    /**
     * This method will remove an AlarmTime object to the ArrayList
     * @param a
     */
    public void addAlarmTime(AlarmTime a){
        alarmTimes.add(a);
        updateAlarmTimesFile();
    }

    /**
     * This method will remove an AlarmTime object to the ArrayList
     * @param a
     */
    public void removeAlarmTime(AlarmTime a){
        alarmTimes.remove(a);
        updateAlarmTimesFile();
    }

    /**
     * This method will go through the AlarmTime ArrayList and write all the data into the
     * alarmtimes.txt file
     *
     * File formatting example:
     * 6,30,true
     * 14,15,false
     * 10,30,false
     */
    public void updateAlarmTimesFile(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("alarmtimes.txt"));
            for(AlarmTime a : alarmTimes){
                bw.write(a.getHour() + "," + a.getMinute() + "," + a.isOn());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            File file = new File(context.getFilesDir(), "alarmtimes.txt");
        }
    }

    /**
     * Helper method to quickly sort an array
     * @param arr
     * @param begin
     * @param end
     */
    private void quickSort(ArrayList<AlarmTime> arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex-1);
            quickSort(arr, partitionIndex+1, end);
        }
    }

    /**
     * Helper method to quickly sort an array
     * @param arr
     * @param begin
     * @param end
     * @return
     */
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
