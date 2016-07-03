package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class FragmentPan extends IntermediateFragment{


    Spinner spinner_panControlMode;
    Spinner spinner_panModeDefaultSetting;


    TextView tv_pitchPan = null;
    TextView tv_pitchPanExpo = null;
    TextView tv_pitchPanDeadband = null;


    TextView tv_rollPan = null;
    TextView tv_rollPanExpo = null;
    TextView tv_rollPanDeadband = null;


    TextView tv_yawPan = null;
    TextView tv_yawPanExpo = null;
    TextView tv_yawPanDeadband = null;
    TextView tv_yawPanDeadbandLPF = null;
    TextView tv_yawPanDeadbandHysteresis = null;



    public FragmentPan() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Storm32", "FF6: onCreateView! ");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_pan, container, false);

        removeControlsFromTables();

        spinner_panControlMode = (Spinner) v.findViewById(R.id.spinner_6_panControlMode);

        spinner_panModeDefaultSetting = (Spinner) v.findViewById(R.id.spinner_6_panModeDefaultSetting);
        //addPairSpinner(v, spinner_panControlMode, 65);
        addPairSpinner(v, spinner_panModeDefaultSetting, 66);

        addPairSpinner(v, spinner_panControlMode, 65);


        tv_pitchPan = (TextView) v.findViewById(R.id.textView_pan_pitchPan);
        tv_pitchPanDeadband = (TextView) v.findViewById(R.id.textView_pan_pitchPanDeadband);
        tv_pitchPanExpo = (TextView) v.findViewById(R.id.textView_pan_pitchPanExpo);

        tv_rollPan = (TextView) v.findViewById(R.id.textView_pan_rollPan);
        tv_rollPanDeadband = (TextView) v.findViewById(R.id.textView_pan_rollPanDeadband);
        tv_rollPanExpo = (TextView) v.findViewById(R.id.textView_pan_rollPanExpo);

        tv_yawPan = (TextView) v.findViewById(R.id.textView_pan_yawPan);
        tv_yawPanDeadband = (TextView) v.findViewById(R.id.textView_pan_yawPanDeadband);
        tv_yawPanExpo = (TextView) v.findViewById(R.id.textView_pan_yawPanExpo);
        tv_yawPanDeadbandHysteresis = (TextView) v.findViewById(R.id.textView_pan_yawPanDeadbandHysteresis);
        tv_yawPanDeadbandLPF = (TextView) v.findViewById(R.id.textView_pan_yawPanDeadbandLPF);


        addPairTv(v, tv_pitchPan, 4);
        addPairTv(v, tv_pitchPanDeadband, 17);
        addPairTv(v, tv_pitchPanExpo, 104);

        addPairTv(v, tv_rollPan, 10);
        addPairTv(v, tv_rollPanDeadband, 11);
        addPairTv(v, tv_rollPanExpo, 103);

        addPairTv(v, tv_yawPan, 16);
        addPairTv(v, tv_yawPanDeadband, 17);
        addPairTv(v, tv_yawPanExpo, 104);

        addPairTv(v, tv_yawPanDeadbandHysteresis, 97);
        addPairTv(v, tv_yawPanDeadbandLPF, 118);


        //addPair(v, R.id.textView_6_pid, optionList.lowVoltageLimitInt);

        //updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }

}
