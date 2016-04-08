package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class FiveFragment extends IntermediateFragment{

    public FiveFragment() {
        // Required empty public constructor
    }

    TextView tv_PitchMotorVmax = null;
    TextView tv_RollMotorVmax = null;
    Spinner sp_GyroLpf = null;
    Spinner sp_Imu2FeedForwardLpf = null;
    Spinner sp_LowVoltageLimit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        Log.i("Storm32", "Frag5: onCreateView");
        removeControlsFromTables();
        View v =  inflater.inflate(R.layout.fragment_five, container, false);
        tv_PitchMotorVmax = (TextView) v.findViewById(R.id.textView_5_pitchMotorVmax);
        tv_RollMotorVmax = (TextView) v.findViewById(R.id.textView_5_rollMotorVmax);
        addPairTv(v, tv_PitchMotorVmax, 3);
        addPairTv(v, tv_RollMotorVmax, 9);

        sp_GyroLpf = (Spinner) v.findViewById((R.id.spinner_5_gyroLpf));
        sp_Imu2FeedForwardLpf = (Spinner) v.findViewById((R.id.spinner_5_imu2_feedForward_lpf));
        sp_LowVoltageLimit = (Spinner) v.findViewById((R.id.spinner_5_lowVoltageLimit));


        addPairSpinner(v, sp_GyroLpf, 100);
        addPairSpinner(v, sp_Imu2FeedForwardLpf, 99);
        addPairSpinner(v, sp_LowVoltageLimit, 18);

        //updateGUI();
        updateAllControlsAccordingToOptionList();
        return v;
    }


    public void onResume() {
        Log.i("Storm32 FF5", "onResume");
        super.onResume();

        //updateAllControlsAccordingToOptionList();

    }

}
