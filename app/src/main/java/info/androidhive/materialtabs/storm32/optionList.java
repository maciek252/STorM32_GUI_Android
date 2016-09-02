package info.androidhive.materialtabs.storm32;

import android.content.res.Configuration;
import android.util.Log;

import org.mavlink.MAVLinkCRC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

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
                saveToOptions(o, 2, o.value);
                Log.i("Storm32", "optionNumber encode val=" + o.value);

            } else if( o instanceof OptionListA){
                //o.value += 1;
                saveToOptions(o, 2, o.value);
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
                o.setValueRead(readOptionFromOptions(o, 2));
                Log.i("Storm32", "optionNumber decode addr=" + o.address + " val=" + o.value);
                o.setRead();
            } else if( o instanceof OptionListA){

                //o.value = readFromOptions(o.address, 2);
                o.setValueRead(readOptionFromOptions(o, 2));
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
        ,{
  name => 'Yaw Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 15,

         */

        addOption(
                new OptionNumber("Yaw Motor Vmax",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,255,150,1,2,15, "")
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
  name => "Roll Motor Vmax",
  type => "OPTTYPE_UI", len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
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
        name => "Gyro LPF",
                type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 6, default => 1, steps => 1,
                size => 1,
                adr => 100,
                choices => [ "off", "1.5 ms", "3.0 ms", "4.5 ms", "6.0 ms", "7.5 ms", "9 ms" ],
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
  name => "Rc Pitch Min",
  type => "OPTTYPE_SI", len => 0, ppos => 1, min => -1200, max => 1200, default => -250, steps => 5,
  size => 2,
  adr => 47,
  unit => "�",
}*/
        addOption(
                new OptionNumber("Rc Pitch Min",
                        OptionNumber.NumberType.SignedInt,
                        5, 1,-1200,1200,-250,5,2,47, "deg")
        );


/*,{
  name => "Rc Pitch Max",
  type => "OPTTYPE_SI", len => 0, ppos => 1, min => -1200, max => 1200, default => 250, steps => 5,
  size => 2,
  adr => 48,
  unit => "�",
}*/
        addOption(
                new OptionNumber("Rc Pitch Max",
                        OptionNumber.NumberType.SignedInt,
                        5, 1,-1200,1200,250,5,2,48, "deg")
        );




        /*{
  name => "Rc Pitch Speed Limit (0 = off)",
  type => "OPTTYPE_UI", len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 49,
  unit => "�/s",
}*/
        addOption(
                new OptionNumber("Rc Pitch Speed Limit",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,0,1000,400,5,2,49, "deg/s")
        );


        /*{
  name => "Rc Pitch Accel Limit (0 = off)",
  type => "OPTTYPE_UI", len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 50,
}*/
        addOption(
                new OptionNumber("Rc Pitch Accel Limit",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 3,0,1000,300,10,2,50, "")
        );





        /*
        name => "Rc Roll",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
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
        name => 'Acc Compensation Method',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 1, default => 1, steps => 1,
  size => 1,
  adr => 88,
  choices => [ 'standard', 'advanced'],
  pos=> [1,4],
},
         */

        String [] accCompensationMethods = {"standard", "advanced"};

        addOption(
                new OptionListA("Acc Compensation Method",
                        0,0,0,accCompensationMethods.length-1,0,1,1,88,
                        accCompensationMethods
                )
        );



        /*
{
  name => "Rc Roll Mode",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 52,
  choices => [ "absolute", "relative", "absolute centered"],
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
  name => "Rc Roll Min",
  type => "OPTTYPE_SI", len => 0, ppos => 1, min => -450, max => 450, default => -250, steps => 5,
  size => 2,
  adr => 54,
  unit => "°",
         */

        addOption(
                new OptionNumber("Rc Roll Min",
                        OptionNumber.NumberType.SignedInt,
                        0, 1,-450,450,-250,5,2,54, "deg")
        );

        /*
          name => "Rc Roll Max",
  type => "OPTTYPE_SI", len => 0, ppos => 1, min => -450, max => 450, default => 250, steps => 5,
  size => 2,
  adr => 55,
  unit => "°",
         */

        addOption(
                new OptionNumber("Rc Roll Max",
                        OptionNumber.NumberType.SignedInt,
                        0, 1,-450,450,250,5,2,55, "deg")
        );

/*
},{
  name => "Rc Roll Speed Limit (0 = off)",
  type => "OPTTYPE_UI", len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 56,
  unit => "°/s",
 */
        addOption(
                new OptionNumber("Rc Roll Speed Limit",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,0,1000,400,5,2,56, "deg/s")
        );

        /*
        ,{
  name => "Rc Roll Accel Limit (0 = off)",
  type => "OPTTYPE_UI", len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 57,

},{
         */


        addOption(
                new OptionNumber("Rc Roll Accel Limit (0=off)",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 3,0,1000,300,10,2,57, "")
        );

/*
},{
  name => 'Rc Yaw Min',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -2700, max => 2700, default => -250, steps => 10,
  size => 2,
  adr => 61,
  unit => '°',
},{
  name => 'Rc Yaw Max',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -2700, max => 2700, default => 250, steps => 10,
  size => 2,
  adr => 62,
  unit => '°',
},{
  name => 'Rc Yaw Speed Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 1, min => 0, max => 1000, default => 400, steps => 5,
  size => 2,
  adr => 63,
  unit => '°/s',
},{
  name => 'Rc Yaw Accel Limit (0 = off)',
  type => 'OPTTYPE_UI', len => 0, ppos => 3, min => 0, max => 1000, default => 300, steps => 10,
  size => 2,
  adr => 64,


 */

        addOption(
                new OptionNumber("Rc Yaw Min",
                        OptionNumber.NumberType.SignedInt,
                        0, 1,-2700,2700,-250,5,2,61, "deg")
        );

        addOption(
                new OptionNumber("Rc Yaw Max",
                        OptionNumber.NumberType.SignedInt,
                        0, 1,-2700,2700,250,5,2,62, "deg")
        );

        addOption(
                new OptionNumber("Rc Yaw Speed Limit",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,0,1000,400,5,2,63, "deg/s")
        );


        addOption(
                new OptionNumber("Rc Yaw Accel Limit (0=off)",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 3,0,1000,300,10,2,64, "")
        );



        /*
        {
  name => "Rc Dead Band",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 0, max => 50, default => 10, steps => 1,
  size => 2,
  adr => 43,
  unit => "us",
  expert=> 3,
},
*/
        addOption(
                new OptionNumber("Rc Dead Band",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,0,50,10,1,2,43, "us")
        );



/*
name => "Rc Pitch",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
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
{  name => "Rc Pitch Mode",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 45,
  choices => [ "absolute", "relative", "absolute centered"],
}
 */

        addOption(
                new OptionListA("Rc Pitch Mode",
                        0,0,0,2,0,1,1,45,
                        new String[] {"absolute", "relative", "absolute centered"}
                )
        );

/*{
  name => "Rc Hysteresis",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 0, max => 50, default => 5, steps => 1,
  size => 2,
  adr => 105,
  unit => "us",

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
  name => "Standby",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
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

  name => "Re-center Camera",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
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
  name => "IR Camera Control",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
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
  name => "Camera Model",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 3, default => 0, steps => 1,
  size => 1,
  adr => 72,
  choices => [ "Sony Nex", "Canon", "Panasonic", "Nikon" ],
},*/
        String [] cameras = new String[] {"Sony Nex", "Canon", "Panasonic", "Nikon"};
        addOption(
                new OptionListA("Standby",
                        0,0,0,cameras.length,0,1,1,72,
cameras
                )
        );

        /*{
  name => "IR Camera Setting #1",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 73,
  choices => [ "shutter", "shutter delay", "video on/off" ],
}*/

        String [] irCamSetting1 = new String[] {"shutter", "shutter delay", "video on/off"};
        addOption(
                new OptionListA("IR Camera Setting #1",
                        0,0,0,irCamSetting1.length,0,1,1,73,
                        irCamSetting1
                )
        );

/*
        name => "Virtual Channel Configuration",
                type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 10, default => 0, steps => 1,
                size => 1,
                adr => 41,
                choices => [ "off",  "sum ppm 6", "sum ppm 7", "sum ppm 8", "sum ppm 10", "sum ppm 12",
                "spektrum 10 bit", "spektrum 11 bit", "sbus", "hott sumd", "srxl" ],
        column=> 2,
*/

        String [] virtualChannelConfiguration = new String[] {"off",  "sum ppm 6", "sum ppm 7", "sum ppm 8", "sum ppm 10", "sum ppm 12",
                "spektrum 10 bit", "spektrum 11 bit", "sbus", "hott sumd", "srxl"};
        addOption(
                new OptionListA("Virtual Channel Configuration",
                        0,0,0,virtualChannelConfiguration.length-1,0,1,1,41,
                        virtualChannelConfiguration
                )
        );

        /*
        {
  name => "Pwm Out Configuration",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
  size => 1,
  adr => 42,
  choices => [ "off", "1520 us 55 Hz", "1520 us 250 Hz" ],
         */

        String [] pwmOutConfiguration = new String[] {"off", "1520 us 55 Hz", "1520 us 250 Hz"};
        addOption(
                new OptionListA("Pwm Out Configuration",
                        0,0,0,pwmOutConfiguration.length-1,0,1,1,42,
                        pwmOutConfiguration
                )
        );
/*
        {
            name => "Pitch Motor Usage",
                type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 3, default => 3, steps => 1,
                size => 1,
                adr => 78,
                choices => [ "normal", "level", "startup pos", "disabled"],
            column=> 4,
        }
*/

        String [] motorUsage = new String[] {"normal", "level", "startup pos", "disabled"};
        addOption(
                new OptionListA("Pitch Motor Usage",
                        0,0,0,motorUsage.length-1,0,1,1,78,
                        motorUsage
                )
        );

        addOption(
                new OptionListA("Roll Motor Usage",
                        0,0,0,motorUsage.length-1,0,1,1,79,
                        motorUsage
                )
        );

        addOption(
                new OptionListA("Yaw Motor Usage",
                        0,0,0,motorUsage.length-1,0,1,1,80,
                        motorUsage
                )
        );
        
        
/*        {
  name => "IR Camera Setting #2",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 3, default => 2, steps => 1,
  size => 1,
  adr => 74,
  choices => [ "shutter", "shutter delay", "video on/off", "off" ],
}*/

        String [] irCamSetting2 = new String[] {"shutter", "shutter delay", "video on/off", "off"};
        addOption(
                new OptionListA("IR Camera Setting #2",
                        0,0,0,irCamSetting2.length-1,0,1,1,74,
                        irCamSetting2
                )
        );

/*
    },{
        name => "Imu Orientation",
                type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 23, default => 0, steps => 1,
                size => 1,
                adr => 39,
                choices => \@ImuChoicesList,
            expert=> 7,
                    pos=>[1,1],
    }*/

        class Orientation{
            String name;
            String [] axes = new String[3];
            int value;
            public Orientation(String name, String ax0, String ax1, String ax2, int val){
                this.name = name;
                axes[0] = ax0;
                axes[1] = ax1;
                axes[2] = ax2;
                this.value = val;
            }
            public String stringRepresentation(){
                String result = name + axes[0] + " " + axes[1] + " " + axes[2] + " " + value;
                return result;
            }
        }

/*
        my @ImuOrientationList=(
        { name => "z0°",     axes => "+x +y +z",  value => 0, },
        { name => "z90°",    axes => "-y +x +z",  value => 1, },
        { name => "z180°",   axes => "-x -y +z",  value => 2, },
        { name => "z270°",   axes => "+y -x +z",  value => 3, },

        { name => "x0°",     axes => "+y +z +x",  value => 4, },
        { name => "x90°",    axes => "-z +y +x",  value => 5, },
        { name => "x180°",   axes => "-y -z +x",  value => 6, },
        { name => "x270°",   axes => "+z -y +x",  value => 7, },

        { name => "y0°",     axes => "+z +x +y",  value => 8, },
        { name => "y90°",    axes => "-x +z +y",  value => 9, },
        { name => "y180°",   axes => "-z -x +y",  value => 10, },
        { name => "y270°",   axes => "+x -z +y",  value => 11, },

        { name => "-z0°",    axes => "+y +x -z",  value => 16, },
        { name => "-z90°",   axes => "-x +y -z",  value => 17, },
        { name => "-z180°",  axes => "-y -x -z",  value => 18, },
        { name => "-z270°",  axes => "+x -y -z",  value => 19, },

        { name => "-x0°",    axes => "+z +y -x",  value => 20, },
        { name => "-x90°",   axes => "-y +z -x",  value => 21, },
        { name => "-x180°",  axes => "-z -y -x",  value => 22, },
        { name => "-x270°",  axes => "+y -z -x",  value => 23, },

        { name => "-y0°",    axes => "+x +z -y",  value => 24, },
        { name => "-y90°",   axes => "-z +x -y",  value => 25, },
        { name => "-y180°",  axes => "-x -z -y",  value => 26, },
        { name => "-y270°",  axes => "+z -x -y",  value => 27, },
        );
  */
        LinkedList<Orientation> orientations = new LinkedList<>();
        orientations.add(new Orientation("z0°", "+x", "+y", "+z", 0));
        orientations.add(new Orientation("z90°", "-y", "+x", "+z", 1));
        orientations.add(new Orientation("z180°", "-x", "-y", "+z", 2));
        orientations.add(new Orientation("z270°", "+y", "-x", "+z", 3));

        orientations.add(new Orientation("z0°", "+x", "+y", "+z", 4));
        orientations.add(new Orientation("z90°", "-y", "+x", "+z", 5));
        orientations.add(new Orientation("z180°", "-x", "-y", "+z", 6));
        orientations.add(new Orientation("z270°", "+y", "-x", "+z", 7));

        orientations.add(new Orientation("z0°", "+x", "+y", "+z", 8));
        orientations.add(new Orientation("z90°", "-y", "+x", "+z", 9));
        orientations.add(new Orientation("z180°", "-x", "-y", "+z", 10));
        orientations.add(new Orientation("z270°", "+y", "-x", "+z", 11));


        orientations.add(new Orientation("z0°", "+x", "+y", "+z", 16));
        orientations.add(new Orientation("z90°", "-y", "+x", "+z", 17));
        orientations.add(new Orientation("z180°", "-x", "-y", "+z", 18));
        orientations.add(new Orientation("z270°", "+y", "-x", "+z", 19));

        orientations.add(new Orientation("z0°", "+x", "+y", "+z", 20));
        orientations.add(new Orientation("z90°", "-y", "+x", "+z", 21));
        orientations.add(new Orientation("z180°", "-x", "-y", "+z", 22));
        orientations.add(new Orientation("z270°", "+y", "-x", "+z", 23));

        orientations.add(new Orientation("z0°", "+x", "+y", "+z", 24));
        orientations.add(new Orientation("z90°", "-y", "+x", "+z", 25));
        orientations.add(new Orientation("z180°", "-x", "-y", "+z", 26));
        orientations.add(new Orientation("z270°", "+y", "-x", "+z", 27));

        LinkedList<String> orientationsNames = new LinkedList<>();
        for( Orientation o: orientations){
            orientationsNames.add(o.stringRepresentation());
        }



        addOption(
                new OptionListA("Imu Orientation",
                        0,0,0,orientationsNames.size(),0,1,1,39,
                        orientationsNames.toArray(new String[orientationsNames.size()])
                )
        );

        addOption(
                new OptionListA("Imu2 Orientation",
                        0,0,0,orientationsNames.size(),0,1,1,95,
                        orientationsNames.toArray(new String[orientationsNames.size()])
                )
        );


        /*
        name => "Imu2 Orientation",
                type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 23, default => 0, steps => 1,
                size => 1,
                adr => 95,
                choices => \@ImuChoicesList,

    },{*/



 /*
  name => "Pitch Motor Poles",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 12, max => 28, default => 14, steps => 2,
  size => 2,
  adr => 20,
  pos=> [2,1],
*/

/*
  name => "Yaw Pan (0 = hold)",
  type => "OPTTYPE_UI", len => 5, ppos => 1, min => 0, max => 50, default => 20, steps => 1,
  size => 2,
  adr => 16,
  column=> 4,
 */

        addOption(
                new OptionNumber("Pitch Pan (0 = hold)",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,50,20,1,2,4, "")
        );


        addOption(
                new OptionNumber("Pitch Pan Deadband",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,100,50,5,2,5, "")
        );


        addOption(
                new OptionNumber("Pitch Pan Expo",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,100,0,0,2,102, "")
        );

//------------------------ ROLL

        addOption(
                new OptionNumber("Roll Pan (0 = hold)",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,50,20,1,2,10, "")
        );


        addOption(
                new OptionNumber("Roll Pan Deadband",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,600,50,5,2,11, "")
        );


        addOption(
                new OptionNumber("Roll Pan Expo",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,100,0,0,2,103, "")
        );


// ------------------------YAW

        addOption(
                new OptionNumber("Yaw Pan (0 = hold)",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,50,20,1,2,16, "")
        );


        addOption(
                new OptionNumber("Yaw Pan Deadband",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,100,50,5,2,17, "")
        );


        addOption(
                new OptionNumber("Yaw Pan Expo",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,100,0,1,2,104, "")
        );

        addOption(
                new OptionNumber("Yaw Pan (0 = hold)",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,50,20,1,2,16, "")
        );


        addOption(
                new OptionNumber("Yaw Pan Deadband LPF",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,0,200,0,5,2,118, "")
        );


        addOption(
                new OptionNumber("Yaw Pan Deadband Hysteresis",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 1,0,50,0,1,2,97, "")
        );



        // -------------------------------------------------


        addOption(
                new OptionNumber("Pitch Motor Poles",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,12,28,14,2,2,20, "")
        );


        /*
  name => "Pitch Motor Direction",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 2, steps => 1,
  size => 1,
  adr => 21,
  choices => [ "normal",  "reversed", "auto" ],
*/

        String [] motorDirection = new String[] {"normal", "reversed", "auto"};
        addOption(
                new OptionListA("Pitch Motor Direction",
                        0,0,0,2,2,1,1,21,
            motorDirection
                )
        );


        /*
  name => "Pitch Startup Motor Pos",
  type => "OPTTYPE_UI", len => 5, ppos => 0, min => 0, max => 1008, default => 504, steps => 1,
  size => 2,
  adr => 23,
*/

        addOption(
                new OptionNumber("Pitch Startup Motor pos",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,1008,504,1,2,23, "")
        );

         /*
  name => "Pitch Offset",
  type => "OPTTYPE_SI", len => 5, ppos => 2, min => -300, max => 300, default => 0, steps => 5,
  size => 2,
  adr => 22,
  unit=> "°",

*/

        addOption(
                new OptionNumber("Pitch Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,-300,300,0,5,2,22, "")
        );

         /*
  name => "Roll Motor Poles",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 12, max => 28, default => 14, steps => 2,
  size => 2,
  adr => 26,
  pos=> [3,1],
*/
        addOption(
                new OptionNumber("Roll Motor Poles",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,12,28,14,2,2,26, "")
        );


        /*
  name => "Roll Motor Direction",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 2, steps => 1,
  size => 1,
  adr => 27,
  choices => [ "normal",  "reversed", "auto" ],
*/


        addOption(
                new OptionListA("Roll Motor Direction",
                        0,0,0,2,2,1,1,27,
                        motorDirection
                )
        );


        addOption(
                new OptionNumber("Pitch P",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,0,3000,400,10,2,0, "")
        );

        addOption(
                new OptionNumber("Pitch I",
                        OptionNumber.NumberType.UnsignedInt,
                        7, 1,0,20000,1000,50,2,1, "")
        );

        addOption(
                new OptionNumber("Pitch D",
                        OptionNumber.NumberType.UnsignedInt,
                        3, 4,0,8000,500,50,2,2, "")
        );



        addOption(
                new OptionNumber("Roll P",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,0,3000,400,10,2,6, "")
        );

        addOption(
                new OptionNumber("Roll I",
                        OptionNumber.NumberType.UnsignedInt,
                        7, 1,0,20000,1000,50,2,7, "")
        );

        addOption(
                new OptionNumber("Roll D",
                        OptionNumber.NumberType.UnsignedInt,
                        3, 4,0,8000,500,50,2,8, "")
        );


        addOption(
                new OptionNumber("Yaw P",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,0,3000,400,10,2,12, "")
        );

        addOption(
                new OptionNumber("Yaw I",
                        OptionNumber.NumberType.UnsignedInt,
                        7, 1,0,20000,1000,50,2,13, "")
        );

        addOption(
                new OptionNumber("Yaw D",
                        OptionNumber.NumberType.UnsignedInt,
                        3, 4,0,8000,500,50,2,14, "")
        );

        
         /*
  name => "Roll Startup Motor Pos",
  type => "OPTTYPE_UI", len => 5, ppos => 0, min => 0, max => 1008, default => 504, steps => 1,
  size => 2,
  adr => 29,
*/

        addOption(
                new OptionNumber("Roll Startup Motor pos",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,1008,504,1,2,29, "")
        );

         /*
  name => "Roll Offset",
  type => "OPTTYPE_SI", len => 5, ppos => 2, min => -300, max => 300, default => 0, steps => 5,
  size => 2,
  adr => 28,
  unit=> "°",

*/

        addOption(
                new OptionNumber("Roll Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,-300,300,0,5,2,28, "°")
        );

         /*
  name => "Yaw Motor Poles",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 12, max => 28, default => 14, steps => 2,
  size => 2,
  adr => 32,
  pos=> [4,1],
*/
        addOption(
                new OptionNumber("Yaw Motor Poles",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,12,28,14,2,2,32, "")
        );

         /*
  name => "Yaw Motor Direction",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 2, default => 2, steps => 1,
  size => 1,
  adr => 33,
  choices => [ "normal",  "reversed", "auto", ],
*/



        addOption(
                new OptionListA("Yaw Motor Direction",
                        0,0,0,2,2,1,1,33,
                        motorDirection
                )
        );



         /*
  name => "Yaw Startup Motor Pos",
  type => "OPTTYPE_UI", len => 5, ppos => 0, min => 0, max => 1008, default => 504, steps => 1,
  size => 2,
  adr => 35,
*/

        addOption(
                new OptionNumber("Pitch Startup Motor pos",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 0,0,1008,504,1,2,35, "")
        );

         /*
  name => "Yaw Offset",
  type => "OPTTYPE_SI", len => 5, ppos => 2, min => -300, max => 300, default => 0, steps => 5,
  size => 2,
  adr => 34,
  unit=> "°",

 */

        addOption(
                new OptionNumber("Yaw Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,-300,300,0,5,2,34, "°"));
        
        /*
        {
  name => "Imu AHRS",
  type => "OPTTYPE_UI", len => 5, ppos => 2, min => 0, max => 2500, default => 1000, steps => 100,
  size => 2,
  adr => 81,
  unit => "s",
         */

        addOption(
                new OptionNumber("Imu AHRS",
                        OptionNumber.NumberType.UnsignedInt,
                        5, 2,0,2500,1000,100,2,81, "s")
        );

/*
name => "Rc Pitch Offset",
  type => "OPTTYPE_SI", len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 106,
  unit => "°",
  pos=> [2,4],

  {
  name => 'Rc Roll Offset',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 107,
  unit => '°',
},{
  name => 'Rc Yaw Offset',
  type => 'OPTTYPE_SI', len => 0, ppos => 1, min => -1200, max => 1200, default => 0, steps => 5,
  size => 2,
  adr => 108,
  unit => '°',

 */


        addOption(
                new OptionNumber("Rc Pitch Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,-1200,1200,0,5,2,106, "°")
        );

        addOption(
                new OptionNumber("Rc Roll Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,-1200,1200,0,5,2,107, "°")
        );

        addOption(
                new OptionNumber("Rc Yaw Offset",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 1,-1200,1200,0,5,2,108, "°")
        );


        /*name => "Imu2 Configuration",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => 4, default => 0, steps => 1,
  size => 1,
  adr => 94,
  choices => [ "off", "full", "full xy", "full v1", "full v1 xy" ],
  expert=> 5,*/


        String [] imu2configuration = new String[] { "off", "full", "full xy", "full v1", "full v1 xy" };
        addOption(
                new OptionListA("Imu2 Configuration",
                        0,0,0,imu2configuration.length-1,0,1,1,94,
                        imu2configuration
                )
        );

/*
        name => 'Beep with Motors',
                type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => 2, default => 0, steps => 1,
                size => 1,
                adr => 98,
                choices => [ 'off', 'basic', 'all' ],
        pos=> [3,4],
        */

        String [] beepWithMotors = new String[] {"off", "basic", "all"};

        addOption(
                new OptionListA("Imu2 Configuration",
                        0,0,0,beepWithMotors.length-1,0,1,1,98,
                        beepWithMotors
                )
        );

        /*{
  name => "Time Interval (0 = off)",
  type => "OPTTYPE_UI", len => 0, ppos => 1, min => 0, max => 150, default => 0, steps => 1,
  size => 2,
  adr => 75,
  unit => "s",

}*/

        addOption(
                new OptionNumber("Time Interval (0 = off)",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,0,150,0,1,2,75, "s")
        );
        
        /*{
  name => "Pwm Out Control",
  type => "OPTTYPE_LISTA", len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => 113,
  choices => \@FunctionInputChoicesList,
  column=> 3,
}*/

        addOption(
                new OptionListA("Pwm Out Control",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,113,
                        functionInputChoicesList
                )
        );

        /*
        ##--- SCRIPTS tab --------------------
},{
  name => 'Script1 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-5, #119,
  choices => \@FunctionInputChoicesList,
  expert=> 8,
  column=> 1,
},{
  name => 'Script2 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-4, #120,
  choices => \@FunctionInputChoicesList,
  column=> 2,
},{
  name => 'Script3 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-3, #121,
  choices => \@FunctionInputChoicesList,
  column=> 3,
},{
  name => 'Script4 Control',
  type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
  size => 1,
  adr => $CMD_g_PARAMETER_ZAHL-2, #122,
  choices => \@FunctionInputChoicesList,
  column=> 4,
         */

        addOption(
                new OptionListA("Script1Control",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,119,
                        functionInputChoicesList
                )
        );
        addOption(
                new OptionListA("Script2Control",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,120,
                        functionInputChoicesList
                )
        );
        addOption(
                new OptionListA("Script3Control",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,121,
                        functionInputChoicesList
                )
        );
        addOption(
                new OptionListA("Script4Control",
                        0,0,0,functionInputChoicesList.length-1,0,1,1,122,
                        functionInputChoicesList
                )
        );

        
        /*{
  name => "Pwm Out Mid",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 900, max => 2100, default => 1500, steps => 1,
  size => 2,
  adr => 114,
  unit => "us",
}*/
        addOption(
                new OptionNumber("Pwm Out Mid",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,900,2100,1500,10,2,114, "us")
        );


        /*{
  name => "Pwm Out Min",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 900, max => 2100, default => 1100, steps => 10,
  size => 2,
  adr => 115,
  unit => "us",
}*/

        addOption(
                new OptionNumber("Pwm Out Min",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,900,2100,1100,10,2,115, "us")
        );

        /*{
  name => "Pwm Out Max",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 900, max => 2100, default => 1900, steps => 10,
  size => 2,
  adr => 116,
  unit => "us",
}*/
        addOption(
                new OptionNumber("Pwm Out Mid",
                        OptionNumber.NumberType.UnsignedInt,
                        0, 0,900,2100,1900,10,2,116, "us")
        );

        /*{
  name => "Pwm Out Speed Limit (0 = off)",
  type => "OPTTYPE_UI", len => 0, ppos => 0, min => 0, max => 1000, default => 0, steps => 5,
  size => 2,
  adr => 117,
  unit => "us/s",
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

    static double readOptionFromOptions(Option o, int length){

        double result = 0;
        int address = o.address;

        result = decodeByte(options[2*address]);
        result += 256*decodeByte(options[2*address+1]);

        if(result > 61440){
            result = -(65536-result);
        }


        if(result > o.getMax())
            result = o.getMax();
        if(result < o.getMin())
            result = o.getMin();


        //if(o.ppos > 0)
//            result = result / (10.0*o.ppos);

        return result;

    }

    static void saveToOptions(Option o, int length, double value){

        int address = o.address;


  //      if(o.ppos > 0)
//            value = value * 10 * o.ppos;


        if(value < 0){
            value = 65536 - Math.abs(value);
        }

        //result = decodeByte(options[2*address]);
        //result += 256*decodeByte(options[2*address+1]);
        options[2*address] = (byte) (value % 256);
        options[2*address+1] = (byte) (value / 256);


    }

}

