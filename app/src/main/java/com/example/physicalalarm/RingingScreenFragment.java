package com.example.physicalalarm;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RingingScreenFragment} factory method to
 * create an instance of this fragment.
 */
public class RingingScreenFragment extends Fragment {

    private SensorManager sm;

    private float xAccel;
    private float yAccel;
    private float zAccel;

    private RingingScreenFragment thisFrag;

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            xAccel = Math.abs(event.values[0]);
            yAccel = Math.abs(event.values[1]);
            zAccel = Math.abs(event.values[2]);
            System.out.println("x=" + xAccel + "y=" + yAccel + "z=" +zAccel);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisFrag = this;
        sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        double[] accValues = new double[100];
        final int[] indexToBeReplaced = {0};

        final Handler someHandler = new Handler(getMainLooper());
        final boolean[] notShaken = {true};
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double netAcc = Math.sqrt(xAccel*xAccel + yAccel*yAccel + zAccel*zAccel);
                accValues[indexToBeReplaced[0]] = netAcc;
                indexToBeReplaced[0] = (indexToBeReplaced[0] + 1) % 100;

                double totalAcc = 0;
                for(int i=0; i<100; i++){
                    totalAcc += accValues[i];
                }

                totalAcc /= 100;

                if(notShaken[0] && totalAcc >= 10){
                    Toast.makeText(getActivity().getApplicationContext(), "Good Exercise", Toast.LENGTH_SHORT).show();
                    notShaken[0] = false;
                    if(SoundPlayer.isPlaying){
                        SoundPlayer.stopSound();
                    }
                }

                someHandler.postDelayed(this, 100);

            }
        }, 10);


        return inflater.inflate(R.layout.fragment_ringing_screen, container, false);
    }
}