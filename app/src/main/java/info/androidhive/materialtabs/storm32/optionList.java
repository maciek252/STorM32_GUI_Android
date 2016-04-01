package info.androidhive.materialtabs.storm32;

import java.util.HashMap;

/**
 * Created by maciek on 31.03.16.
 */
public class optionList {

    static public int pitchP;

    static private byte[] options = null;

    static public int voltageCorrection = 0;
    static public Integer voltageCorrectionInt = 0;
    static public Integer lowVoltageLimitInt = 0;

    HashMap<Integer, Option> map_address_Option = new HashMap<>();

    public void addOption(Option o){
        map_address_Option.put(o.address, o);
    }

    static public void setOptions(byte [] options){
        optionList.options = options;
    }

    static public boolean decodeOptions(){
        return true;
    }

    public void populateOptions(){

        /*name => 'Voltage Correction',
  type => 'OPTTYPE_UI', len => 7, ppos => 0, min => 0, max => 200, default => 0, steps => 1,
  size => 2,
  adr => 19,
  unit => '%',
*/
        OptionNumber voltageCorrection = new OptionNumber("Voltage Correction",
                OptionNumber.NumberType.UnsignedInt,
                7, 0,0,200,0,1,2,19, "%");
        addOption(voltageCorrection);


    }


}

