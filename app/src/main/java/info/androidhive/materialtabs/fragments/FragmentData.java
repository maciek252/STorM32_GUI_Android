package info.androidhive.materialtabs.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.activity.MainActivity;
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

public class FragmentData extends Fragment implements View.OnClickListener{


//    private SimpleBluetooth simpleBluetooth;
    Button button = null;
    TextView tv = null;


    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    private byte bufor[] = new byte[10];

    private FragmentConnection fragmentConnection = null;


    private RadioGroup radioGroupImuSelect = null;

    final Handler handler_interact=new Handler();//not defined as final variable. may cause problem

    Timer timer = null;
    private long previous = 0;

    GraphView graph = null;


    protected TextView imuStatus = null;
    protected TextView imu2Status = null;
    protected TextView i2cErrors = null;

    public FragmentData(){

    }

    public void setFragmentConnection(FragmentConnection fc){
        this.fragmentConnection = fc;
    }

    @Override
    public void onPause(){
        //if( timer != null) {
            //timer.cancel();
            //timer.purge();
//        }
        timer = null;

        super.onPause();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void setImuState(int imuNr, boolean active){
        if(imuNr != 0 && imuNr != 1)
            return;

        TextView imuState = null;
        if(imuNr == 0)
            imuState = imuStatus;
        else if(imuNr == 1)
            imuState = imu2Status;

        if(active){
            imuState.setBackgroundColor(Color.GREEN);
            imuState.setText("OK");
        } else{
            imuState.setBackgroundColor(Color.RED);
            imuState.setText("ERR");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        button = (Button) v.findViewById(R.id.buttonFindIdDevices);
        button.setOnClickListener(this);

        radioGroupImuSelect = (RadioGroup)v.findViewById(R.id.data_radioGroupImu);



        radioGroupImuSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(getContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        tv = (TextView) v.findViewById(R.id.textView2);

         imuStatus = (TextView) v.findViewById(R.id.textView_data_imuState);
        imu2Status = (TextView) v.findViewById(R.id.textView_data_imu2_state);
        i2cErrors = (TextView) v.findViewById(R.id.textView_data_i2c_errors);

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

    private void runUpdating(){
        if(timer != null ) {
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {

                    try {

                        updateGUI();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }, 0, 500);
        }
    }


    @Override
    public void onClick(View v) {





        switch (v.getId()) {
            case R.id.buttonFindIdDevices:

                timer = new Timer();
                runUpdating();




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
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            byte [] a = InterFragmentCom.getData_d();
            synchronized (a){
            if(tv != null && a != null && a.length > 10) {
                int status1 = (int) Utils.getNumberFromByteArray(a, 1);
                int status2 = (int) Utils.getNumberFromByteArray(a, 2);
                int i2cError = (int) Utils.getNumberFromByteArray(a, 3);
                double pitch = (double) Utils.getNumberFromByteArray(a, 16) / 100.0;
                double pitch2 = (int) Utils.getNumberFromByteArray(a, 25) / 100.0;

                //series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 100);
                series.appendData(new DataPoint(lastX++, pitch), true, 30);

                byte aa = (byte) (status1 & 32);
                if (aa != 0)
                    setImuState(0, true);
                else
                    setImuState(0, false);

                setImuState(1, true);
                i2cErrors.setText(" " + i2cError);

                int voltage = (int) Utils.getNumberFromByteArray(a, 4);
                int magYaw = (int) Utils.getNumberFromByteArray(a, 28);
                tv.setText(" i2cerr=" + i2cError + " p1=" + pitch + "p2=" + pitch2 + " v=" + voltage + " mag=" + magYaw + "s1=" + status1 + " s2=" + status2);
            }
            }


        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph

        runUpdating();

        /*
        long temp = System.currentTimeMillis();
        if(temp - previous > 100){
            previous = temp;
            updateGUI();
        }*/



    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 100);

        //graph.refreshDrawableState();
        //graph.setHorizontalScrollBarEnabled(true);
    }


}
