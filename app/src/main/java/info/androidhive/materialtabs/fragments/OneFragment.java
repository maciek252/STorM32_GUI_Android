package info.androidhive.materialtabs.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialtabs.R;

/*
import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.abstracts.BaseBluetoothActivity;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;
*/
public class OneFragment extends Fragment implements View.OnClickListener {


//    private SimpleBluetooth simpleBluetooth;
    Button button = null;
    TextView tv = null;


    public OneFragment() {

        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
/*
    @Override
    public void onResume() {
        super.onResume();
//        Log.d("MAIN", "OnResume Called");
        //this check needs to be here to ensure that the simple bluetooth is not reset.
        //an issue was occuring when a client would connect to a server. When a client
        // connects they have to select a device, that is another activity, so after they
        //select a device, this gets called again and the reference to the original simpleBluetooth
        //object on the client side gets lost. Thus when send is called, nothing happens because it's
        //a different object.
        if(simpleBluetooth == null) {
            //new SimpleBluetooth(getContext(), getActivity());
            //simpleBluetooth = new SimpleBluetooth(getContext(), new SimpleBluetoothListener() {
            SimpleBluetoothListener sbl = new SimpleBluetoothListener() {
                @Override
                public void onBluetoothDataReceived(byte[] bytes, String data) {
                    //read the data coming in.
                    Toast.makeText(getActivity(), "Data: " + data, Toast.LENGTH_SHORT).show();
                    //connectionState.setText("Data: " + data);
                    //isConnected = false;
                    Log.w("SIMPLEBT", "Data received");
                }

                @Override
                public void onDeviceConnected(BluetoothDevice device) {
                    //a device is connected so you can now send stuff to it
                    Toast.makeText(getActivity(), "Connected!", Toast.LENGTH_SHORT).show();
                    //connectionState.setText("Connected");
                    //isConnected = true;
                    Log.w("SIMPLEBT", "Device connected");
                }

                @Override
                public void onDeviceDisconnected(BluetoothDevice device) {
                    // device was disconnected so connect it again?
                    Toast.makeText(getActivity(), "Disconnected!", Toast.LENGTH_SHORT).show();
                    //connectionState.setText("Disconnected");
                    Log.w("SIMPLEBT", "Device disconnected");
                }
            };
            //simpleBluetooth = new SimpleBluetooth(getContext(), sbl);
        }
        simpleBluetooth.initializeSimpleBluetooth();
        simpleBluetooth.setInputStreamType(BluetoothUtility.InputStreamType.BUFFERED);
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        button = (Button) v.findViewById(R.id.buttonFindIdDevices);
        button.setOnClickListener(this);

        tv = (TextView) v.findViewById(R.id.textView2);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonFindIdDevices:

                /*
                Spinner btDevices = (Spinner) v.findViewById(R.id.spinnerBtDevices);
                final List<String> list=new ArrayList<String>();
                list.add("Item 1");
                list.add("Item 2");
                list.add("Item 3");
                list.add("Item 4");
                list.add("Item 5");

                //ArrayAdapter<String> adp1=new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1,list);
  //              adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //            btDevices.setAdapter(adp1);
*/
                //EditText editText = (EditText) v.findViewById(R.id.editText);
                //editText.setText("ple");
                if(tv != null)
                    tv.setText("ple");
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    return;
                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    int REQUEST_ENABLE_BT = 2;
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                // query paired

                //discover:




                break;




        }
    }
}
