package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.materialtabs.R;

import info.androidhive.materialtabs.storm32.optionList;

public class ThreeFragment extends Fragment implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener{




    TextView tv_voltageCorrection = null;
    Button buttonUpdate = null;


    public ThreeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_three, container, false);

        tv_voltageCorrection = (TextView) v.findViewById(R.id.textView_3_voltageCorrection);
        tv_voltageCorrection.addTextChangedListener(this);

        buttonUpdate = (Button) v.findViewById(R.id.button_3_update);
        buttonUpdate.setOnClickListener(this);


        return v;
    }

    /*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
        //else {  }
    }*/
    //@Override
    //public void on

    @Override
    public void onHiddenChanged (boolean hidden){
        Toast toast = Toast.makeText(getContext(), "3?", Toast.LENGTH_SHORT);
        //toast.setDuration;
        toast.show();
    }

    @Override
    //public void onHiddenChanged (boolean hidden){
    public void onResume(){

        super.onResume();

        Toast toast = Toast.makeText(getContext(), "3!", Toast.LENGTH_SHORT);
        //toast.setDuration;
        toast.show();

        if (tv_voltageCorrection != null) {
            tv_voltageCorrection.setText("" + optionList.voltageCorrection);
        }
    }

    public void onFocusChange(View v, boolean b)
    {
        //if(b) {
        Toast toast = Toast.makeText(getContext(), "33?", Toast.LENGTH_SHORT);
        //toast.setDuration;
        toast.show();
        //}



        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }



    @Override
    public void afterTextChanged(Editable editable) {

        optionList.voltageCorrection = Integer.parseInt(tv_voltageCorrection.getText().toString());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_3_update:
                tv_voltageCorrection.setText("" + optionList.voltageCorrection);
                break;
        }
    }


    public void inv(){
        if(tv_voltageCorrection != null) {
            tv_voltageCorrection.setText("" + optionList.voltageCorrection);
        }

    }

        }
