package info.androidhive.materialtabs.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import info.androidhive.materialtabs.R;
import utils.InterFragmentCom;
import utils.Utils;

/*
import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.abstracts.BaseBluetoothActivity;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;
*/

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentData extends Fragment implements View.OnClickListener {


//    private SimpleBluetooth simpleBluetooth;
    Button button = null;
    TextView tv = null;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    private byte bufor[] = new byte[10];

    private FragmentConnection fragmentConnection = null;

    final Handler handler_interact=new Handler();//not defined as final variable. may cause problem

    Timer timer = null;
    private long previous = 0;

    GraphView graph = null;

    public FragmentData(){

    }

    public void setFragmentConnection(FragmentConnection fc){
        this.fragmentConnection = fc;
    }

    @Override
    public void onPause(){
        super.onPause();
        if( timer != null)
            timer.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        button = (Button) v.findViewById(R.id.buttonFindIdDevices);
        button.setOnClickListener(this);

        tv = (TextView) v.findViewById(R.id.textView2);

        //v.setContentView(R.layout.activity_main);
        // we get graph view instance
        graph = (GraphView) v.findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-180);
        viewport.setMaxY(180);
        viewport.setScrollable(true);

        return v;
    }

    @Override
    public void onClick(View v) {





        switch (v.getId()) {
            case R.id.buttonFindIdDevices:
                timer = new Timer();
                timer.scheduleAtFixedRate( new TimerTask() {
                    public void run() {

                        try{

                            updateGUI();

                        }
                        catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }, 0, 500);






                break;




        }
    }

    protected void updateGUI(){
        handler_interact.post(runnable_interact);
        //updateMy();
    }
    final Runnable runnable_interact = new Runnable() {
        public void run() {
            //addEntry();

            InterFragmentCom.clearData();

            if(fragmentConnection != null)
                fragmentConnection.readData_d();
            InterFragmentCom.setReadData_d(true);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte [] a = InterFragmentCom.getData_d();
            if(tv != null && a != null && a.length > 5) {
                int status1 = (int)Utils.getNumberFromByteArray(a, 1);
                int status2 = (int)Utils.getNumberFromByteArray(a, 2);
                int i2cError = (int)Utils.getNumberFromByteArray(a, 3);
                double pitch = (double)Utils.getNumberFromByteArray(a, 16) / 100.0;
                double pitch2 = (int)Utils.getNumberFromByteArray(a, 25) / 100.0;

                //series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 100);
                series.appendData(new DataPoint(lastX++, pitch), true, 30);


                int voltage = (int)Utils.getNumberFromByteArray(a, 4);
                int magYaw = (int)Utils.getNumberFromByteArray(a, 28);
                tv.setText(" i2cerr=" +  i2cError  + " p1=" + pitch + "p2=" +pitch2 + " v=" + voltage + " mag=" + magYaw + "s1=" + status1 + " s2=" + status2);
            }


        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph



        long temp = System.currentTimeMillis();
        if(temp - previous > 1000){
            previous = temp;
            updateGUI();
        }



    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 100);

        //graph.refreshDrawableState();
        //graph.setHorizontalScrollBarEnabled(true);
    }


}
