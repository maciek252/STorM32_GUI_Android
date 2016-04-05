package info.androidhive.materialtabs.storm32;

/**
 * Created by maciek on 01.04.16.
 */
public class Option {

    int value = 0;
    int valueRead = 0;
    public boolean hasBeenRead = false;
    public boolean hasBeenChanged = false;
    public int address = -1;
    public int size = -1;
    public int ppos = -1;

    public int defaultValue = 1;


    public boolean isRead(){
        return hasBeenRead;
    }

    public int getValueRead(){
        return valueRead;
    }

    public int getValue(){
        return value;
    }

    public void setValueRead(int v){
        value = v;
        valueRead = v;
    }

    public void setValue(int v){
        value = v;
    }

    public void setRead(){
        hasBeenRead = true;
    }
}
