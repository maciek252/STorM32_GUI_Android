package info.androidhive.materialtabs.fragments;



import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import info.androidhive.materialtabs.R;
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

    public void removeControlsFromTables(){
        for(TextView tv: mappingTextViewsOptions.keySet()){
            mappingTextViewsOptionsAll.remove(tv);
        }
        mappingTextViewsOptions.clear();
        for(Spinner sp: mappingSpinnerssOptions.keySet()){
            mappingSpinnersOptionsAll.remove(sp);
        }
        mappingSpinnerssOptions.clear();
    }

    protected void updateGUI(){
        handler_interact.post(runnable_interact);
        //updateMy();
    }
    //creating runnable
    final Runnable runnable_interact = new Runnable() {
        public void run() {
            updateMy();


        }
    };

    /*
    does not read option values from optionList
    sets vals in optionList
     */
    protected void updateMy(){

        if(getView() == null)
            return;
        if(!getView().isShown())
            return;

        Log.i("Storm32", "IntermFrag: updateMy");

        for(TextView tv: mappingTextViewsOptions.keySet()){

            int addr = mappingTextViewsOptions.get(tv);
            OptionNumber o = (OptionNumber) optionList.getOptionForAddress(addr);
            if(o.needUpdate){
                Log.i("Storm32", "IF o needed update");
                o.needUpdate = false;
                tv.setText("" + doubleToString(o.convertToWithDot(o.getValueRead()), o.getppos()));
                tv.setBackgroundColor(Color.GREEN);
            } else if(!o.isRead())
                tv.setBackgroundColor(Color.GRAY);
            else {
                if (tv.getText().length() == 0 || !isNumeric(tv.getText().toString()))
                    tv.setBackgroundColor(Color.YELLOW);
                else {


                    double v = gettNumber(tv.getText().toString());


                    o.setValueFromWithouDot(o.convertToWithoutDot(v));
                    Log.e("IntermediateFrag", "setting value of addr" + addr + " to " + v);
                    if (o.getValueWithoutDot() < o.getMin()) {
                        o.setValueFromWithouDot(o.getMin());
                        tv.setText("" + doubleToString(o.convertToWithDot(o.getValueWithoutDot()), o.getppos()));
                        tv.setBackgroundColor(Color.YELLOW);
                    } else if (o.getValueWithoutDot() > o.getMax()) {
                        o.setValueFromWithouDot(o.getMax());
                        tv.setText("" + doubleToString(o.convertToWithDot(o.getValueWithoutDot()), o.getppos()));
                        tv.setBackgroundColor(Color.YELLOW);
                    } else if (o.getValueRead() != o.getValue())
                        tv.setBackgroundColor(Color.RED);
                    else
                        tv.setBackgroundColor(Color.GREEN);
                }
            }
        }


        for(Spinner sp: mappingSpinnerssOptions.keySet()) {

            int addr = mappingSpinnerssOptions.get(sp);

            OptionListA o = (OptionListA) optionList.getOptionForAddress(addr);
            o.setValueFromWithouDot((int)sp.getSelectedItemId());

            if(o.needUpdate){
                Log.i("Storm32", "IF o needed update");
                o.needUpdate = false;
                sp.setSelection((int)o.getValueRead());

                sp.setBackgroundColor(Color.GREEN);
            } else
            if(!o.isRead())
                sp.setBackgroundColor(Color.GRAY);
            else {
                if (sp.getSelectedItemId() != o.getValueRead()) {
                    sp.setBackgroundColor(Color.RED);
                    Log.e("IF updateMy", "spinner addr" + addr + "R");
                }else {
                    sp.setBackgroundColor(Color.GREEN);
                    Log.e("IF updateMy", "spinner addr" + addr + "G");
                }
            }



        }
    }



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

    private static String doubleToString(double d, int numOfDigitsAfterDot){


        String f = "%1$,."+numOfDigitsAfterDot+"f";
        String result = String.format(Locale.US, f, d).replace(',', '.');
        Log.i("IF", "converting" + d + " for numofDigits" + numOfDigitsAfterDot + " result=" + result);
        return result;
    }

    private static double gettNumber(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
            Log.i("IF", "converting" + str + " result=" + d);
            return d;
        }
        catch(NumberFormatException nfe)
        {

        }
        return 0;
    }

    public void addPairTv(View v, final TextView tv, int addr){

        Log.i("Storm32", "addPairTv");

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

                }, 4000);

                // TODO Auto-generated method stub
                s.length();
                int addr = mappingTextViewsOptions.get(tv);
                Log.i("Storm32", "option changed: TextView addr=" + addr);

            }
        });


        //if(mappingTextViewsOptions.containsKey(tv))

        //    mappingTextViewsOptions.remove(tv);
        //if(mappingTextViewsOptions.values().contains(addr))
            mappingTextViewsOptions.put(tv,addr);
        //if(mappingTextViewsOptionsAll.containsKey(tv))
            //IntermediateFragment.mappingTextViewsOptionsAll.remove(tv);
        IntermediateFragment.mappingTextViewsOptionsAll.put(tv,addr);

    }

    public void addPairSpinner(View v, final Spinner sp, int addr){


        // PLUS SET editable and contextClicable!

        sp.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
                if (!hasFocus) {
                    int addr = mappingSpinnerssOptions.get(sp);
                    Log.i("Storm32", "option changed: TextView addr=" + addr);
                }
            }
        });

