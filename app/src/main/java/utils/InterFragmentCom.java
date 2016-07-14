package utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by maciek on 5/19/16.
 * to be fixed/refactored!
 */
public class InterFragmentCom {

    static byte [] data_d = null;

    public static boolean readData_d = false;

    public static void setReadData_d(boolean v){
        readData_d = v;
    }


    public static void setData_d(byte[] d){
        data_d = d;
    }

    public static byte[] getData_d(){
        return data_d;
    }

    static public void addToTempBuffer(byte [] o, int num) {


        //byte [] oo = System.arraycopy(o, 0, num);
        byte [] oo = Arrays.copyOfRange(o, 0, num);

        if( data_d == null)
            data_d = oo;

        else {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(data_d);
                outputStream.write(oo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            data_d = outputStream.toByteArray();
        }

    }

    static public void clearData(){
        data_d = new byte[0];
    }

    static public int getBufferLen(){ return data_d.length;}

}
