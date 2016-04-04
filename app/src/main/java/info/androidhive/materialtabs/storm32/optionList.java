package info.androidhive.materialtabs.storm32;

import android.util.Log;
import android.widget.Toast;

import org.mavlink.MAVLinkCRC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
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

        for(Integer i: map_address_Option.keySet()){
            Option o = map_address_Option.get(i);
            if( o instanceof OptionNumber){
                Log.i("Storm32", "optionNumber");
                o.value = readFromOptions(o.address, 2);
                o.setRead();
            } else if( o instanceof OptionListA){
                Log.i("Storm32", "optionListA");
                o.value = readFromOptions(o.address, 2);
                o.setRead();
            }
        }

        return true;
    }

    static public void resetTempBuffer(){
        options = null;

    }

    static public void addToTempBuffer(String message) {

        if (options == null)
            options = MAVLinkCRC.stringToByte(message);
        else {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(options);
                outputStream.write(MAVLinkCRC.stringToByte(message));
            } catch (IOException e) {
                e.printStackTrace();
            }

            options = outputStream.toByteArray();


        }
    }

    static public void addToTempBuffer(byte [] o, int num) {


            //byte [] oo = System.arraycopy(o, 0, num);
            byte [] oo = Arrays.copyOfRange(o, 0, num);

        if( options == null)
            options = oo;

        else {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(options);
                outputStream.write(oo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            options = outputStream.toByteArray();
            Log.d("optionList", "message buffer " + writeStringAsHex(options));
        }
    }


    static public boolean checkMessage(){

        if(options.length >= 381 && options[380] == 'o'){
            Log.d("optionList", "options read OK");
        } else
            return false;

        if(true){
            byte [] subArray = Arrays.copyOfRange(options, 0, 377);

            int crc = MAVLinkCRC.crc_calculate(subArray);
            int crc2 = MAVLinkCRC.hexVaxToInt(options[378], options[379]);
            if(crc2 != crc){

                //Toast toast = Toast.makeText(, "options received but bad CRC!", Toast.LENGTH_SHORT);
                //toast.setDuration;
                //toast.show();
                Log.d("optionList", "RCV CRC OK!");
                return true;
            }
        }
        return true;




    }

    static public Option getOptionForAddress(int address){
        if( map_address_Option.containsKey(address))
            return map_address_Option.get(address);

        return null;

    }

    static public String writeStringAsHex(byte [] b){
        String result = "";
        for(byte bb : b)
            result += String.format("%01X", bb) + " ";
        return result;
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

    static int readFromOptions(int address, int length){

        int result = 0;
        result = options[2*address];
        result += 16*options[2*address+1];
        return result;

    }

}

