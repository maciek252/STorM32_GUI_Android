package info.androidhive.materialtabs.storm32;

/**
 * Created by maciek on 01.04.16.
 */
public class Option {



    double value = 0.0;
    double valueRead = 0.0;
    public boolean hasBeenRead = false;
    public boolean hasBeenChanged = false;
    public int address = -1;
    public int size = -1;
    public int ppos = -1;
    public boolean needUpdate = false;

    protected int min;
    protected int max;


    public double defaultValue = 1.0;


    public boolean isRead(){
        return hasBeenRead;
    }

    public double getValueRead(){
        return valueRead;
    }

    public double getValue(){
        return value;
    }

    public void setValueRead(double v){
        value = v;
        valueRead = v;
    }

    public double convertToWithDot(double v){
        if(ppos > 0) {
            double coeff = Math.pow(10, ppos);
            v /= coeff;
        }

        return v;
    }

    public int convertToWithoutDot(double v){
        if(ppos > 0)
            v = v * (Math.pow(10,ppos));

        return (int)v;
    }


    public void setValueFromWithouDot(int v){
        value = v;
    }


    public void setRead(){
        hasBeenRead = true;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getValueWithoutDot(){
        return (int)value;
    }

    public int getppos(){
        return ppos;
    }




}
