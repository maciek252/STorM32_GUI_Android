package utils;

/**
 * Created by maciek on 5/18/16.
 */
public class Utils {

    public static long getNumberFromByteArray(byte [] a, int addr){
        long result = 0;
        result = decodeByte(a[2*addr]);
        result += 256*decodeByte(a[2*addr+1]);

//        if(result > 61440){
        if(result > 31000){
            result = -(65536-result);
        }
        return result;
    }

    static private int decodeByte(byte b){
        int result = 0;
        int b0 = (int) b;
        int b3 = (int)(b & 0xFF);
        return b3;
    }
}
