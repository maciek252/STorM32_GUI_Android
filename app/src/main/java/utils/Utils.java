package utils;

/**
 * Created by maciek on 5/18/16.
 */
public class Utils {


    public static long getTwoByteNumberFromByteArray(byte[] a, int addr){

        long result = 0;
        result = decodeByte(a[addr]);
        result += 256*decodeByte(a[addr+1]);

//        if(result > 61440){
        if(result > 31000){
            result = -(65536-result);
        }
        return result;

    }

    public static long getOneByteNumberFromByteArray(byte[] a, int addr){

        long result = 0;
        result = decodeByte(a[addr]);

//        if(result > 61440){
        if(result > 128){
            result = -(256-result);
        }
        return result;

    }

    public static long getOptionFromByteArray(byte [] a, int addr){
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

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    public static int indexOfTwoArrays( byte[] data, int start, int stop, byte[] pattern) {
        if( data == null || pattern == null) return -1;

        int[] failure = computeFailure(pattern);

        int j = 0;

        for( int i = start; i < stop; i++) {
            while (j > 0 && ( pattern[j] != '*' && pattern[j] != data[i])) {
                j = failure[j - 1];
            }
            if (pattern[j] == '*' || pattern[j] == data[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    /**
     * Computes the failure function using a boot-strapping process,
     * where the pattern is matched against itself.
     */
    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j>0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }

}
