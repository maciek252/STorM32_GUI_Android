package info.androidhive.materialtabs.fragments;



import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.Option;
import info.androidhive.materialtabs.storm32.OptionNumber;
import info.androidhive.materialtabs.storm32.optionList;

/**
 * Created by maciek on 31.03.16.
 */
public class IntermediateFragment extends Fragment implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener{

    public IntermediateFragment(){
        optionList.populateOptions();
    }

    static public HashMap<TextView,Integer> mappingTextViewsOptionsAll = new HashMap<>();
    public HashMap<TextView,Integer> mappingTextViewsOptions = new HashMap<>();

    static public HashMap<Spinner,Integer> mappingSpinnersOptionsAll = new HashMap<>();
    public HashMap<Spinner,Integer> mappingSpinnerssOptions = new HashMap<>();



    public void addPairTv(View v, TextView tv, int addr){



        // PLUS SET editable and contextClicable!

        tv.setCursorVisible(true);
        tv.setFocusable(true);
        tv.setEnabled(true);
        tv.setClickable(true);
        tv.setFocusableInTouchMode(true);
        tv.setTextIsSelectable(false);
        //tv.setBackgroundColor(Color.blue(3));

        mappingTextViewsOptions.put(tv,addr);
        IntermediateFragment.mappingTextViewsOptionsAll.put(tv,addr);

    }

    public void addPairSpinner(View v, Spinner sp, int addr){



        // PLUS SET editable and contextClicable!

        /*
        tv.setCursorVisible(true);
        tv.setFocusable(true);
        tv.setEnabled(true);
        tv.setClickable(true);
        tv.setFocusableInTouchMode(true);
        tv.setTextIsSelectable(false);
        //tv.setBackgroundColor(Color.blue(3));
*/
        //map_addr_control.put(addr, tv);

        mappingSpinnerssOptions.put(sp,addr);
        IntermediateFragment.mappingSpinnersOptionsAll.put(sp,addr);

    }

    public void updateAll(){

    }

    public void updateAllControls(){

        for(TextView v: mappingTextViewsOptions.keySet()){
            if(!mappingTextViewsOptions.containsKey(v))
                return;
            int addr = mappingTextViewsOptions.get(v);

            OptionNumber on = (OptionNumber) optionList.getOptionForAddress(addr);

            if(on != null) {
                if(on.isRead())
                    v.setBackgroundColor(Color.GREEN);
                else
                    v.setBackgroundColor(Color.GRAY);

                v.setTextColor(Color.parseColor("#bdbdbd"));

                v.setText("" + on.getValue());
            } else
                v.setBackgroundColor(Color.CYAN);
        }

        for(Spinner sp: mappingSpinnerssOptions.keySet()) {
            if(!mappingSpinnerssOptions.containsKey(sp))
                return;
            int addr = mappingSpinnerssOptions.get(sp);
        }

    }

    static public void setColorToAllControls(){

        for(TextView v: mappingTextViewsOptionsAll.keySet()){
            v.setBackgroundColor(Color.CYAN);
            v.setTextColor(Color.parseColor("#bdbdbd"));
        }

        for(Spinner sp: mappingSpinnersOptionsAll.keySet()){
           //sp.setBackgroundColor(Color.CYAN);
            sp.setBackgroundColor(Color.parseColor("#bdbdbd"));
            //sp.setC(Color.parseColor("#bdbdbd"));
        }

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
