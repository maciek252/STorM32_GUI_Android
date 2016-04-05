package info.androidhive.materialtabs.fragments;



import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.Option;
import info.androidhive.materialtabs.storm32.OptionListA;
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

    final Handler handler_interact=new Handler();//not defined as final variable. may cause problem


    private void updateGUI(){
        handler_interact.post(runnable_interact);

    }
    //creating runnable
    final Runnable runnable_interact = new Runnable() {
        public void run() {



            for(TextView tv: mappingTextViewsOptions.keySet()){

                int addr = mappingTextViewsOptions.get(tv);
                OptionNumber o = (OptionNumber) optionList.getOptionForAddress(addr);

                if(tv.getText().length() == 0 || !isNumeric(tv.getText().toString()))
                    tv.setBackgroundColor(Color.YELLOW);
                else {
                    o.setValue((int)gettNumber(tv.getText().toString()));
                    if(o.getValueRead() != o.getValue())
                        tv.setBackgroundColor(Color.RED);
                    else
                        tv.setBackgroundColor(Color.GREEN);
                }
            }

        }
    };

    private static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private static double gettNumber(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
            return d;
        }
        catch(NumberFormatException nfe)
        {

        }
        return 0;
    }

    public void addPairTv(View v, final TextView tv, int addr){



        // PLUS SET editable and contextClicable!

        tv.setCursorVisible(true);
        tv.setFocusable(true);
        tv.setEnabled(true);
        tv.setClickable(true);
        tv.setFocusableInTouchMode(true);
        tv.setTextIsSelectable(false);
        //tv.setBackgroundColor(Color.blue(3));

        tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
                if (!hasFocus) {
                    int addr = mappingTextViewsOptions.get(tv);
                    Log.i("Storm32", "option changed: TextView addr=" + addr);
                }
            }
        });

//        tv.addTextChangedListener(this);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // TODO: do what you need here (refresh list)
                        // you will probably need to use
                        // runOnUiThread(Runnable action) for some specific
                        // actions
                        updateGUI();
                    }

                }, 2000);

                // TODO Auto-generated method stub
                s.length();
                int addr = mappingTextViewsOptions.get(tv);
                Log.i("Storm32", "option changed: TextView addr=" + addr);

            }
        });

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
                if(on.isRead()) {
                    //v.setBackgroundColor(Color.GREEN);
                    if(on.getValueRead() != on.getValue())
                        v.setBackgroundColor(Color.RED);
                    else
                        v.setBackgroundColor(Color.GREEN);
                }else
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

            OptionListA on = (OptionListA) optionList.getOptionForAddress(addr);

            if(on != null) {
                if(on.isRead())
                    sp.setBackgroundColor(Color.GREEN);
                else
                    sp.setBackgroundColor(Color.GRAY);

                //v.setTextColor(Color.parseColor("#bdbdbd"));

                //v.setText("" + on.getValue());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(  getContext(),
                        android.R.layout.simple_spinner_item, on.choices);
                sp.setAdapter(adapter);

            } else
                sp.setBackgroundColor(Color.CYAN);

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

        if(editable instanceof  TextView){
            TextView tv = (TextView) editable;
            int addr = mappingTextViewsOptions.get(tv);
            Log.i("Storm32", "option changed: TextView addr=" + addr);
        }


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
