package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.optionList;


public class FiveFragment extends IntermediateFragment{

    public FiveFragment() {
        // Required empty public constructor
    }

    TextView tv_PitchMotorVmax = null;
    TextView tv_RollMotorVmax = null;
    Spinner sp_GyroLpf = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_five, container, false);
        tv_PitchMotorVmax = (TextView) v.findViewById(R.id.textView_5_pitchMotorVmax);
        tv_RollMotorVmax = (TextView) v.findViewById(R.id.textView_5_rollMotorVmax);
        addPairTv(v, tv_PitchMotorVmax, 3);
        addPairTv(v, tv_RollMotorVmax, 9);

        sp_GyroLpf = (Spinner) v.findViewById((R.id.spinner_5_gyroLpf));

        String [] opcje = new String[] {
                "1", "2", "3", "4", "5"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(  getContext(),
                android.R.layout.simple_spinner_item, opcje);
        sp_GyroLpf.setAdapter(adapter);


        addPairSpinner(v, sp_GyroLpf, 3);

        updateAllControls();
        //IntermediateFragment.setColorToAllControls();
        return v;
    }

}
