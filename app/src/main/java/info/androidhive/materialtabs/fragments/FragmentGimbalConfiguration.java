package info.androidhive.materialtabs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.materialtabs.R;


public class FragmentGimbalConfiguration extends IntermediateFragment{

    Spinner sp_imu_orientation;
    Spinner sp_imu2_orientation;
    Button b_configureGimbalTool = null;


    TextView tv_pitchMotorPoles = null;
    Spinner sp_pitchMotorDirection = null;
    TextView tv_pitchStartupMotorPosition = null;
    TextView tv_pitchOffset = null;

    TextView tv_rollMotorPoles = null;
    Spinner sp_rollMotorDirection = null;
    TextView tv_rollStartupMotorPosition = null;
    TextView tv_rollOffset = null;

    TextView tv_yawMotorPoles = null;
    Spinner sp_yawMotorDirection = null;
    TextView tv_yawStartupMotorPosition = null;
    TextView tv_yawOffset = null;

    public FragmentGimbalConfiguration() {
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
        View v =  inflater.inflate(R.layout.fragment_gimbal_configuration, container, false);

        removeControlsFromTables();

        sp_imu_orientation = (Spinner) v.findViewById(R.id.spinner_gimconf_imu_orientation);
        sp_imu2_orientation = (Spinner) v.findViewById(R.id.spinner_gimconf_imu2_orientation);

        tv_pitchMotorPoles = (TextView) v.findViewById(R.id.textView_gimconf_pitch_motor_poles);
        tv_pitchOffset = (TextView) v.findViewById(R.id.textView_gimconf_pitch_offset);
        tv_pitchStartupMotorPosition = (TextView) v.findViewById(R.id.textView_gimconf_pitch_startup_motor_pos);
        sp_pitchMotorDirection = (Spinner) v.findViewById(R.id.spinner_gimconf_pitch_motor_direction);

        tv_rollMotorPoles = (TextView) v.findViewById(R.id.textView_gimconf_roll_motor_poles);
        tv_rollOffset = (TextView) v.findViewById(R.id.textView_gimconf_roll_offset);
        tv_rollStartupMotorPosition = (TextView) v.findViewById(R.id.textView_gimconf_roll_startup_motor_pos);
        sp_rollMotorDirection = (Spinner) v.findViewById(R.id.spinner_gimconf_roll_motor_direction);

        tv_yawMotorPoles = (TextView) v.findViewById(R.id.textView_gimconf_yaw_motor_poles);
        tv_yawOffset = (TextView) v.findViewById(R.id.textView_gimconf_yaw_offset);
        tv_yawStartupMotorPosition = (TextView) v.findViewById(R.id.textView_gimconf_yaw_startup_motor_pos);
        sp_yawMotorDirection = (Spinner) v.findViewById(R.id.spinner_gimconf_yaw_motor_direction);

        b_configureGimbalTool = (Button) v.findViewById(R.id.button_gimbal_configuration_configure_gimbal_tool);
        b_configureGimbalTool.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Toast.makeText(getContext(), "button", Toast.LENGTH_LONG).show();
                    FragmentConfigureGimbalTool1 f1 = new FragmentConfigureGimbalTool1();
    //                    ActivityConfigureGimbal1 activityConfigureGimbal1 = new ActivityConfigureGimbal1();
                Intent intent = new Intent(getActivity(), ActivityConfigureGimbal1.class);
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);


                //Intent intent = new Intent(this, DisplayMessageActivity.class);


            }
        });


        addPairSpinner(v, sp_imu_orientation, 39);
        addPairSpinner(v, sp_imu2_orientation, 95);

        addPairTv(v, tv_pitchMotorPoles, 20);
        addPairTv(v, tv_pitchOffset, 22);
        addPairTv(v, tv_pitchStartupMotorPosition, 23);
        addPairSpinner(v, sp_pitchMotorDirection, 21);

        addPairTv(v, tv_rollMotorPoles, 26);
        addPairTv(v, tv_rollOffset, 28);
        addPairTv(v, tv_rollStartupMotorPosition, 29);
        addPairSpinner(v, sp_rollMotorDirection, 27);

        addPairTv(v, tv_yawMotorPoles, 32);
        addPairTv(v, tv_yawOffset, 34);
        addPairTv(v, tv_yawStartupMotorPosition, 35);
        addPairSpinner(v, sp_yawMotorDirection, 33);

        
//        updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }



}
