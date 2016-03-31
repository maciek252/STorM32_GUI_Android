package info.androidhive.materialtabs.fragments;



import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.optionList;

/**
 * Created by maciek on 31.03.16.
 */
public class IntermediateFragment extends Fragment implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener{


    public HashMap<TextView,Integer> mappingTextViewsOptions = new HashMap<>();

    public void addPair(View v, int tvId, Integer i){

        TextView tv = (TextView) v.findViewById(tvId);

        // PLUS SET editable and contextClicable!

        tv.setCursorVisible(true);
        tv.setFocusable(true);
        tv.setEnabled(true);
        tv.setClickable(true);
        tv.setFocusableInTouchMode(true);
        tv.setTextIsSelectable(false);
        //tv.setBackgroundColor(Color.blue(3));
    }


    public void inv(){
        //if(tv_voltageCorrection != null) {
//            tv_voltageCorrection.setText("" + optionList.voltageCorrection);
  //      }
        for( TextView v: mappingTextViewsOptions.keySet()){
            v.setText("" + mappingTextViewsOptions.get(v));
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }



    @Override
    public void afterTextChanged(Editable editable) {

        //optionList.voltageCorrection = Integer.parseInt(tv_voltageCorrection.getText().toString());

    }

    public void onFocusChange(View v, boolean b)
    {
        //if(b) {
        //Toast toast = Toast.makeText(getContext(), "33?", Toast.LENGTH_SHORT);
        //toast.setDuration;
        //toast.show();
        //}



        //INSERT CUSTOM CODE HERE
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_3_update:
     //           tv_voltageCorrection.setText("" + optionList.voltageCorrection);
//                break;
        }
    }
}
