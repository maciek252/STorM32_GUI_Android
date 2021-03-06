package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class FragmentRcInput extends IntermediateFragment{

    TextView tv_deadBand = null;
    TextView tv_rcHysteresis = null;

    TextView tv_rcPitchMin = null;
    TextView tv_rcPitchMax = null;
    TextView tv_rcPitchSpeedLimit = null;
    TextView tv_rcPitchAccelLimit = null;

    TextView tv_rcRollMin = null;
    TextView tv_rcRollMax = null;
    TextView tv_rcRollSpeedLimit = null;
    TextView tv_rcRollAccelLimit = null;

    TextView tv_rcYawMin = null;
    TextView tv_rcYawMax = null;
    TextView tv_rcYawSpeedLimit = null;
    TextView tv_rcYawAccelLimit = null;



    Spinner sp_rcPitch = null;
    Spinner sp_rcPitchMode = null;
    Spinner sp_rcRoll = null;
    Spinner sp_rcRollMode = null;
    Spinner sp_rcYaw = null;
    Spinner sp_rcYawMode = null;

    public FragmentRcInput() {
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
        Log.i("Storm32", "FF7: onCreateView: ");
        View v =  inflater.inflate(R.layout.fragment_rc_input, container, false);

        removeControlsFromTables();

         tv_rcHysteresis = (TextView) v.findViewById(R.id.textView_rcInput_rcHysteresis);
        tv_deadBand = (TextView) v.findViewById(R.id.textView_rcInput_rcDeadBand);

        tv_rcPitchMin = (TextView) v.findViewById(R.id.textView_rcInput_rcPitchMin);
        tv_rcPitchMax = (TextView) v.findViewById(R.id.textView_rcInput_rcPitchMax);

        tv_rcPitchSpeedLimit = (TextView) v.findViewById(R.id.textView_rcInput_rcPitchSpeedLimit);
        tv_rcPitchAccelLimit = (TextView) v.findViewById(R.id.textView_rcInput_rcPitchAccelLimit);

        tv_rcRollMin = (TextView) v.findViewById(R.id.textView_rcInput_rcRollMin);
        tv_rcRollMax = (TextView) v.findViewById(R.id.textView_rcInput_rcRollMax);


        tv_rcRollSpeedLimit = (TextView) v.findViewById(R.id.textView_rcInput_rcRollSpeedLimit);
        tv_rcRollAccelLimit = (TextView) v.findViewById(R.id.textView_rcInput_rcRollAccelLimit);

        tv_rcYawMin = (TextView) v.findViewById(R.id.textView_rcInput_rcYawMin);
        tv_rcYawMax = (TextView) v.findViewById(R.id.textView_rcInput_rcYawMax);

        tv_rcYawSpeedLimit = (TextView) v.findViewById(R.id.textView_rcInput_rcYawSpeedLimit);
        tv_rcYawAccelLimit = (TextView) v.findViewById(R.id.textView_rcInput_rcYawAccelLimit);
        
        
        sp_rcPitch = (Spinner) v.findViewById(R.id.spinner_RcInput_rcPitch);
        sp_rcPitchMode = (Spinner) v.findViewById(R.id.spinner_rcInput_rcPitchMode);

        sp_rcRoll = (Spinner) v.findViewById(R.id.spinner_rcInput_rcRoll);
        sp_rcRollMode = (Spinner) v.findViewById(R.id.spinner_rcInput_rcRollMode);

        sp_rcYaw = (Spinner) v.findViewById(R.id.spinner_rcInput_rcYaw);
        sp_rcYawMode = (Spinner) v.findViewById(R.id.spinner_rcInput_rcYawMode);

        //spinner_panControlMode = (Spinner) v.findViewById(R.id.spinner_6_panControlMode);
        //addPairSpinner(v, spinner_panModeDefaultSetting, 66);

        //addPair(v, R.id.textView_6_pid, optionList.lowVoltageLimitInt);
        addPairTv(v, tv_rcHysteresis, 105);
        addPairTv(v, tv_deadBand, 43);

        addPairTv(v, tv_rcPitchMin, 47);
        addPairTv(v, tv_rcPitchMax, 48);
        addPairTv(v, tv_rcPitchSpeedLimit, 49);
        addPairTv(v, tv_rcPitchAccelLimit, 50);

        addPairTv(v, tv_rcRollMin, 54);
        addPairTv(v, tv_rcRollMax, 55);
        addPairTv(v, tv_rcRollSpeedLimit, 56);
        addPairTv(v, tv_rcRollAccelLimit, 57);

        addPairTv(v, tv_rcYawMin, 61);
        addPairTv(v, tv_rcYawMax, 62);
        addPairTv(v, tv_rcYawSpeedLimit, 63);
        addPairTv(v, tv_rcYawAccelLimit, 64);

        
        
        addPairSpinner(v, sp_rcPitch, 44);
        addPairSpinner(v, sp_rcPitchMode, 45);
        addPairSpinner(v, sp_rcRoll, 51);
        addPairSpinner(v, sp_rcRollMode, 52);
        addPairSpinner(v, sp_rcYaw, 58);
        addPairSpinner(v, sp_rcYawMode, 59);

//        updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }

}
