package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class FragmentSetup extends IntermediateFragment{

    TextView tv_imuAhrs = null;
    TextView tv_rcPitchOffset = null;
    TextView tv_rcRollOffset = null;
    TextView tv_rcYawOffset = null;

    Spinner sp_imu2configuration = null;
    Spinner sp_accCompensationMethod = null;
    Spinner sp_virtualChannelConfiguration = null;
    Spinner sp_pwmOutConfiguration = null;

    Spinner sp_pitchMotorUsage = null;
    Spinner sp_rollMotorUsage = null;
    Spinner sp_yawMotorUsage = null;

    Spinner sp_beepWithMotors = null;

    public FragmentSetup() {
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
        Log.i("Storm32", "FFSetup: onCreateView: ");
        View v =  inflater.inflate(R.layout.fragment_setup, container, false);

        removeControlsFromTables();


        tv_imuAhrs = (TextView) v.findViewById(R.id.textView_setup_imuAhrs);
        tv_rcPitchOffset = (TextView) v.findViewById(R.id.textView_setup_rcPitchOffset);
        tv_rcRollOffset = (TextView) v.findViewById(R.id.textView_setup_rcRollOffset);
        tv_rcYawOffset = (TextView) v.findViewById(R.id.textView_setup_rcYawOffset);

        sp_accCompensationMethod
                 = (Spinner) v.findViewById(R.id.spinner_setup_accCompensationMethod);
        sp_beepWithMotors
         = (Spinner) v.findViewById(R.id.spinner_setup_beepWithMotors);

        sp_imu2configuration = (Spinner) v.findViewById(R.id.spinner_setup_imu2configuration);
        sp_pitchMotorUsage = (Spinner) v.findViewById(R.id.spinner_setup_pitchMotorUsage);
        sp_rollMotorUsage = (Spinner) v.findViewById(R.id.spinner_setup_rollMotorUsage);
        sp_yawMotorUsage = (Spinner) v.findViewById(R.id.spinner_setup_yawMotorUsage);
        sp_pwmOutConfiguration = (Spinner) v.findViewById(R.id.spinner_setup_pwmOutConfiguration);
        sp_virtualChannelConfiguration =
                (Spinner) v.findViewById(R.id.spinner_setup_virtualChannelConfiguration);

        //addPair(v, R.id.textView_6_pid, optionList.lowVoltageLimitInt);
        addPairTv(v, tv_imuAhrs, 81);
        addPairTv(v, tv_rcPitchOffset, 106);

        addPairSpinner(v, sp_imu2configuration, 94);

//        updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }

}