//        tv.addTextChangedListener(this);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                         @Override
                                         public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                                    int arg2, long arg3) {
                                             // TODO Auto-generated method stub
                                             int addr = mappingSpinnerssOptions.get(sp);
                                             OptionListA oa = (OptionListA) optionList.getOptionForAddress(addr);


                                             Object item = arg0.getItemAtPosition(arg2);
                                             /*
                                             if (item != null) {
                                                 Toast.makeText(getContext(), item.toString(),
                                                         Toast.LENGTH_SHORT).show();
                                             }
                                             Toast.makeText(getContext(), "Selected:" + sp.getSelectedItemId(),
                                                     Toast.LENGTH_SHORT).show();
                                            */
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

                                             }, 100);

                                             oa.setValueFromWithouDot((int)sp.getSelectedItemId());
                                             // TODO Auto-generated method stub


                                             Log.i("Storm32", "option changed: TextView addr=" + addr);


                                         }

                                         @Override
                                         public void onNothingSelected(AdapterView<?> arg0) {
                                             // TODO Auto-generated method stub

                                         }

                                     });



        //if(mappingTextViewsOptions.containsKey(tv))

        //    mappingTextViewsOptions.remove(tv);
        //if(mappingTextViewsOptions.values().contains(addr))
        mappingSpinnerssOptions.put(sp,addr);
        //if(mappingTextViewsOptionsAll.containsKey(tv))
        //IntermediateFragment.mappingTextViewsOptionsAll.remove(tv);
        IntermediateFragment.mappingSpinnersOptionsAll.put(sp,addr);

        ///-----------------------------------------------
        //mappingSpinnerssOptions.put(sp,addr);
        //IntermediateFragment.mappingSpinnersOptionsAll.put(sp,addr);

    }

    public void updateAll(){

    }

    // on the basis of optionList

    /*
    protected void updateGui_ReadFromOptionList_RunFromOutside(){
        handler_interact.post(runnable_interact_2);
        //updateMy();
    }
    //creating runnable
    final Runnable runnable_interact_2 = new Runnable() {
        public void run() {
            //updateAllControlsAccordingToOptionList();


        }
    };
*/


    public void updateAllControlsAccordingToOptionList(){

        Log.i("Storm32", "IntermFrag: updateAllControlsAccordingToOptionList" +
                "");
        for(TextView v: mappingTextViewsOptions.keySet()){

            int addr = mappingTextViewsOptions.get(v);

            OptionNumber on = (OptionNumber) optionList.getOptionForAddress(addr);

            if(on != null) {
                if(on.isRead()) {

                    //v.setBackgroundColor(Color.GREEN);
                    if(on.getValueRead() != on.getValueWithoutDot())
                        v.setBackgroundColor(Color.RED);
                    else
                        v.setBackgroundColor(Color.GREEN);
                }else
                    v.setBackgroundColor(Color.GRAY);

                v.setTextColor(Color.parseColor("#bdbdbd"));

                v.setText("" + doubleToString(on.convertToWithDot(on.getValueWithoutDot()), on.getppos()));
            } else
                v.setBackgroundColor(Color.CYAN);
        }

        for(Spinner sp: mappingSpinnerssOptions.keySet()) {
            if(!mappingSpinnerssOptions.containsKey(sp))
                return;
            int addr = mappingSpinnerssOptions.get(sp);
            Log.i("Storm32", "getting listA for addr=" + addr);
            OptionListA on = (OptionListA) optionList.getOptionForAddress(addr);


            if(on != null) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(  getContext(),
                        android.R.layout.simple_spinner_item, on.choices);
                sp.setAdapter(adapter);

                if(on.isRead()) {

                    sp.setSelection((int)on.getValueWithoutDot());

                    if(sp.getSelectedItemId() == on.getValueRead()) {
                        sp.setBackgroundColor(Color.GREEN);
                        Log.e("IF updateAllContr", "spinner addr" + addr + "G");
                    }else {
                        sp.setBackgroundColor(Color.RED);
                        Log.e("IF updateAllContr", "spinner addr" + addr + "R");
                    }

                }else {
                    sp.setBackgroundColor(Color.GRAY);
                    sp.setSelection((int)on.defaultValue);
                }

                Log.e("IntermediateFrag", "setting value of (spinner) addr" + addr + " to " +  on.getValueWithoutDot());
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
/*
    public void inv(){
        //if(tv_voltageCorrection != null) {
//            tv_voltageCorrection.setText("" + optionList.voltageCorrection);
  //      }
        for( TextView v: mappingTextViewsOptions.keySet()){
            v.setText("" + mappingTextViewsOptions.get(v));
        }

    }
*/
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
