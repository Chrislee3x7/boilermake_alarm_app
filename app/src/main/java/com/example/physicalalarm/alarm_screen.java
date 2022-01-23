package com.example.physicalalarm;

import android.os.Bundle;

import android.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link alarm_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class alarm_screen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button powerButton;
    private AlarmTime editingAlarmTime;
    private ImageView powerButtonImageOn;
    private ImageView powerButtonImageOff;
    private ImageView circle;

    public alarm_screen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment alarm_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static alarm_screen newInstance(String param1, String param2) {
        alarm_screen fragment = new alarm_screen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(getContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_bottom));
        setExitTransition(inflater.inflateTransition(R.transition.slide_bottom));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_alarm_screen, container, false);
        powerButton = root.findViewById(R.id.powerbutton);
        powerButtonImageOn = root.findViewById(R.id.power2);
        powerButtonImageOff = root.findViewById(R.id.power1);
        circle = root.findViewById(R.id.circle);
        Animation aniRotateClk = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_anticlockwise);

        editingAlarmTime = MainActivity.selectedTime;
        if(editingAlarmTime.isOn() == true){
            powerButtonImageOn.setVisibility(View.VISIBLE);
            powerButtonImageOff.setVisibility(View.GONE);

            circle.startAnimation(aniRotateClk);
            aniRotateClk.setRepeatCount(Animation.INFINITE);
        } else {
            powerButtonImageOn.setVisibility(View.GONE);
            powerButtonImageOff.setVisibility(View.VISIBLE);
            aniRotateClk.cancel();
        }

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingAlarmTime.setOn(!editingAlarmTime.isOn());
                if(editingAlarmTime.isOn() == true){
                    powerButtonImageOn.setVisibility(View.VISIBLE);
                    powerButtonImageOff.setVisibility(View.GONE);

                    circle.startAnimation(aniRotateClk);
                    aniRotateClk.setRepeatCount(Animation.INFINITE);
                } else {
                    powerButtonImageOn.setVisibility(View.GONE);
                    powerButtonImageOff.setVisibility(View.VISIBLE);
                    aniRotateClk.cancel();
                }
            }
        });
        return root;
    }
}