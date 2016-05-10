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


        /*
        },{
  name => 'Roll Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 9,

         */

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
  name => 'Rc Pitch Min',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => -250, steps => 5,
  size => 2,
  adr => 47,
  unit => '�',
}*/
        addOption(
                new OptionNumber("Rc Pitch Min",
                        OptionNumber.NumberType.SignedInt,
                        5, 1,-1200,1200,-250,5,2,47, "deg")
        );

/*,{
  name => 'Rc Pitch Max',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 250, steps => 5,
  size => 2,
  adr => 48,
  unit => '�',
}*/
        addOption(
                new OptionNumber("Rc Pitch Max",
                        OptionNumber.NumberType.SignedInt,
                        5, 1,-1200,1200,250,5,2,48, "deg")
        );




        /*{
  name => 'Rc Pitch Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 49,
  unit => '�/s',
}*/
        addOption(
                new OptionNumber("Rc Pitch Speed Limit",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,0,1000,400,5,2,49, "deg/s")
        );


        /*{
  name => 'Rc Pitch Accel Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 50,
}*/
        addOption(
                new OptionNumber("Rc Pitch Accel Limit",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 3,0,1000,300,10,2,50, "")
        );





        /*
        name => 'Rc Roll',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 51,
  choices => \@FunctionInputChoicesList,
  column => 3,
},
*/
        addOption(
                new OptionListA("Rc Roll",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,51,
                        functionInputChoicesList
                )
        );


        /*
{
  name => 'Rc Roll Mode',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 52,
  choices => [ 'absolute', 'relative', 'absolute centered'],
},
         */

        addOption(
                new OptionListA("Rc Roll Mode",
                        0,0,0,2,0,1,1,52,
                        new String[]{"absolute", "relative", "absolute centered"}
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
        addOption(
                new OptionNumber("Rc Dead Band",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,0,50,10,1,2,43, "us")
        );



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
/*
{  name => 'Rc Pitch Mode',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 45,
  choices => [ 'absolute', 'relative', 'absolute centered'],
}
 */

        addOption(
                new OptionListA("Rc Pitch Mode",
                        0,0,0,2,0,1,1,45,
                        new String[] {"absolute", "relative", "absolute centered"}
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

        /*
        ##--- FUNCTIONS tab --------------------
},{
  name => 'Standby',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 70,
  choices => \@FunctionInputChoicesList,
  expert=> 4,
  column=> 1,
},
*/

        addOption(
                new OptionListA("Standby",
                        0,0,0,functionInputChoicesList.length,0,1,1,70,
                        functionInputChoicesList
                )
        );

        /*
{

  name => 'Re-center Camera',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 76,
  choices => \@FunctionInputChoicesList,
  pos=>[1,3],

}*/
        addOption(
                new OptionListA("Re-center Camera",
                        0,0,0,functionInputChoicesList.length,0,1,1,76,
                        functionInputChoicesList
                )
        );

        /*{
  name => 'IR Camera Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 71,
  choices => \@FunctionInputChoicesList,
  column=> 2, #3,
}*/

        addOption(
                new OptionListA("IR Camera Control",
                        0,0,0,functionInputChoicesList.length,0,1,1,71,
                        functionInputChoicesList
                )
        );

        /*{
  name => 'Camera Model',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 0, steps => 1,
  size => 1,
  adr => 72,
  choices => [ 'Sony Nex', 'Canon', 'Panasonic', 'Nikon' ],
},*/
        String [] cameras = new String[] {"Sony Nex", "Canon", "Panasonic", "Nikon"};
        addOption(
                new OptionListA("Standby",
                        0,0,0,cameras.length,0,1,1,72,
cameras
                )
        );

        /*{
  name => 'IR Camera Setting #1',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 73,
  choices => [ 'shutter', 'shutter delay', 'video on/off' ],
}*/

        String [] irCamSetting1 = new String[] {"shutter", "shutter delay", "video on/off"};
        addOption(
                new OptionListA("IR Camera Setting #1",
                        0,0,0,irCamSetting1.length,0,1,1,73,
                        irCamSetting1
                )
        );
        
        
/*        {
  name => 'IR Camera Setting #2',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 3, default => 2, steps => 1,
  size => 1,
  adr => 74,
  choices => [ 'shutter', 'shutter delay', 'video on/off', 'off' ],
}*/

        String [] irCamSetting2 = new String[] {"shutter", "shutter delay", "video on/off", "off"};
        addOption(
                new OptionListA("IR Camera Setting #2",
                        0,0,0,irCamSetting2.length,0,1,1,74,
                        irCamSetting2
                )
        );


        /*
        {
  name => 'Imu AHRS',
  type => 'OPTTYPE_UI', len => 5, ppos => 2, min => 0, max => 2500, default => 1000, steps => 100,
  size => 2,
  adr => 81,
  unit => 's',
         */

        addOption(
                new OptionNumber("Imu AHRS",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,0,2500,1000,100,2,81, "s")
        );

/*
name => 'Rc Pitch Offset',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 106,
  unit => '°',
  pos=> [2,4],
 */

        /*
        addOption(
                new OptionNumber("Rc Pitch Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,-1200,1200,0,5,2,106, "°")
        );
        */

        /*name => 'Imu2 Configuration',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 4, default => 0, steps => 1,
  size => 1,
  adr => 94,
  choices => [ 'off', 'full', 'full xy', 'full v1', 'full v1 xy' ],
  expert=> 5,*/


        String [] imu2configuration = new String[] { "off", "full", "full xy", "full v1", "full v1 xy" };
        addOption(
                new OptionListA("Imu2 Configuration",
                        0,0,0,imu2configuration.length,0,1,1,94,
                        imu2configuration
                )
        );

        /*{
  name => 'Time Interval (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 150, default => 0, steps => 1,
  size => 2,
  adr => 75,
  unit => 's',

}*/

        addOption(
                new OptionNumber("Time Interval (0 = off)",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,0,150,0,1,2,75, "s")
        );
        
        /*{
  name => 'Pwm Out Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 113,
  choices => \@FunctionInputChoicesList,
  column=> 3,
}*/

        addOption(
                new OptionListA("Pwm Out Control",
                        0,0,0,functionInputChoicesList.length,0,1,1,113,
                        functionInputChoicesList
                )
        );

        
        /*{
  name => 'Pwm Out Mid',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 900, max => 2100, default => 1500, steps => 1,
  size => 2,
  adr => 114,
  unit => 'us',
}*/
        addOption(
                new OptionNumber("Pwm Out Mid",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,900,2100,1500,10,2,114, "us")
        );


        /*{
  name => 'Pwm Out Min',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 900, max => 2100, default => 1100, steps => 10,
  size => 2,
  adr => 115,
  unit => 'us',
}*/

        addOption(
                new OptionNumber("Pwm Out Min",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,900,2100,1100,10,2,115, "us")
        );

        /*{
  name => 'Pwm Out Max',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 900, max => 2100, default => 1900, steps => 10,
  size => 2,
  adr => 116,
  unit => 'us',
}*/
        addOption(
                new OptionNumber("Pwm Out Mid",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,900,2100,1900,10,2,116, "us")
        );

        /*{
  name => 'Pwm Out Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 0, min => 0, max => 1000, default => 0, steps => 5,
  size => 2,
  adr => 117,
  unit => 'us/s',
         */

        addOption(
                new OptionNumber("Pwm Out Speed Limit (0 = off)",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,0,1000,0,5,2,117, "us/s")
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

