package info.androidhive.materialtabs.storm32;

/**
 * Created by maciek on 01.04.16.
 */
public class OptionListA extends Option {

    public String description = null;
    public String [] choices = null;

    public OptionListA(String description,
                     int length,
                     int ppos,
                     int min, int max,
                     int steps,
                     int size, int address,
                     String [] choices
                     ){
        this.description = description;
        this.choices = choices;
        this.address = address;
    }

}
