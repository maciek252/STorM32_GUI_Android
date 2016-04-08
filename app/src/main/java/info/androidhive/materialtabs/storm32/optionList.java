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

    static protected byte[] options = null;

    static public int voltageCorrection = 0;
    static public Integer voltageCorrectionInt = 0;
    static public Integer lowVoltageLimitInt = 0;

    static HashMap<Integer, Option> map_address_Option = new HashMap<>();

    static public void addOption(Option o){
        map_address_Option.put(o.address, o);
    }


    /*
    static public void setOptions(byte [] options){
        optionList.options = options;
    }
    */

    static public byte[] getOptions(){
        return options;
    }


    static public boolean encodeOptions(){

        for(Integer i: map_address_Option.keySet()){
            Option o = map_address_Option.get(i);
            if( o instanceof OptionNumber){

                //o.value += 1;
                saveToOptions(o.address, 2, o.value);
                Log.i("Storm32", "optionNumber encode val=" + o.value);

            } else if( o instanceof OptionListA){
                //o.value += 1;
                saveToOptions(o.address, 2, o.value);
                Log.i("Storm32", "optionNumber encode val=" + o.value);

            }
        }

        return true;
    }

    static public boolean decodeOptions(){

        for(Integer i: map_address_Option.keySet()){
            Option o = map_address_Option.get(i);
            o.needUpdate = true;
            if( o instanceof OptionNumber){

                //o.value = readFromOptions(o.address, 2);
                o.setValueRead(readFromOptions(o.address, 2));
                Log.i("Storm32", "optionNumber decode addr=" + o.address + " val=" + o.value);
                o.setRead();
            } else if( o instanceof OptionListA){

                //o.value = readFromOptions(o.address, 2);
                o.setValueRead(readFromOptions(o.address, 2));
                Log.i("Storm32", "optionListA decode addr=" + o.address + " val=" + o.value);
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
        
        String [] functionInputChoicesList= {
                "off", "Rc-0", "Rc-1", "Rc-2", "Rc2-0", "Rc2-1", "Rc2-2", "Rc2-3", "Pot-0", "Pot-1", "Pot-2",
                "Virtual-1", "Virtual-2", "Virtual-3", "Virtual-4", "Virtual-5", "Virtual-6", "Virtual-7", "Virtual-8",
                "Virtual-9", "Virtual-10", "Virtual-11", "Virtual-12", "Virtual-13", "Virtual-14", "Virtual-15", "Virtual-16",
                "But switch", "But latch", "But step",
                "Aux-0 switch", "Aux-1 switch", "Aux-2 switch", "Aux-01 switch", "Aux-012 switch",
                "Aux-0 latch", "Aux-1 latch", "Aux-2 latch", "Aux-01 latch", "Aux-012 latch",
                "Aux-0 step", "Aux-1 step", "Aux-2 step"};
        


        if(!map_address_Option.isEmpty())
            return;
        /*name => "Voltage Correction",
  type => "OPTTYPE_UI", len => 7, ppos => 0, min => 0, max => 200, default => 0, steps => 1,
  size => 2,
  adr => 19,
  unit => "%",
*/
        OptionNumber voltageCorrection = new OptionNumber("Voltage Correction",
                OptionNumber.NumberType.UnsignedInt,
                7, 0,0,200,0,1,2,19, "%");
        addOption(voltageCorrection);

        /*
          name => "Pitch Motor Vmax",
  type => "OPTTYPE_UI", len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 3,

         */

        addOption(
        new OptionNumber("150 Pitch Motor Vmax",
                OptionNumber.NumberType.UnsignedInt,
                5, 0,0,255,150,1,2,3, "")
        );

        /*
        },{
  name => "Low Voltage Limit",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 7, default => 1, steps => 1,
  size => 1,
  adr => 18,
  choices => [ "off", "2.9 V/cell", "3.0 V/cell", "3.1 V/cell", "3.2 V/cell", "3.3 V/cell", "3.4 V/cell", "3.5 V/cell" ],
  pos=>[1,4],
},{
         */
        String [] alvl = new String[]{"off", "2.9 V/cell", "3.0 V/cell", "3.1 V/cell", "3.2 V/cell", "3.3 V/cell", "3.4 V/cell", "3.5 V/cell"};
        addOption(
                new OptionListA("18 Imu2 FeedForward LPF",
                        0,0,0,7,1,1,1,18,
                        alvl
                )
        );


        addOption(
                new OptionNumber("9 Roll Motor Vmax",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,255,150,1,2,9, "")
        );
/*
        {
        int length,
                     int ppos,
                     int min, int max, int default
                     int steps,
                     int size, int address,

            name => "Imu2 FeedForward LPF",
                type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 6, default => 1, steps => 1,
                size => 2,
                adr => 99,
                choices => [ "off", "1.5 ms", "4 ms", "10 ms", "22 ms", "46 ms", "94 ms" ],

        },        
  */

        String [] a1 = new String[]{"off", "1.5 ms", "4 ms", "10 ms", "22 ms", "46 ms", "94 ms"};
        addOption(
                new OptionListA("99 Imu2 FeedForward LPF",
                        0,0,0,6,1,1,2,99,
                        a1
                )
        );

/*
    },{
        name => 'Gyro LPF',
                type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 6, default => 1, steps => 1,
                size => 1,
                adr => 100,
                choices => [ 'off', '1.5 ms', '3.0 ms', '4.5 ms', '6.0 ms', '7.5 ms', '9 ms' ],
            pos=>[1,1],
            expert=> 1,
    },{
*/


        addOption(
                new OptionListA("Gyro LPF",
                        0,0,0,6,1,1,1,100,
                        new String[]{"off", "1.5 ms", "3.0 ms", "4.5 ms", "6.0 ms", "7.5 ms", "9 ms" }
                )
        );



        /*
          name => "Pan Mode Control",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 65,
  choices => \@FunctionInputChoicesList,
  column=> 1,
  expert=> 2,

         */

        addOption(
                new OptionListA("65 Pan Mode Control",
                        0,0,0,functionInputChoicesList.length-1,0,1,2,65,
                        functionInputChoicesList
                )
        );

        /*
        {
  name => 'Rc Dead Band',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 50, default => 10, steps => 1,
  size => 2,
  adr => 43,
  unit => 'us',
  expert=> 3,
},
*/
/*
name => 'Rc Pitch',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 44,
  choices => \@FunctionInputChoicesList,
  column => 2,
},
 */

        addOption(
                new OptionListA("44 Rc Pitch",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,44,
                        functionInputChoicesList
                )
        );

/*{
  name => 'Rc Hysteresis',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 50, default => 5, steps => 1,
  size => 2,
  adr => 105,
  unit => 'us',

         */

        addOption(
                new OptionNumber("105 Rc Hysteresis",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,50,5,1,2,105, "us")
        );

/*
        addOption(
                new OptionListA("Pan Mode Control",
                        0,0,0,
                        5, 0,0,255,150,1,2,9, "")
        );
*/
        /*
          name => "Pan Mode Default Setting",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 4, default => 0, steps => 1,
  size => 1,
  adr => 66,
  choices => [ "hold hold pan", "hold hold hold", "pan pan pan", "pan hold hold", "pan hold pan"],

         */
        String [] a = new String[]{"hold hold pan", "hold hold hold", "pan pan pan", "pan hold hold", "pan hold pan"};
        addOption(
                new OptionListA("66 Pan Mode Default Setting",
                        0,0,0,4,0,1,1,66,
                        a
                        )
        );


    }

    static private int decodeByte(byte b){
        int result = 0;
        int b0 = (int) b;
        int b3 = (int)(b & 0xFF);
        return b3;
    }

    static int readFromOptions(int address, int length){

        int result = 0;
        result = decodeByte(options[2*address]);
        result += 256*decodeByte(options[2*address+1]);
        return result;

    }

    static void saveToOptions(int address, int length, int value){


        //result = decodeByte(options[2*address]);
        //result += 256*decodeByte(options[2*address+1]);
        options[2*address] = (byte) (value % 256);
        options[2*address+1] = (byte) (value / 256);


    }

}

