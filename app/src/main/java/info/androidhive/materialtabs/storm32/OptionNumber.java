package info.androidhive.materialtabs.storm32;

/**
 * Created by maciek on 01.04.16.
 */
public class OptionNumber extends Option {

    public enum NumberType {UnsignedInt, SignedInt, UnsignedChar, SignedChar};


    public int min;
    public int max;
    public int defaultValue;
    public int length;

    static public String unit;

    public OptionNumber(String description, NumberType numberType,
                             int length,
                             int ppos,
                             int min, int max,
                             int defaultValue,
                             int steps,
                             int size, int address,
                             String unit){
        this.min = min;
        this.max = max;
        this.length = length;
        value = defaultValue;
        this.address = address;
        this.unit = unit;
    }



}
