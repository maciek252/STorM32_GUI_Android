package info.androidhive.materialtabs.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import info.androidhive.materialtabs.R;


import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;

import org.mavlink.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import info.androidhive.materialtabs.storm32.*;
import quadcopter.Bluetooth;
import utils.InterFragmentCom;


public class FragmentConnection extends Fragment
        implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener,
        View.OnClickListener
{

    ViewPager vp = null;

    private String statusStr = "";

    public final String TAG = "Main";

    private SeekBar elevation;
    private TextView debug;
    private TextView status;
    private Bluetooth bt;





    String messageBuffer = "";

    //byte [] options = null;



    Button button = null;
    Button button_2_readVersion = null;
    Button button_2_setName = null;
    Button button_2_readOptions = null;

    Button button_2_SaveOptions = null;
    Button button_2_readOption = null;
    Button button_2_changeOption = null;
    Button button_2_disconnect = null;
    Button button_2_saveToEeprom = null;
    Button button_connectUSB = null;
    Button button_btTest = null;
    TextView tv = null;
    TextView tv_connectionStatus = null;
    TextView tv_receivedBt = null;
    TextView tv_name = null;
    TextView tv_board_version = null;
    TextView tv_software_version = null;
    TextView tv_board = null;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothSerial bluetoothSerial;

    public FragmentConnection() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
        bluetoothSerial.setup();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create a new instance of BluetoothSerial
        bluetoothSerial = new BluetoothSerial(getContext(), this);
        bt = new Bluetooth(getContext(), mHandler);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_connection, container, false);


        View v = inflater.inflate(R.layout.fragment_connection, container, false);
        button = (Button) v.findViewById(R.id.connection_button_detect_bt);
        button.setOnClickListener(this);

        button_connectUSB = (Button) v.findViewById(R.id.connection_button_usb_connect);

        debug = (TextView) v.findViewById(R.id.textDebug);
        status = (TextView) v.findViewById(R.id.textStatus);

        /*
        v.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //connectService();

                //UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

            }
        });*/

        button_btTest = (Button) v.findViewById(R.id.connection_button_btTest);
        button_btTest.setOnClickListener(this);

        button_2_readVersion = (Button) v.findViewById(R.id.connection_button_readVersion);
        button_2_readVersion.setOnClickListener(this);
        button_2_setName = (Button) v.findViewById(R.id.connection_button_setName);
        button_2_setName.setOnClickListener(this);

        button_2_readOptions = (Button) v.findViewById(R.id.connection_button_readOptions);
        button_2_readOptions.setOnClickListener(this);

        button_2_SaveOptions = (Button) v.findViewById(R.id.connection_button_saveOptions);
        button_2_SaveOptions.setOnClickListener(this);

        button_2_readOption = (Button) v.findViewById(R.id.connection_button_detect_bt);
        button_2_readOption.setOnClickListener(this);

        button_2_disconnect = (Button) v.findViewById(R.id.connection_button_disconnectBT);
        button_2_disconnect.setOnClickListener(this);

        button_2_saveToEeprom = (Button) v.findViewById(R.id.connection_button_saveToEeprom);
        button_2_saveToEeprom.setOnClickListener(this);

        tv = (TextView) v.findViewById(R.id.textViewDetectedBT);
        tv_connectionStatus = (TextView) v.findViewById(R.id.textView_2_connectionStatus);
        tv_receivedBt =  (TextView) v.findViewById(R.id.textView_2_rcvBt);

        tv_receivedBt.setMovementMethod(new ScrollingMovementMethod());


        tv_name =  (TextView) v.findViewById(R.id.textView_connection_boardName);
        tv_board_version = (TextView) v.findViewById(R.id.textView_connection_board_version);
        tv_software_version = (TextView) v.findViewById(R.id.textView_connection_software_version);
        tv_board = (TextView) v.findViewById(R.id.textView_2_board);


        final InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                imm.toggleSoftInput(0, 0);
            }
        });

        return v;
    }

    public void onResume() {
        super.onResume();

        status.setText(statusStr);
//        updateAllControlsAccordingToOptionList();

    }

    private void readOptions(){
        bt.queryMode = Bluetooth.QueryMode.GET_OPTIONS;
        //bluetoothSerial.write("g");
        bt.sendMessage("g");

    }

    public void readData_d(){
        bt.queryMode = Bluetooth.QueryMode.GET_DATA_D;
        bt.sendMessage("d");
    }


    @Override
    public void onClick(View v) {

        bt.queryMode = Bluetooth.QueryMode.NONE;

        switch (v.getId()) {
            case R.id.connection_button_btTest:

                IntermediateFragment.setColorToAllControls();

                BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(getActivity());
                dialog.setOnDeviceSelectedListener(this);
                dialog.setTitle("paired devices");
                dialog.setDevices(bluetoothSerial.getPairedDevices());
                dialog.showAddress(true);
                dialog.show();
                break;
            case  R.id.connection_button_readVersion:
                //bluetoothSerial.getConnectedDeviceName().

                bt.queryMode = Bluetooth.QueryMode.GET_VERSION;

                InterFragmentCom.clearData();
                String getVersion = "v";
                bt.write2(getVersion.getBytes());
                break;

            case R.id.connection_button_saveToEeprom:



                bt.queryMode = Bluetooth.QueryMode.SAVE_TO_EEPROM;
                String saveToEeprom = "xs";
                bt.write2(saveToEeprom.getBytes());
                break;

            case R.id.connection_button_usb_connect:

                break;

            case R.id.connection_button_setName:
                //  my $res = ExecuteCmdwCrc( 'xn', %name, 0 );
                /*
                sub ExecuteCmdwCrc{
                      my $cmd= shift; my $params= shift; my $reslen= shift;
                        return ExecuteCmd( $cmd.add_crc_to_data($params), $reslen );

                 */

                String wej = "testName";
                String o = String.format("%-20s", wej);
                o = o.substring(0,16);

                  bluetoothSerial.write("xn", false);
  //              bluetoothSerial.write(o, false);
                /* //OK DZIAŁA
                bluetoothSerial.write(n);
*/
                //o = "0123456789012345";
                o = tv_name.getText().toString();
                if(o.length() > 16)
                    o = o.substring(0,15);
                else if(o.length() < 16)
                    o = String.format("%0$-16s", o) ;
                byte [] i = MAVLinkCRC.stringToByte("xn" + o + "cc");
                //i[18] = (byte) 0x93;
                //i[19] = (byte) 0xe6;

                byte [] s= MAVLinkCRC.stringToByte(o);
                int crc = MAVLinkCRC.crc_calculate(s);
                byte[] crcArray = MAVLinkCRC.intToHexVax(crc);
                i[18] = crcArray[0];
                i[19] = crcArray[1];
//                bluetoothSerial.write(i);
                bt.write2(i);



                break;
            case R.id.connection_button_readOptions:

                readOptions();

                break;
            case R.id.connection_button_saveOptions:
                //options = optionList.getOptions();
                bt.queryMode = Bluetooth.QueryMode.SET_OPTIONS;
                ;

                if(optionList.getOptions() != null) {
                    optionList.encodeOptions();
                    tv_receivedBt.setText("size of opTions = " + optionList.getOptions().length);
                }else {
                    tv_receivedBt.setText("optins = null");
                    return;
                }

                byte [] optionsFull = new byte[381];
                optionsFull[0] = 'p';
                //optionsFull[380] = 'e'; // i tak nadpisane, dla testu
                tv_receivedBt.append("oko");
                byte [] optionsWrite = new byte [125*2 + 128 ];
                for(int ii = 0; ii < 125*2 + 128 ; ii++){

                    byte b = optionList.getOptions()[ii];
                    optionsWrite[ii] = b;
                    optionsFull[ii+1] = b;
                }
                int crc2 = MAVLinkCRC.crc_calculate(optionsWrite);
                byte[] crcArray2 = MAVLinkCRC.intToHexVax(crc2);

                //for(int ii = 0; ii < 125*2; ii++) // 2 crc, 'o'
// 378 elements - contents 0-377
                // in write - 1 - 378

                optionsFull[379] = crcArray2[0];
                optionsFull[380] = crcArray2[1];

                tv_receivedBt.append("crc=" + String.format("%01X",crcArray2[0])  + "," + String.format("%01X",crcArray2[1]));

                //bluetoothSerial.write(optionsFull);
                bt.write2(optionsFull);

                //SystemClock.sleep(1000);

                // Execute some code after 2 seconds have passed
                break;
            case R.id.connection_button_detect_bt:



                //tv_receivedBt.setText("  " + String.format("%01X",options[36])  + "," + String.format("%01X",options[37]));
                //optionList.voltageCorrection = options[38];
                showDeviceListDialog();


                break;

            case R.id.connection_button_disconnectBT:

                //bluetoothSerial.stop();
                bt.stop();
                break;

        }
    }

    /* Implementation of BluetoothSerialListener */

    @Override
    public void onBluetoothNotSupported() {

        new AlertDialog.Builder(getActivity())
                .setMessage("ni ma BT")
                .setPositiveButton("boo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                })
                .setCancelable(false)
                .show();

    }

    @Override
    public void onBluetoothDisabled() {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        //invalidateOptionsMenu();
        //updateBluetoothState();
        tv_connectionStatus.setText("disconnected");
    }

    @Override
    public void onConnectingBluetoothDevice() {
        //updateBluetoothState();
        tv_connectionStatus.setText("connecting");
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        //invalidateOptionsMenu();
        //updateBluetoothState();
        tv_connectionStatus.setText("connected");
    }

    @Override
    public void onBluetoothSerialRead(byte [] m) {
        int i = m.length;
        String s1 = String.format("%01X", m[0]);
        String s2 = String.format("%01X", m[1]);
        int i4 = (m[0] & 0x0F)  * 16 + (m[0] & 0xF0);
        int i5 = (m[0] & 0xF0)  * 16 + (m[0] & 0x0F);


    }

    @Override
    public void onBluetoothSerialRead(String message) {
        /*
        tv_receivedBt.append("<" + message + ">");

        if(bt.queryMode == Bluetooth.QueryMode.GET_OPTIONS){
            if(options == null )
                options = MAVLinkCRC.stringToByte(message);
            else {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                try {
                    outputStream.write(options);
                    outputStream.write(MAVLinkCRC.stringToByte(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                options = outputStream.toByteArray();

                if(options.length >= 381 && options[380] == 'o'){

                    // check CRC (?)


                    Toast toast = Toast.makeText(getContext(), "options received, CRC OK!", Toast.LENGTH_SHORT);
                    //toast.setDuration;
                    toast.show();
                    optionList.setOptions(options);
                    optionList.decodeOptions();
                }

            }
        }


        messageBuffer = messageBuffer + message;
        tv_receivedBt.append("{" + messageBuffer + "}");
        //if( message.length() == 0)
//            return;
        if(messageBuffer.startsWith("v") && messageBuffer.endsWith("o")){

            String version = messageBuffer.substring(0,16);
            String name = messageBuffer.substring(16,16);
            //String board = messageBuffer.substring(32,16);

            tv_name.append(name);
            //tv_board.setText(board);
            tv_board.append("ff");
            tv_version.append(version);
        }
*/
        // Print the incoming message on the terminal screen
        /*
        tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getConnectedDeviceName(),
                message));
        svTerminal.post(scrollTerminalToBottom);
        */
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen



        /*
        tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getLocalAdapterName(),
                message));
                */
        //svTerminal.post(scrollTerminalToBottom);

    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        //bluetoothSerial.connect(device);

        try {
            status.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                //bt.connectDevice("HC-06");
                //bt.connectDevice(device.getName());
                bt.connectDeviceByAddress(device.getAddress());


                Log.d(TAG, "Btservice started - listening");
                status.setText("Connected");
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Unable to connect " +e);
        }

    }

    /* End of the implementation of listeners */



    public void connectService(){
        try {
            status.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("HC-06");
                Log.d(TAG, "Btservice started - listening");
                status.setText("Connected");
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Unable to connect " +e);
        }
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch(msg.arg1){
                        case 0:
                            statusStr = "disconnected";
                            break;
                        case 1:
                            statusStr = "listening for incoming";
                            break;
                        case 2:
                            statusStr = "listening for incoming";
                            break;
                        case 3:
                            statusStr = "CONNECTED!";
                            break;
                        default:
                            statusStr = "??";
                            break;
                    }
                    status.setText(statusStr);

                    /*

                    public static final int STATE_NONE = 0; // we're doing nothing
                    public static final int STATE_LISTEN = 1; // now listening for incoming connections
                    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
                    public static final int STATE_CONNECTED = 3; // now connected to a remote device
                    */

                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;



                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ " + msg.arg1 + " " + msg.arg2);
                    if(bt.queryMode == Bluetooth.QueryMode.GET_OPTIONS && msg.arg2 == 1){
                        Toast toast = Toast.makeText(getContext(), "options received CRC OK!", Toast.LENGTH_SHORT);
                        //toast.setDuration;
                        toast.show();



                    }else if(bt.queryMode == Bluetooth.QueryMode.GET_VERSION) {

                        if (msg.arg2 == 1) {
                            byte[] softVersion = Arrays.copyOfRange(bt.bufferExternalComm, 0, 15);
                          //  String softVersionStr = String.valueOf(softVersion);
                            try {
                                String softVersionStr = new String(softVersion, "UTF-8");
                                tv_software_version.setText(softVersionStr);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            byte[] boardName = Arrays.copyOfRange(bt.bufferExternalComm, 16, 31);
                            try {
                                String boardNameStr = new String(boardName, "UTF-8");
                                tv_name.setText(boardNameStr);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            byte[] boardVersion = Arrays.copyOfRange(bt.bufferExternalComm, 32, 48);
                            try {
                                String boardVersionStr = new String(boardVersion, "UTF-8");
                                tv_board_version.setText(boardVersionStr);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(bt.queryMode == Bluetooth.QueryMode.SAVE_TO_EEPROM){

                        if(msg.arg2 == 1){
                            Toast toast = Toast.makeText(getContext(), "SAVE TO EEPROM OK, will re-read", Toast.LENGTH_SHORT);
                            //toast.setDuration;
                            toast.show();
                            rereadOptions();
                        }
                    } else if(bt.queryMode == Bluetooth.QueryMode.SET_OPTIONS){

                        if(msg.arg2 == 1){
                            Toast toast = Toast.makeText(getContext(), "SAVE OK, will re-read", Toast.LENGTH_SHORT);
                            //toast.setDuration;
                            toast.show();
                            rereadOptions();
                        }
                    } else if(bt.queryMode == Bluetooth.QueryMode.GET_DATA_D) {
                        //byte[] ar = new byte[14];

                        //ar[5] = 4;
                        //InterFragmentCom.setData_d(ar);

                    }
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };

    private void rereadOptions(){
        SystemClock.sleep(1000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                readOptions();
            }
        }, 1000);

    }

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(getContext());
        dialog.setOnDeviceSelectedListener(this);
        //dialog.setTitle(R.string.paired_devices);
        dialog.setTitle("paired devices");
        //dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.setDevices(bt.getPairedDevicesName());
        dialog.showAddress(true);
        dialog.show();
    }

}
