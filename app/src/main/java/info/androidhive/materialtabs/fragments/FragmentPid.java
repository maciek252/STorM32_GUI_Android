package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class FragmentPid extends IntermediateFragment{

    public FragmentPid() {
        // Required empty public constructor
    }

    TextView tv_PitchMotorVmax = null;
    TextView tv_RollMotorVmax = null;

    TextView tv_PitchP = null;
    TextView tv_PitchI = null;
    TextView tv_PitchD = null;

    TextView tv_RollP = null;
    TextView tv_RollI = null;
    TextView tv_RollD = null;

    TextView tv_YawP = null;
    TextView tv_YawI = null;
    TextView tv_YawD = null;


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
        View v =  inflater.inflate(R.layout.fragment_pid, container, false);
        tv_PitchMotorVmax = (TextView) v.findViewById(R.id.textView_5_pitchMotorVmax);
        tv_RollMotorVmax = (TextView) v.findViewById(R.id.textView_5_rollMotorVmax);

        tv_PitchP = (TextView) v.findViewById(R.id.textView_pid_pitchP);
        tv_PitchI = (TextView) v.findViewById(R.id.textView_pid_pitchI);
        tv_PitchD = (TextView) v.findViewById(R.id.textView_pid_pitchD);


        tv_RollP = (TextView) v.findViewById(R.id.textView_pid_rollP);
        tv_RollI = (TextView) v.findViewById(R.id.textView_pid_rollI);
        tv_RollD = (TextView) v.findViewById(R.id.textView_pid_rollD);

        tv_YawP = (TextView) v.findViewById(R.id.textView_pid_yawP);
        tv_YawI = (TextView) v.findViewById(R.id.textView_pid_yawI);
        tv_YawD = (TextView) v.findViewById(R.id.textView_pid_yawD);

        addPairTv(v, tv_PitchMotorVmax, 3);
        addPairTv(v, tv_RollMotorVmax, 9);

        addPairTv(v, tv_PitchP, 0);
        addPairTv(v, tv_PitchI, 1);
        addPairTv(v, tv_PitchD, 2);
        
        addPairTv(v, tv_RollP, 6);
        addPairTv(v, tv_RollI, 7);
        addPairTv(v, tv_RollD, 8);

        addPairTv(v, tv_YawP, 12);
        addPairTv(v, tv_YawI, 13);
        addPairTv(v, tv_YawD, 14);


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
