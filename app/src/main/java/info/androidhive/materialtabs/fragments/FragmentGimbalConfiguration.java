package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class FragmentGimbalConfiguration extends IntermediateFragment{

    TextView tv_deadBand = null;
    TextView tv_rcHysteresis = null;

    TextView tv_rcPitchMin = null;
    TextView tv_rcPitchMax = null;
    TextView tv_rcPitchSpeedLimit = null;
    TextView tv_rcPitchAccelLimit = null;

    Spinner sp_rcPitch = null;
    Spinner sp_rcPitchMode = null;
    Spinner sp_rcRoll = null;
    Spinner sp_rcRollMode = null;

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


//        updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }

}
