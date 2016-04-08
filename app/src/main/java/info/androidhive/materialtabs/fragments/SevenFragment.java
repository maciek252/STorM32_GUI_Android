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


public class SevenFragment extends IntermediateFragment{

    TextView tv_rcHysteresis = null;
    Spinner sp_rcPitch = null;

    public SevenFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_seven, container, false);

        removeControlsFromTables();

         tv_rcHysteresis = (TextView) v.findViewById(R.id.textView_rcInput_rcHysteresis);
        sp_rcPitch = (Spinner) v.findViewById(R.id.spinner_RcInput_rcPitch);

        //spinner_panControlMode = (Spinner) v.findViewById(R.id.spinner_6_panControlMode);
        //addPairSpinner(v, spinner_panModeDefaultSetting, 66);

        //addPair(v, R.id.textView_6_pid, optionList.lowVoltageLimitInt);
        addPairTv(v, tv_rcHysteresis, 105);
        addPairSpinner(v, sp_rcPitch, 44);

//        updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }

}
