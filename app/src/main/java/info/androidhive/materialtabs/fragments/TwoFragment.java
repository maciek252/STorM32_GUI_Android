package info.androidhive.materialtabs.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.ActionBarActivity;

import info.androidhive.materialtabs.R;


import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;

import org.mavlink.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import info.androidhive.materialtabs.storm32.*;
import quadcopter.Bluetooth;


public class TwoFragment extends Fragment
        implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener,
        View.OnClickListener
{

    public final String TAG = "Main";

    private SeekBar elevation;
    private TextView debug;
    private TextView status;
    private Bluetooth bt;


    enum QueryMode {GET_OPTIONS, GET_VERSION, SET_OPTIONS, SET_VERSION, NONE};
    QueryMode queryMode = QueryMode.NONE;

    String messageBuffer = "";

    byte [] options = null;

    Button button = null;
    Button button_2_readVersion = null;
    Button button_2_setName = null;
    Button button_2_readOptions = null;

    Button button_2_SaveOptions = null;
    Button button_2_readOption = null;
    Button button_2_changeOption = null;
    Button button_2_disconnect = null;
    TextView tv = null;
    TextView tv_connectionStatus = null;
    TextView tv_receivedBt = null;
    TextView tv_name = null;
    TextView tv_version = null;
    TextView tv_board = null;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothSerial bluetoothSerial;

    public TwoFragment() {
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





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_two, container, false);


        View v = inflater.inflate(R.layout.fragment_two, container, false);
        button = (Button) v.findViewById(R.id.buttonDetectBT);
        button.setOnClickListener(this);
        bt = new Bluetooth(getContext(), mHandler);

        debug = (TextView) v.findViewById(R.id.textDebug);
        status = (TextView) v.findViewById(R.id.textStatus);

        v.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectService();
            }
        });

        button_2_readVersion = (Button) v.findViewById(R.id.button_2_ReadVersion);
        button_2_readVersion.setOnClickListener(this);
        button_2_setName = (Button) v.findViewById(R.id.button_2_setName);
        button_2_setName.setOnClickListener(this);

        button_2_readOptions = (Button) v.findViewById(R.id.button_2_readOptions);
        button_2_readOptions.setOnClickListener(this);

        button_2_SaveOptions = (Button) v.findViewById(R.id.button_2_saveOptions);
        button_2_SaveOptions.setOnClickListener(this);

        button_2_readOption = (Button) v.findViewById(R.id.button_2_ReadOption);
        button_2_readOption.setOnClickListener(this);
        button_2_changeOption = (Button) v.findViewById(R.id.button_2_ChangeOption);
        button_2_changeOption.setOnClickListener(this);

        button_2_disconnect = (Button) v.findViewById(R.id.button_2_disconnect);
        button_2_disconnect.setOnClickListener(this);

        tv = (TextView) v.findViewById(R.id.textViewDetectedBT);
        tv_connectionStatus = (TextView) v.findViewById(R.id.textView_2_connectionStatus);
        tv_receivedBt =  (TextView) v.findViewById(R.id.textView_2_rcvBt);

        tv_receivedBt.setMovementMethod(new ScrollingMovementMethod());


        tv_name =  (TextView) v.findViewById(R.id.textView_2_name);
        tv_version = (TextView) v.findViewById(R.id.textView_2_version);
        tv_board = (TextView) v.findViewById(R.id.textView_2_board);


        return v;
    }

    @Override
    public void onClick(View v) {

        queryMode = QueryMode.NONE;

        switch (v.getId()) {
            case R.id.buttonDetectBT:

                IntermediateFragment.setColorToAllControls();

                BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(getActivity());
                dialog.setOnDeviceSelectedListener(this);
                dialog.setTitle("urządzenia sparowane");
                dialog.setDevices(bluetoothSerial.getPairedDevices());
                dialog.showAddress(true);
                dialog.show();
                break;
            case R.id.button_2_ReadVersion:
                //bluetoothSerial.getConnectedDeviceName().



                optionList.voltageCorrection += 1;

                messageBuffer = "";
                //bluetoothSerial.write("v", false);



                //bluetoothSerial.writ
                break;
            case R.id.button_2_setName:
                //  my $res = ExecuteCmdwCrc( 'xn', %name, 0 );
                /*
                sub ExecuteCmdwCrc{
                      my $cmd= shift; my $params= shift; my $reslen= shift;
                        return ExecuteCmd( $cmd.add_crc_to_data($params), $reslen );
                }

                sub ExecuteCmd{
                ## this doesn't help a lot against the heartbeat
                ##  my $cmd = shift;
                ##  WritePort( substr($cmd,0,1) ); # write first char
                ##  $p_Serial->purge_all();
                ##  WritePort( substr($cmd,1) ); # write remaining chars
                ##XXX  $p_Serial->purge_all(); #this helps a bit against the heartbeat!
                    WritePort( shift ); #consumes first parameter, is the command!
                    return ReadPort( shift ); #consumes second parameter, is the length of expected values!
                }

sub add_crc_to_data{
  my $datafield= shift;
  my $crc= do_crc( $datafield, length($datafield) );
  #TextOut( " CRC:".UIntToHexstr($crc) );
  $datafield.= pack( "v", $crc );
  #substr($data,1,1) = 'a'; #test to check if error is detected
  return $datafield;
}


#uses MAVLINK's x25 checksum
#https://github.com/mavlink/pymavlink/blob/master/mavutil.py
sub do_crc{
  my $bufstr= shift; my $len= shift;
  my @buf= unpack( "C".$len, $bufstr );
  my $crc= 0xFFFF;
  foreach my $b (@buf){
     my $tmp= $b ^ ($crc & 0xFF );
     $tmp= ($tmp ^ ($tmp<<4)) & 0xFF;
     $crc= ($crc>>8) ^ ($tmp<<8) ^ ($tmp<<3) ^ ($tmp>>4);
     $crc= $crc & 0xFFFF;
  }
##TextOut( " CRC:0x".UIntToHexstr($crc)."!" );
  return $crc;
}



                 */

                String wej = "testName";
                String o = String.format("%-20s", wej);
                o = o.substring(0,16);

                  bluetoothSerial.write("xn", false);
  //              bluetoothSerial.write(o, false);
                /* //OK DZIAŁA
                byte [] n = new byte[20];
                n[0] = (byte) 0x78;
                n[1] = (byte) 0x6e;
                n[2] = (byte) 0x73;
                n[3] = (byte) 0x6f;
                n[4] = (byte) 0x77;
                n[5] = (byte) 0x61;
                n[6] = (byte) 0x73;
                n[7] = (byte) 0x6f;
                n[8] = (byte) 0x77;
                n[9] = (byte) 0x61;
                n[10] = (byte) 0x20;
                n[11] = (byte) 0x20;
                n[12] = (byte) 0x20;
                n[13] = (byte) 0x20;
                n[14] = (byte) 0x20;
                n[15] = (byte) 0x20;
                n[16] = (byte) 0x20;
                n[17] = (byte) 0x20;
                n[18] = (byte) 0xce;
                n[19] = (byte) 0x86;
                bluetoothSerial.write(n);
*/
                byte [] i = MAVLinkCRC.stringToByte("xn" + o + "cc");
                //i[18] = (byte) 0x93;
                //i[19] = (byte) 0xe6;

                byte [] s= MAVLinkCRC.stringToByte(o);
                int crc = MAVLinkCRC.crc_calculate(s);
                byte[] crcArray = MAVLinkCRC.intToHexVax(crc);
                i[18] = crcArray[0];
                i[19] = crcArray[1];
                bluetoothSerial.write(i);
                /*
                crcArray[0] = 0x9;
                crcArray[1] = 0x3;
                crcArray[2] = 0xe;
                crcArray[3] = 0x6;
                */
                //crcArray[0] = (byte) 0x93;
                //crcArray[1] = (byte) 0x6e;

                //bluetoothSerial.write(crcArray);

                break;
            case R.id.button_2_readOptions:
                options = null;
                queryMode = QueryMode.GET_OPTIONS;
                //bluetoothSerial.write("g");
                bt.sendMessage("g");

                break;
            case R.id.button_2_saveOptions:
                //options = optionList.getOptions();
                options = optionList.options;
                optionList.encodeOptions();
                if(options != null)
                    tv_receivedBt.setText("size of opTions = " + options.length);
                else
                    tv_receivedBt.setText("optins = null");

                byte [] optionsFull = new byte[381];
                optionsFull[0] = 'p';
                //optionsFull[380] = 'e'; // i tak nadpisane, dla testu
                tv_receivedBt.append("oko");
                byte [] optionsWrite = new byte [125*2 + 128 ];
                for(int ii = 0; ii < 125*2 + 128 ; ii++){

                    optionsWrite[ii] = options[ii];
                    optionsFull[ii+1] = options[ii];
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



                break;
            case R.id.button_2_ReadOption:



                tv_receivedBt.setText("  " + String.format("%01X",options[36])  + "," + String.format("%01X",options[37]));
                optionList.voltageCorrection = options[38];


                break;
            case R.id.button_2_ChangeOption:


                if(options != null){
                    options[36] = (byte) 0x04;
                }

                break;

            case R.id.button_2_disconnect:

                bluetoothSerial.stop();
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


        char[] charArray = message.toCharArray();
        byte[] byteArray = message.getBytes();

        //byte[] cookie = Base64.decode(message, Base64.DEFAULT);
        byte[] b = message.getBytes(Charset.forName("UTF-8"));

        //byte[] b2 = message.getBytes(StandardCharsets.UTF_8); // Java 7+ only


        int a1 = (int) byteArray[0];
        int a2 = (int) byteArray[1];
/*
        int i= (byteArray[0]<<24)&0xff000000|
                (byteArray[1]<<16)&0x00ff0000|
                (byteArray[2]<< 8)&0x0000ff00|
                (byteArray[3]<< 0)&0x000000ff;
*/
        int i2 = byteArray[0] & 0xFF;
        int i3 = byteArray[1] & 0xFF;

        int i4 = (byteArray[0] & 0x0F)  * 16 + (byteArray[0] & 0xF0);
        int i5 = (byteArray[0] & 0xF0)  * 16 + (byteArray[0] & 0x0F);

        tv_receivedBt.append("<" + message + ">");

        if(queryMode == QueryMode.GET_OPTIONS){
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
                    /*
                    byte [] subArray = Arrays.copyOfRange(options, 0, 377);

                    int crc = MAVLinkCRC.crc_calculate(subArray);
                    int crc2 = MAVLinkCRC.hexVaxToInt(options[378], options[379]);
                    if(crc2 != crc){

                        Toast toast = Toast.makeText(getContext(), "options received but bad CRC!", Toast.LENGTH_SHORT);
                        //toast.setDuration;
                        toast.show();

                        return;
                    }
                    */


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
        bluetoothSerial.connect(device);
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
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ " + msg.arg1 + " " + msg.arg2);
                    if(msg.arg2 == 1){
                        Toast toast = Toast.makeText(getContext(), "options received CRC OK!", Toast.LENGTH_SHORT);
                        //toast.setDuration;
                        toast.show();
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

}
