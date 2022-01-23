package com.example.physicalalarm;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.sql.Time;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumberPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberPickerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NumberPickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NumberPickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NumberPickerFragment newInstance(String param1, String param2) {
        NumberPickerFragment fragment = new NumberPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private TimePicker timePicker;
    private Button confirmBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_number_picker, container, false);
        timePicker = root.findViewById(R.id.timePicker);
        confirmBtn = root.findViewById(R.id.confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmTime alarmTime = new AlarmTime(timePicker.getHour(), timePicker.getMinute(), false);
                AlarmTimeManager alarmTimeManager = ((MainActivity) getActivity()).getAlarmTimeManager();
                alarmTimeManager.addAlarmTime(alarmTime);
                getActivity().getFragmentManager().popBackStack();
            }
        });
        return root;
    }
}