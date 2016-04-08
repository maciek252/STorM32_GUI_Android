package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import info.androidhive.materialtabs.R;


public class SixFragment extends IntermediateFragment{

    Spinner spinner_panControlMode;
    Spinner spinner_panModeDefaultSetting;


    public SixFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_six, container, false);

        removeControlsFromTables();

        spinner_panControlMode = (Spinner) v.findViewById(R.id.spinner_6_panControlMode);

        spinner_panModeDefaultSetting = (Spinner) v.findViewById(R.id.spinner_6_panModeDefaultSetting);
        //addPairSpinner(v, spinner_panControlMode, 65);
        addPairSpinner(v, spinner_panModeDefaultSetting, 66);

        addPairSpinner(v, spinner_panControlMode, 65);

        //addPair(v, R.id.textView_6_pid, optionList.lowVoltageLimitInt);


        updateAllControlsAccordingToOptionList();

        return v;
    }

}
