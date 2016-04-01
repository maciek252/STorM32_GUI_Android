package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.optionList;
//import info.androidhive.materialtabs.

public class FourFragment extends IntermediateFragment{
//Fragment{

    public FourFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_four, container, false);
        //addPairTv(v, R.id.textView_4_lowVoltageLimit, optionList.lowVoltageLimitInt);
        //addPairTv(v, R.id.textView_4_Pid, optionList.lowVoltageLimitInt);
        return v;
    }

}
