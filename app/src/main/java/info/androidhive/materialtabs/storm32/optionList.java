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

    static HashMap<Integer, Option> map_address_Option = new HashMap<>();

    static public void addOption(Option o){
        map_address_Option.put(o.address, o);
    }

    static public void setOptions(byte [] options){
        optionList.options = options;
    }

    static public boolean decodeOptions(){
        return true;
    }

    static public Option getOptionForAddress(int address){
        if( map_address_Option.containsKey(address))
            return map_address_Option.get(address);

        return null;

    }

    static public void populateOptions(){

        if(!map_address_Option.isEmpty())
            return;
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

        /*
          name => 'Pitch Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 3,

         */

        addOption(
        new OptionNumber("Pitch Motor Vmax",
                OptionNumber.NumberType.UnsignedInt,
                5, 0,0,255,150,1,2,3, "")
        );

        addOption(
                new OptionNumber("Roll Motor Vmax",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,255,150,1,2,9, "")
        );

        /*
          name => 'Pan Mode Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 65,
  choices => \@FunctionInputChoicesList,
  column=> 1,
  expert=> 2,

         */
/*
        addOption(
                new OptionListA("Pan Mode Control",
                        0,0,0,
                        5, 0,0,255,150,1,2,9, "")
        );
*/
        /*
          name => 'Pan Mode Default Setting',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 4, default => 0, steps => 1,
  size => 1,
  adr => 66,
  choices => [ 'hold hold pan', 'hold hold hold', 'pan pan pan', 'pan hold hold', 'pan hold pan'],

         */
        String [] a = new String[]{"hold hold pan", "hold hold hold", "pan pan pan", "pan hold hold", "pan hold pan"};
        addOption(
                new OptionListA("Pan Mode Default Setting",
                        0,0,0,4,0,1,66,
                        a
                        )
        );


    }


}

