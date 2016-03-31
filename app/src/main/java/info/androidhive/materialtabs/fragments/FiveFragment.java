package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.optionList;


public class FiveFragment extends IntermediateFragment{

    public FiveFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_five, container, false);
        addPair(v, R.id.textView_5_pid, optionList.lowVoltageLimitInt);
        addPair(v, R.id.textView_5_pid2, optionList.lowVoltageLimitInt);
        addPair(v, R.id.textView_5_pid3, optionList.lowVoltageLimitInt);
        return v;
    }

}
