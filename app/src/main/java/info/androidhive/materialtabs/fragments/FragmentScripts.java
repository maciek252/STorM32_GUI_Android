package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Arrays;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.storm32.optionList;
import utils.Utils;


public class FragmentScripts extends IntermediateFragment
implements         View.OnClickListener
{

    TextView tv_deadBand = null;
    TextView tv_rcHysteresis = null;

    TextView tv_rcPitchMin = null;
    TextView tv_rcPitchMax = null;
    TextView tv_rcPitchSpeedLimit = null;
    TextView tv_rcPitchAccelLimit = null;

    EditText editTextScript1 = null;
    EditText editTextScript2 = null;
    EditText editTextScript3 = null;

    Spinner sp_rcPitch = null;
    Spinner sp_rcPitchMode = null;
    Spinner sp_rcRoll = null;
    Spinner sp_rcRollMode = null;

    Button compile1 = null;
    Button compile2 = null;
    Button compile3 = null;

    Spinner sp_script1 = null;
    Spinner sp_script2 = null;
    Spinner sp_script3 = null;
    Spinner sp_script4 = null;

    TabHost tabHost = null;

    public FragmentScripts() {
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
        View v =  inflater.inflate(R.layout.fragment_scripts, container, false);

        removeControlsFromTables();

        sp_script1 = (Spinner) v.findViewById(R.id.spinner_scripts_script1);
        sp_script2 = (Spinner) v.findViewById(R.id.spinner_scripts_script2);
        sp_script3 = (Spinner) v.findViewById(R.id.spinner_scripts_script3);

        editTextScript1 = (EditText)v.findViewById(R.id.script1_editText);
        editTextScript2 = (EditText)v.findViewById(R.id.script2_editText);
        editTextScript3 = (EditText)v.findViewById(R.id.script3_editText);

        addPairSpinner(v, sp_script1, 119);
        addPairSpinner(v, sp_script2, 120);
        addPairSpinner(v, sp_script3, 121);

//        updateGUI();


        TabHost host = (TabHost)v.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Three");
        host.addTab(spec);

        compile1 = (Button) v.findViewById(R.id.script_compile1);
        compile2 = (Button) v.findViewById(R.id.scripts_compile2);
        compile3 = (Button) v.findViewById(R.id.scripts_compile3);
        compile1.setOnClickListener(this);
        compile2.setOnClickListener(this);
        compile3.setOnClickListener(this);



        updateAllControlsAccordingToOptionList();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.script_compile1:
                editTextScript1.setText("bibi");
                break;
            case R.id.scripts_compile2:
                editTextScript2.setText("222");
                break;
            case R.id.scripts_compile3:
                String s = "";
                byte[] scripts = optionList.giveScriptsArray();
                // 6 - naglowek
                editTextScript3.setText("222_" + scripts.length + "_" + Utils.byteArrayToHex(scripts));


                scripts = Arrays.copyOfRange(scripts, 6, scripts.length);
                int scriptNr = 0;
                while(true) {
                     int i = splitScriptCode(scripts);


                    if(i != -1 && scriptNr <= 3){

                        byte[] skrypt = Arrays.copyOfRange(scripts, 0, i);
                        scripts = Arrays.copyOfRange(scripts, i+1, scripts.length);
                        editTextScript3.setText(editTextScript3.getText() + "scriptnr=" + scriptNr + " " + Utils.byteArrayToHex(skrypt) + "-------\n");
                        editTextScript3.setText(editTextScript3.getText() + "DECODE:" + scriptNr + decodeScriptArray(skrypt) + "========\n");
                        scriptNr++;
                    } else {
                        break;
                    }
                }


        }
    }



    int  splitScriptCode(byte [] a){
        byte [] pattern = new byte [1];
        pattern[0] = (byte)0xff;
        int i = Utils.indexOfTwoArrays(a, 0, a.length, pattern);
        return i;
    }

    String decodeScriptArray(byte [] s){
        String result = "";

        int pc = 0;
        while(pc < s.length) {
            if (s[pc] == 0x03) {
                result += "CASE#DEFAULT\n";
                pc += 3;
            } else if (s[pc] == 0x16) {
                result += "DOCAMERA\n";
                pc += 3;
            } else if (s[pc] == 0x09) {
                pc++;
                int nr = (int) Utils.getOneByteNumberFromByteArray(s, pc);
                result += "WAIT " + nr + "\n";
                pc += 1;
            }
            pc++;
        }

        return result;
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
